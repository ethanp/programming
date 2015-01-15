package client.util;

import client.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Ethan Petuchowski 1/14/15
 */
public class ViewUtil {
    public static void showOnRightClick(Node cell, ContextMenu menu) {
        cell.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                event.consume();
                menu.show(Main.getPrimaryStage(), event.getScreenX(), event.getScreenY());
            }
        });
    }

    public static void addOpt(ContextMenu menu, String text, EventHandler<ActionEvent> action) {
        MenuItem menuItem = new MenuItem(text);
        menu.getItems().add(menuItem);
        menuItem.setOnAction(action);
    }
}
