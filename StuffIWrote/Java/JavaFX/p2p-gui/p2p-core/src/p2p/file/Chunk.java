package p2p.file;

/**
 * Ethan Petuchowski 1/8/15
 */
public abstract class Chunk {

    /* until I have a reason to change it */
    public static final int CHUNK_SIZE = 1 << 15;

    /* Should this be a "property"?  Would that add a ton of space overhead? */
    protected byte[] data;
}
