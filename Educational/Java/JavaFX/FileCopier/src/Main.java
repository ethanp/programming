import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Based in part upon http://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
 */
public class Main extends Application {

    @Override
    public void start(final Stage stage) {
        stage.setTitle("File Copier"); // window title

        /* seems to default to last location used by a FileChooser (probably system-wide) */
        final FileChooser fileChooser = new FileChooser();

        /* added to the grid later, but onAction set now */
        final Button openButton = new Button("Choose file to copy");

        /* what to do when the openButton Button is buttoned */
        openButton.setOnAction(e -> {

            /* receive the user's selected File */
            File srcFile = fileChooser.showOpenDialog(stage);
            if (srcFile != null) {

                /* obtain which directory to save to */
                File saveDir = new DirectoryChooser().showDialog(stage);
                copyFileInto(srcFile, saveDir);
            }
        });

        /* pane onto which the button-grid shall be placed */
        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().add(openButton);

        stage.setScene(new Scene(rootGroup));
        stage.show();
    }

    private void copyFileInto(File srcFile, File saveDir) {
        final File destFile = new File(saveDir, srcFile.getName());
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcFile));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
            byte[] buff = new byte[1024];
            int len;
            while ((len = in.read(buff)) > 0)
                out.write(buff, 0, len);
            out.flush();
        }
        catch (IOException e1) { e1.printStackTrace(); }
    }

    public static void main(String[] args) { Application.launch(args); }
}
