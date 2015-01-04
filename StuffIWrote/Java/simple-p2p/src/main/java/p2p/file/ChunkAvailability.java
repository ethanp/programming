package p2p.file;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Ethan Petuchowski 1/3/15
 */
public class ChunkAvailability implements Comparable<ChunkAvailability> {
    public int chunkIdx;
    public Set<InetSocketAddress> ownerAddrs = new HashSet<>();

    public int numOwners() { return ownerAddrs.size(); }
    public void addOwner(InetSocketAddress addr) { ownerAddrs.add(addr); }
    public void removeOwner(InetSocketAddress addr) { ownerAddrs.remove(addr); }
    public ChunkAvailability(int idx) { chunkIdx = idx; }

    public static List<ChunkAvailability> createList(BitSet completeChunks, int totalChunks) {
        int numChunksLeft = totalChunks - completeChunks.cardinality();
        List<ChunkAvailability> arr = new ArrayList<>(numChunksLeft);
        for (int i = 0; i < totalChunks; i++)
            if (!completeChunks.get(i))
                arr.add(new ChunkAvailability(i));
        return arr;
    }

    /**
     * less-than if replication is LOWER;
     * or if replication is SAME, then less-than if chunkIdx is LOWER
     */
    @Override
    public int compareTo(ChunkAvailability o) {
        int repDiff = numOwners()-o.numOwners();
        if (repDiff != 0) return repDiff;
        return chunkIdx-o.chunkIdx;
    }
}
