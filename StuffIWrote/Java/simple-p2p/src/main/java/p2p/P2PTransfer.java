package p2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.Socket;
import java.util.Collection;
import java.util.Timer;
import java.util.concurrent.TimeoutException;

/**
 * Ethan Petuchowski 12/29/14
 *
 * facilitates the transfer of a predetermined set of chunks
 * from one predetermined peer to another
 *
 * TODO should this extend Thread or Callable<T> (e.g. Boolean?) or something?
 */
public class P2PTransfer {

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

    boolean run() throws TimeoutException {
        throw new NotImplementedException();
    }

    P2PTransfer(Socket from, Socket to, Collection<Integer> chunkIds, P2PFile pFile) {
        this.from = from;
        this.to = to;
        this.chunkIdcs = chunkIds;
        this.pFile = pFile;
    }

    boolean sendChunk() {
        throw new NotImplementedException();
    }
}
