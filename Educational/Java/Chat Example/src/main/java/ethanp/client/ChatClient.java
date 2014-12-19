package ethanp.client;

import ethanp.Constants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

/**
 * This is based on
 * http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
 *
 * A frame with
 *  - a text field for entering messages
 *  - a textarea to see the whole dialog
 *
 * Chat Protocol:
 *  - on receiving "SUBMITNAME", client replies with a screen name
 *  - repeat until server sends a line beginning with "NAMEACCEPTED"
 *  - client can now submit strings to be broadcast
 *  - received texts starting with "MESSAGE" are displayed in the text area
 */
public class ChatClient {

    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("Chatter"); // String becomes the Title on top of the Frame
    JTextField textField = new JTextField(40); // 40 columns of text
    JTextArea messageArea = new JTextArea(8, 40); // 8 rows, 40 cols

    /**
     * - Layout the GUI
     * - Register listener with the textfield so that by pressing Return,
     *     listener sends the textfield contents to the server
     * - Textfield only becomes editable *after* client receives NAMEACCEPTED
     */
    public ChatClient() {

        // Layout GUI
        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();

        // Listen for Return key to send contents to server, then clear
        // For JTextFields, the return key sends an ActionEvent
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                out.println(textField.getText());
                textField.setText("");
            }
        });
    }

    /** Prompt for and return the address of the server. */
    private String getServerAddress() {
        return JOptionPane.showInputDialog(
            frame,
            "Enter IP Address of the Server:", // shows above the textfield
            "Welcome to the Chatter",          // title of the window
            JOptionPane.QUESTION_MESSAGE);
    }

    /** Prompt for and return the desired screen name. */
    private String getName() {

        // showInputDialog returns a String, which we return
        return JOptionPane.showInputDialog(
            frame,
            "Choose a screen name:",
            "Screen name selection",
            JOptionPane.PLAIN_MESSAGE);
    }

    /** Connects to the server then enters the processing loop. */
    private void run() throws IOException {

        // Make connection and initialize streams
        String serverAddress = getServerAddress();

        // Socket can be local because no other methods use it
        // If client enters no-good very-bad address,
        //   this simply throws a 'NoRouteToHostException'
        Socket socket = new Socket(serverAddress, Constants.PORT_NO);

        // gives you readLine(), used for receiving messages according to protocol
        in = new BufferedReader( // buffer text from InputStream speed up rdg of chars/lines
                new InputStreamReader( // bridge from byte stream to char stream
                        socket.getInputStream())); // socket's incoming byte stream

        // let's you use the print/f/ln functions
        out = new PrintWriter(socket.getOutputStream(), true); // 'true' is for "autoflush"

        // Process all messages from server, according to the protocol.
        while (true) {
            String line = in.readLine(); // blocks until a line comes through
            if (line.startsWith("SUBMITNAME")) {
                out.println(getName());
            } else if (line.startsWith("NAMEACCEPTED")) {
                textField.setEditable(true);
            } else if (line.startsWith("MESSAGE")) {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    /** Runs the client as an application with a closeable frame. */
    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
}
