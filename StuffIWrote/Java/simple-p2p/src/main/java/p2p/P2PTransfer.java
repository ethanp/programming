package p2p;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
    Peer from;
    Peer to;
    Collection<Integer> chunkIdcs;
    Timer timeoutTimer; // if connection times-out raise an Exception
    boolean run() throws TimeoutException {
        throw new NotImplementedException();
    }
}
