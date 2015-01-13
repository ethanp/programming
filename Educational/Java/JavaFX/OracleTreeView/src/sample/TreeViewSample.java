package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

/**
 * based on Oracle's example at
 * http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm
 */
public class TreeViewSample extends Application {
    public static void main(String[] args) { launch(args); }

    private final Node rootIcon = new ImageView(
            new Image(getClass().getResourceAsStream("zoom.png")));

    private final Image depIcon =
            new Image(getClass().getResourceAsStream("zip.png"));

    List<Employee> employees = Arrays.asList(
            new Employee("Ethan Williams", "Sales Department"),
            new Employee("Emma Jones", "Sales Department"),
            new Employee("Michael Brown", "Sales Department"),
            new Employee("Anna Black", "Sales Department"),
            new Employee("Rodger York", "Sales Department"),
            new Employee("Susan Collins", "Sales Department"),
            new Employee("Mike Graham", "IT Support"),
            new Employee("Judy Mayer", "IT Support"),
            new Employee("Gregory Smith", "IT Support"),
            new Employee("Jacob Smith", "Accounts Department"),
            new Employee("Isabella Johnson", "Accounts Department"));

    TreeItem<String> rootNode = new TreeItem<>("MyCompany Human Resource", rootIcon);

    @Override public void start(Stage stage) {
        rootNode.setExpanded(true);
        for (Employee employee : employees) {
            TreeItem<String> empLeaf = new TreeItem<>(employee.getName());
            boolean found = false;
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(employee.getDepartment())) {
                    depNode.getChildren().add(empLeaf);
                    found = true;
                    break;
                }
            }
            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(
                        employee.getDepartment(),
                        new ImageView(depIcon)
                );
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(empLeaf);
            }
        }

        stage.setTitle("Tree View Sample");
        VBox box = new VBox();
        final Scene scene = new Scene(box, 400, 300);
        scene.setFill(Color.LIGHTGRAY);

        TreeView<String> treeView = new TreeView<>(rootNode);
        treeView.setEditable(true);
        treeView.setCellFactory(e -> new TextFieldTreeCellImpl());
        box.getChildren().add(treeView);
        stage.setScene(scene);
        stage.show();
    }

    public static class Employee {
        private final SimpleStringProperty name;
        private final SimpleStringProperty department;

        public Employee(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
        }

        public String getName() { return name.get(); }
        public SimpleStringProperty nameProperty() { return name; }
        public void setName(String name) { this.name.set(name); }
        public String getDepartment() { return department.get(); }
        public SimpleStringProperty departmentProperty() { return department; }
        public void setDepartment(String department) {
            this.department.set(department);
        }
    }

    private final class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl() {
            MenuItem addMenuItem = new MenuItem("Add Employee");
            addMenu.getItems().add(addMenuItem);
            addMenuItem.setOnAction(event -> {
                TreeItem<String> newEmployee = new TreeItem<>("New Employee");
                getTreeItem().getChildren().add(newEmployee);
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override public void handle(KeyEvent event) {
                    if (event.getCode() == KeyCode.ENTER)
                        commitEdit(textField.getText());
                    else if (event.getCode() == KeyCode.ESCAPE)
                        cancelEdit();
                }
            });
        }

        @Override public void startEdit() {
            super.startEdit();
            if (textField == null) {
                /*
                instantiate the textField with the curr text
                and install an EventHandler to have it commit
                on ENTER and cancel on ESCAPE
                */
                createTextField();
            }
            setText(null); // get the text out of the way
            setGraphic(textField); // replace the text with the textField
            textField.selectAll(); // select the text inside the input box
        }

        @Override public void cancelEdit() {
            super.cancelEdit();
            /* set the Cell's Label to contain the content of its underlying
             * TreeItem<String> item; property */
            setText(getItem());
            /* Labelled Nodes have "graphic" properties that are located in
             * a configurable location wrt to their text */
            setGraphic(getTreeItem().getGraphic());
        }

        /** this gets called automagically by the framework
         * THIS IS WHERE THE ORIGINAL TEXT GETS SET!
         * if you don't override this baby right, everything goes haywire
         * the most important thing is calling super.
         */
        @Override protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (isEditing()) {
                    if (textField != null)
                        textField.setText(getString());
                    setText(null);
                    setGraphic(textField);
                }
                else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null)
                        setContextMenu(addMenu);
                }
            }
            else {
                setText(null);
                setGraphic(null);
            }
        }

    }
}
