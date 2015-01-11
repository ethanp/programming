package base.p2p.tracker;

import base.p2p.file.P2PFile;
import base.p2p.peer.Peer;

import java.util.Collection;

/**
 * Ethan Petuchowski 1/10/15
 *
 * I'm not sure whether someone is going to need to inherit
 * this or if one implementation of this guy is enough
 */
public class Swarm {
    protected Collection<Peer> leechers;
    protected Collection<Peer> seeders;
    protected P2PFile p2pFile;
}
