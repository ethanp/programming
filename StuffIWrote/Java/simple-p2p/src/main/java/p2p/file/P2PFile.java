package p2p.file;

import com.sun.demo.jvmti.hprof.Tracker;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.net.URL;

/**
 * Ethan Petuchowski 12/29/14
 */
public class P2PFile {

    File file;
    URL trackerURL;

    /* TODO what is a good way to serialize this thing over The Networks? */
    /* TODO what is a stream and would that be a good way to serialize this thing? */

    P2PFile(String fileName) {
        throw new NotImplementedException();
    }

    byte[] getChunkNum(int chunkNum) {
        throw new NotImplementedException();
    }
}
