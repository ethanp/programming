package page.ethanp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

record Utxo(
    // What it represents:
    //
    // A unique identifier for the transaction that created this Utxo. This is typically a cryptographic hash
    // (like SHA-256) of the transaction data.
    //
    // Why it's needed:
    //
    // * Uniquely identifies the transaction that spawned the Utxo.
    // * Ensures each Utxo can be traced back to its origin.
    // * Prevents double-spending (you can't spend the same Utxo twice).
    //
    String txId,

    // What it represents:
    //The position (index) of this specific output within its parent transaction. Transactions can have multiple outputs, so this index distinguishes between them.
    //
    //Why it's needed:
    //
    //Identifies which output in a multi-output transaction you're referring to.
    //
    //Example: A transaction with two outputs (e.g., sending coins to Alice and Bob) would have outputIndex = 0 for Alice's output and outputIndex = 1 for Bob's.
    int outputIndex, String owner, double amount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Utxo utxo = (Utxo) o;
        return outputIndex == utxo.outputIndex && txId.equals(utxo.txId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(txId, outputIndex);
    }
}

class Transaction {

    private final String txId;
    private final List<Utxo> inputs;
    private final List<Map.Entry<String, Double>> outputs;
    private final byte[] signature;

    public Transaction(Wallet wallet, List<Utxo> inputs,
                       List<Map.Entry<String, Double>> outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.txId = createTxIdByHashingContents();
        this.signature = wallet.sign(txId);
    }

    private String createTxIdByHashingContents() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = inputs + outputs.toString();
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValid(List<Utxo> validUtxos) {
        // Verify all inputs are valid Utxos
        if (!new HashSet<>(validUtxos).containsAll(inputs)) {
            return false;
        }

        // Verify signature matches FIRST INPUT'S owner
        String signingAddress = inputs.isEmpty() ? "" : inputs.getFirst().owner();
        if (!Wallet.verify(txId, signature, signingAddress)) {
            return false;
        }

        // Verify input sum >= output sum
        double inputSum = inputs.stream().mapToDouble(Utxo::amount).sum();
        double outputSum = outputs.stream().mapToDouble(Map.Entry::getValue).sum();
        return inputSum >= outputSum;
    }

    public List<Utxo> getInputs() {
        return inputs;
    }

    public List<Map.Entry<String, Double>> getOutputs() {
        return outputs;
    }

    public String getTxId() {
        return txId;
    }
}

class Block {

    private final int index;
    private final long timestamp;
    private final List<Transaction> transactions;
    private final String previousHash;
    private final String hash;
    private final int nonce;
    static final int DIFFICULTY = 2;

    public Block(int index, List<Transaction> transactions, String previousHash) {
        this.index = index;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timestamp = System.currentTimeMillis();

        // "Proof of work" -- find valid nonce and hash values
        int nonceTrial = 0;
        String hashTrial = computeHash();
        String target = new String(new char[DIFFICULTY]).replace('\0', '0');
        while (!hashTrial.startsWith(target)) {
            nonceTrial++;
            hashTrial = computeHash();
        }

        this.nonce = nonceTrial;
        this.hash = hashTrial;
    }

