package tracker.view;

import p2p.tracker.Swarm;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Ethan Petuchowski 1/14/15
 */
public class TrackerViewCtrl {
    @FXML private ListView<Swarm> pFileList;
    @FXML private ListView<Swarm> seederList;
    @FXML private ListView<Swarm> leecherList;
    private List<ListView<Swarm>> lists;

    @FXML private void initialize() {

    }
}
