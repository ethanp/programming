package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p2p.file.Chunk;
import p2p.file.P2PFile;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.Socket;
import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.Callable;

/**
 * Ethan Petuchowski 12/29/14
 *
 * facilitates the transfer of a predetermined set of chunks
 * from one predetermined peer to another
 */
public class P2PTransfer implements Callable<Chunk> {

        static final Logger log = LogManager.getLogger(P2PTransfer.class.getName());


    /* I'n not sure if Collection is the right thing here */
    Collection<Integer> chunkIdcs;

    /* I'm not sure if Socket is the right thing here */
    Socket from;
    Socket to;
    P2PFile pFile;


    /* we can create the sockets in here, and Sockets have a configurable timeout parameter
     * when you call
     *
     *      mySocket.connect(SocketAddress, Timeout)
     *
     */
    Timer timeoutTimer; // if connection times-out raise an Exception

    /**
     * Downloads a Chunk from Client to Host-Peer,
     *      or throws an Exception if unable to do so.
     * @return downloaded Chunk
     * @throws Exception if unable to compute a result
     * @throws java.util.concurrent.TimeoutException if transfer times-out
     */
    @Override
    public Chunk call() throws Exception {

        // start the timeoutTimer

        // open a Socket between the Client and the Host-Peer
        // pump the Chunk through it using an Object-Stream

        // if the Timer runs out, throw a TimeoutException

        throw new NotImplementedException();
    }
}