    String computeHash() {
        try {
            final String input = "" + index + timestamp + transactions.toString() + previousHash + nonce;
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            final StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHash() {
        return hash;
    }

    public Iterable<? extends Transaction> getTransactions() {
        return transactions;
    }

    public int getIndex() {
        return index;
    }

    public String getPreviousHash() {
        return previousHash;
    }
}

class P2PNode {

    private final Blockchain blockchain;
    private final List<InetAddress> peers = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;
    private boolean running = true;
    private final int port;

    public P2PNode(Blockchain blockchain, int port) {
        this.blockchain = blockchain;
        this.port = port;
        startServer();
        discoverPeers();
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                System.out.println("Server stopped: " + e.getMessage());
            }
        }).start();
    }

    private void discoverPeers() {
        // Simple peer discovery - connect to localhost peers for demo
        try {
            for (int i = 8080; i < 8084; i++) { // Connect to potential peers
                InetAddress address = InetAddress.getByName("localhost");
                if (i != port) {
                    if (connectToPeer(address, i)) {
                        peers.add(address);
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private boolean connectToPeer(InetAddress address, int port) {
        try (Socket socket = new Socket(address, port)) {
            sendMessage(socket, "CONNECT");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @SuppressWarnings("unused")
    public void broadcast(Object data) {
        for (InetAddress peer : peers) {
            try (Socket socket = new Socket(peer, port)) {
                sendMessage(socket, data);
            } catch (IOException e) {
                System.out.println("Error broadcasting to " + peer);
            }
        }
    }

    private void sendMessage(Socket socket, Object data) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(data);
    }

    class ClientHandler implements Runnable {

        private final Socket clientSocket;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
                Object message = in.readObject();
                handleMessage(message);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Client disconnected: " + e.getMessage());
            }
        }

        private void handleMessage(Object message) {
            if (message instanceof String) {
                if ("CONNECT".equals(message)) {
                    peers.add(clientSocket.getInetAddress());
                }
            } else if (message instanceof Transaction) {
                blockchain.addTransaction((Transaction) message);
            } else if (message instanceof Block) {
                blockchain.addBlock((Block) message);
            } else if (message instanceof Blockchain) {
                blockchain.replaceChain((Blockchain) message);
            }
        }
    }

    public void stop() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Blockchain {

    private List<Block> chain = new ArrayList<>();
    private final List<Transaction> pendingTransactions = new ArrayList<>();
    private Set<Utxo> utxoPool = new HashSet<>();

    private static final double INITIAL_SUPPLY = 1000.0;

    public Blockchain(Wallet genesisWallet) {
        // Create genesis block
        Transaction genesisTx = new Transaction(
            genesisWallet,
            new ArrayList<>(),
            List.of(new AbstractMap.SimpleEntry<>(genesisWallet.getAddress(), INITIAL_SUPPLY))
        );
        utxoPool.add(new Utxo(genesisTx.getTxId(), 0, genesisWallet.getAddress(), INITIAL_SUPPLY));
        chain.add(new Block(0, List.of(genesisTx), "0"));
    }

    // In Blockchain class
    public void addTransaction(Transaction tx) {
        if (tx.isValid(new ArrayList<>(utxoPool))) {
            pendingTransactions.add(tx);
        } else {
            throw new RuntimeException("Invalid transaction");
        }
    }

    public void addBlock(Block block) {
        // Validate all transactions in the block
        for (Transaction tx : block.getTransactions()) {
            if (!tx.isValid(new ArrayList<>(utxoPool))) {
                throw new RuntimeException("Block contains invalid transactions");
            }
        }

        // Update UTXO pool
        for (Transaction tx : block.getTransactions()) {
            tx.getInputs().forEach(utxoPool::remove);
            int outputIndex = 0;
            for (Map.Entry<String, Double> output : tx.getOutputs()) {
                Utxo newUtxo = new Utxo(
                    tx.getTxId(),
                    outputIndex++,
                    output.getKey(),
                    output.getValue()
                );
                utxoPool.add(newUtxo);
            }
        }

        // Add to chain
        chain.add(block);
    }

    public void mineBlock() {
        if (pendingTransactions.isEmpty()) {
            return;
        }

        Block newBlock = new Block(
            chain.size(),
            new ArrayList<>(pendingTransactions),
            chain.getLast().getHash()
        );

        addBlock(newBlock);
        pendingTransactions.clear();
    }

    public boolean isValid() {
        Set<Utxo> tempUtxos = new HashSet<>();
        String target = new String(new char[Block.DIFFICULTY]).replace('\0', '0');

        // Validate genesis block
        Block genesis = chain.getFirst();
        if (genesis.getIndex() != 0 ||
            !genesis.getPreviousHash().equals("0") ||
            !genesis.getHash().equals(genesis.computeHash())) {
            return false;
        }

        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            // Standard block validation
            if (!current.getPreviousHash().equals(previous.getHash())) {
                return false;
            }
            if (!current.getHash().startsWith(target)) {
                return false;
            }
            if (!current.getHash().equals(current.computeHash())) {
                return false;
            }

            // Transaction validation
            for (Transaction tx : current.getTransactions()) {
                if (!tx.isValid(new ArrayList<>(tempUtxos))) {
                    return false;
                }

                // Update temporary Utxo set
                tx.getInputs().forEach(tempUtxos::remove);
                int outputIndex = 0;
                for (Map.Entry<String, Double> output : tx.getOutputs()) {
                    tempUtxos.add(new Utxo(
                        tx.getTxId(),
                        outputIndex++,
                        output.getKey(),
                        output.getValue()
                    ));
                }
            }
        }
        return true;
    }

    public void replaceChain(Blockchain newChain) {
        if (newChain.isValid() && newChain.getChain().size() > chain.size()) {
            chain = newChain.getChain();
            utxoPool = newChain.getUtxoPool();
        }
    }

    private Set<Utxo> getUtxoPool() {
        return utxoPool;
    }

    private List<Block> getChain() {
        return chain;
    }
}

class Wallet {

    private final KeyPair keyPair;

    public Wallet() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(256);
            keyPair = keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] sign(String data) {
        try {
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initSign(keyPair.getPrivate());
            ecdsa.update(data.getBytes());
            return ecdsa.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(String data, byte[] signature, String address) {
        try {
            Signature ecdsa = Signature.getInstance("SHA256withECDSA");
            ecdsa.initVerify(getPublicKey(address));
            ecdsa.update(data.getBytes());
            return ecdsa.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return false;
        }
    }

    public String getAddress() {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    private static PublicKey getPublicKey(String address) {
        byte[] decoded = Base64.getDecoder().decode(address);
        try {
            return KeyFactory.getInstance("EC")
                .generatePublic(new X509EncodedKeySpec(decoded));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Main {

    // Simple CLI
    public static void main(String[] args) {
        Wallet genesisWallet = new Wallet();
        Blockchain blockchain = new Blockchain(genesisWallet);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Port (8080-8083)");
        int port = scanner.nextInt();
        if (port < 8080 || port > 8083) {
            System.err.println("Port %d out of range".formatted(port));
        }
        P2PNode node = new P2PNode(blockchain, port);

        while (true) {
            System.out.println("\n1. Send transaction\n2. Mine block\n3. Show balance\n4. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    // Transaction creation logic
                }
                case 2 -> blockchain.mineBlock();
                case 3 -> {
                    // Balance check logic
                }
                case 4 -> {
                    node.stop();
                    System.exit(0);
                }
            }
        }
    }
}