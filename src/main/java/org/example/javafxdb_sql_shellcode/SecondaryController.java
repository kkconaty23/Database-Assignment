package org.example.javafxdb_sql_shellcode;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import static org.example.javafxdb_sql_shellcode.App.cdbop;

public class SecondaryController {


    @FXML
    private TextField addressID;

    @FXML
    private TextField emailID;

    @FXML
    private TextField nameID;

    @FXML
    private PasswordField passwordID;

    @FXML
    private TextField phoneID;

    @FXML
    private Button registerBtn;





    @FXML
    void registerBtnClick(ActionEvent event) throws IOException {
        String name = nameID.getText();
        String email = emailID.getText();
        String phone = phoneID.getText();
        String address = addressID.getText();
        String password = passwordID.getText();

        boolean success = cdbop.insertUser(name, email, phone, address, password);

        if (success) {
            App.setRoot("primary");
            cdbop.listAllUsers();
        } else {
            System.out.println("Insert failed. Show error to user if needed.");
        }
    }
}