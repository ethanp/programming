package base.view;

import base.Main;
import base.p2p.tracker.Swarm;
import base.p2p.tracker.Tracker;
import base.util.TreeTableRoot;

/**
 * Ethan Petuchowski 1/13/15
 *
 * This is based on the mock-up I created in
 * ProgrammingGit/Educational/Java/JavaFX/MyTreeTrial
 */
public class Celery {

    private final Swarm swarm;
    private final Tracker tracker;

    /* subtype checkers */
    public boolean isTracker()  { return tracker != null; }
    public boolean isSwarm()    { return swarm != null; }
    public boolean isRoot()     { return !isTracker() && !isSwarm(); }

    /* wrapping constructors */
    public Celery(Swarm swarm) {
        this.swarm = swarm;
        this.tracker = null;
    }

    public Celery(Tracker tracker) {
        this.swarm = null;
        this.tracker = tracker;
    }

    /** create a "base" SwarmTreeItem used to list
     *  the known Trackers as its children */
    public Celery(TreeTableRoot x) {
        this.swarm = null;
        this.tracker = null;
    }

    public String getName() {
        if (isRoot()) return Main.knownTrackers.size() + " trackers";
        if (isTracker()) return tracker.getIpPortString();
        else return swarm.getP2pFile().getFilename();
    }

    public String getSize() {
        if (isSwarm()) return swarm.getP2pFile().getFilesizeString();
        if (isTracker()) return tracker.getSwarms().size() + " files";
        else return "";
    }

    public String getNumSeeders() {
        if (isSwarm()) return swarm.getSeeders().size()+"";
        else return "";
    }

    public String getNumLeechers() {
        if (isSwarm()) return swarm.getLeechers().size()+"";
        else return "";
    }
}
