package base.p2p.file;

import com.sun.demo.jvmti.hprof.Tracker;

import java.nio.file.Path;
import java.util.List;

/**
 * Ethan Petuchowski 1/7/15
 */
public abstract class P2PFile {
    Path localPath;
    List<Tracker> trackers;
}
