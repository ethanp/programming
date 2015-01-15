package p2p.local;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 * Ethan Petuchowski 1/14/15
 *
 * Just another run-of-the-mill multithreaded ServerSocket scenario
 *
 * It's only abstract because I haven't implemented it yet.
 */
public abstract class LocalServer {
    public abstract InetSocketAddress getServerSocketAddr();
    ServerSocket socket;
}
