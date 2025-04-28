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
    TextField first_name, last_name;
    @FXML
    private ComboBox<Major> majorComboBox;
    @FXML
    private MenuItem importCsvMenuItem, exportCsvMenuItem;

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
    @FXML
    private Label statusLabel;

    private boolean isDarkMode;

    @FXML
    private Button editBtn, deleteBtn, addBtn;
    @FXML
    private MenuItem editMenuItem, deleteMenuItem, addMenuItem;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));


        tv.setItems(data);

        updateUIState();

        // Add listeners to form fields to update UI state when values change
        first_name.textProperty().addListener((obs, oldVal, newVal) -> updateUIState());
        last_name.textProperty().addListener((obs, oldVal, newVal) -> updateUIState());
        department.textProperty().addListener((obs, oldVal, newVal) -> updateUIState());
        majorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateUIState());

        // Add listener to table selection
        tv.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateUIState());
        setupMajorDropdown();
    }






    @FXML
    protected void addNewRecord() {
        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String dept = department.getText();
        Major selectedMajor = majorComboBox.getValue();
        String maj = selectedMajor != null ? selectedMajor.getValue() : "";

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
            showStatusMessage("Student added successfully!");
        } else {
            showStatusMessage("Error: Failed to add student.");
        }

    }

    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        majorComboBox.setValue(null);
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
        Major selectedMajor = majorComboBox.getValue();
        String maj = selectedMajor != null ? selectedMajor.getValue() : "";
        p2.setMajor(maj);
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
        Major selectedMajor = majorComboBox.getValue();
        String maj = selectedMajor != null ? selectedMajor.getValue() : "";

        //added new method for deleting the user from the database in ConnDbOps
        boolean success = cdbop.deleteStudentUser(firstName, lastName, dept, maj);

        if (success) {
            showStatusMessage("Student deleted successfully!");
        } else {
            showStatusMessage("Error: Failed to delete student.");
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
        Person p = tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDept());
        String majorValue = p.getMajor();
        for (Major m : Major.values()) {
            if (m.getValue().equals(majorValue)) {
                majorComboBox.setValue(m);
                break;
            }


        }
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
    private void updateUIState() {
        // Get selection state
        boolean hasSelection = tv.getSelectionModel().getSelectedItem() != null;

        // Check form validity for Add operation
        boolean isFormValid = validateForm();

        // Update button states
        editBtn.setDisable(!hasSelection);
        deleteBtn.setDisable(!hasSelection);
        addBtn.setDisable(!isFormValid);

        // Update menu item states
        if (editMenuItem != null) editMenuItem.setDisable(!hasSelection);
        if (deleteMenuItem != null) deleteMenuItem.setDisable(!hasSelection);
        if (addMenuItem != null) addMenuItem.setDisable(!isFormValid);
    }

    private boolean validateForm() {
        // Regex patterns
        String namePattern = "^[A-Za-z\\s'-]{2,30}$"; // 2-30 chars, letters, spaces, hyphens, apostrophes
        String deptPattern = "^[A-Za-z\\s&-]{2,50}$"; // 2-50 chars, letters, spaces, &, hyphens

        boolean isFirstNameValid = first_name.getText().matches(namePattern);
        boolean isLastNameValid = last_name.getText().matches(namePattern);
        boolean isDeptValid = department.getText().matches(deptPattern);
        boolean isMajorSelected = majorComboBox.getValue() != null;

        // Visual feedback for validation
        setValidationStyle(first_name, isFirstNameValid);
        setValidationStyle(last_name, isLastNameValid);
        setValidationStyle(department, isDeptValid);

        return isFirstNameValid && isLastNameValid && isDeptValid && isMajorSelected;
    }

    private void setValidationStyle(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("-fx-border-color: green;");
        } else {
            field.setStyle("-fx-border-color: red;");
        }
    }

    // Add to initialize method
    private void setupMajorDropdown() {
        majorComboBox.getItems().addAll(Major.values());
        majorComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateUIState());
    }
    private void showStatusMessage(String message) {
        statusLabel.setText(message);

        // Clear message after 5 seconds
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                javafx.application.Platform.runLater(() -> statusLabel.setText(""));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    @FXML
    protected void importFromCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());

        if (selectedFile != null) {
            try {
                List<Person> importedPersons = CsvHandler.importPersonsFromCsv(selectedFile);
                data.clear();
                data.addAll(importedPersons);

                // Add to database as well
                for (Person p : importedPersons) {
                    cdbop.insertStudentUser(p.getFirstName(), p.getLastName(), p.getDept(), p.getMajor());
                }

                showStatusMessage("CSV imported successfully. " + importedPersons.size() + " records loaded.");
            } catch (Exception e) {
                showStatusMessage("Error importing CSV: " + e.getMessage());
            }
        }
    }

    @FXML
    protected void exportToCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export to CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());

        if (selectedFile != null) {
            try {
                CsvHandler.exportPersonsToCsv(data, selectedFile);
                showStatusMessage("Data exported successfully to " + selectedFile.getName());
            } catch (Exception e) {
                showStatusMessage("Error exporting to CSV: " + e.getMessage());
            }
        }
    }


    public void exportSelectedRecord(ActionEvent actionEvent) {
        exportToCsv();
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    public void toggleAutoRefresh(ActionEvent actionEvent) {
    }

    public void refreshData(ActionEvent actionEvent) {
    }
}