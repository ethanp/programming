package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        /* this is displayed on the top of the window */
        primaryStage.setTitle("Welcome");
        GridPane grid = new GridPane();

        /* position defaults to top-left */
        grid.setAlignment(Pos.CENTER);

        /* set gap between rows & columns */
        grid.setHgap(10);
        grid.setVgap(10);

        /* space around grid pane itself */
        grid.setPadding(new Insets(25, 25, 25, 25));

        /* this is a text object that canNOT be edited */
        Text scenetitle = new Text("Welcome Back");

        /* better to use a stylesheet when there's more styling that needs to happen */
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        /* put it in the top left grid cell, taking two columns of one row */
        grid.add(scenetitle, 0, 0, 2, 1);

        Label username = new Label("User Name: ");
        grid.add(username, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        /* showing grid lines is useful for debugging */
        grid.setGridLinesVisible(true);

        Button btn = new Button("Sign in");

        /* a box to situate the button inside of,
           with "spacing" [think CSS] of 10 */
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);

        final Text actionTarget = new Text();
        grid.add(actionTarget, 1, 6);

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                actionTarget.setFill(Color.FIREBRICK);
                actionTarget.setText("Sign in button pressed");
            }
        });

        /* make the grid pane the root node, so it resizes with the window */
        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
