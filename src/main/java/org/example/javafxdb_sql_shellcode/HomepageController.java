package org.example.javafxdb_sql_shellcode;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static org.example.javafxdb_sql_shellcode.App.cdbop;


public class HomepageController implements Initializable {

    private final ObservableList<Person> data =
            FXCollections.observableArrayList(
                    new Person(1, "Jacob", "Smith", "CPIS", "CS"),
                    new Person(2, "Jacob2", "Smith1", "CPIS1", "CS")

            );


    @FXML
    TextField first_name, last_name, major;
    @FXML
    TextField department;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_dept, tv_major;

    @FXML
    ImageView img_view;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private MenuItem themeToggleItem;
    @FXML
    private Button showBtn;

    private boolean isDarkMode;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));


        tv.setItems(data);
    }




    @FXML
    protected void addNewRecord() {
        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String dept = department.getText();
        String maj = major.getText();

        // Add to the TableView
        data.add(new Person(
                data.size() + 1,
                firstName,
                lastName,
                dept,
                maj
        ));

        // Insert into the database
        //using he same logic as secondary controller, but adds to different database
        boolean success = cdbop.insertStudentUser(firstName, lastName, dept, maj);

        if (success) {
            System.out.println("Student user inserted successfully.");

        } else {
            System.out.println("Insert failed. Show error to user if needed.");
        }
    }

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        major.setText("");
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    @FXML
    protected void editRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        int c=data.indexOf(p);
        Person p2= new Person();
        p2.setId(c+1);
        p2.setFirstName(first_name.getText());
        p2.setLastName(last_name.getText());
        p2.setDept(department.getText());
        p2.setMajor(major.getText());
        data.remove(c);
        data.add(c,p2);
        tv.getSelectionModel().select(c);
    }

    @FXML
    protected void deleteRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        data.remove(p);

        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String dept = department.getText();
        String maj = major.getText();

        //added new method for deleting the user from the database in ConnDbOps
        boolean success = cdbop.deleteStudentUser(firstName, lastName, dept, maj);

        if (success) {
            System.out.println("Student user deleted successfully.");
            // optionally refresh from DB here
        } else {
            System.out.println("Delete failed. Show error to user if needed.");
        }

    }



    @FXML
    protected void showImage() {
        File file= (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if(file!=null){
            img_view.setImage(new Image(file.toURI().toString()));

        }
    }





    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p= tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDept());
        major.setText(p.getMajor());


    }



    /**
     * Allows toggling between light and dark mode, application starts in light mode
     */
    @FXML
    public void toggleTheme() {
        Scene scene = rootPane.getScene();

        if (isDarkMode) {
            scene.getStylesheets().remove(getClass().getResource("styling/dark.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("styling/light.css").toExternalForm());
            themeToggleItem.setText("Switch to Dark Mode");
        } else {
            scene.getStylesheets().remove(getClass().getResource("styling/light.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("styling/dark.css").toExternalForm());
            themeToggleItem.setText("Switch to Light Mode");
        }

        isDarkMode = !isDarkMode;
    }

    /**
     * added a show button to display all users in the table view
     * @param event
     */
    @FXML
    void ShowBtnClick(ActionEvent event) {
        List<Person> users = cdbop.listAllStudentUsers();
        if (users != null) {
            ObservableList<Person> data = FXCollections.observableArrayList(users);
            tv.setItems(data);
        } else {
            System.out.println("null");
        }
    }

}