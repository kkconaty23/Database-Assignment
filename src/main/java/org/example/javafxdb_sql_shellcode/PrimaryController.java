package org.example.javafxdb_sql_shellcode;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.*;

import static org.example.javafxdb_sql_shellcode.App.cdbop;

public class PrimaryController {

    @FXML
    private Button createAcctBtn;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void createAcctBtnClick(ActionEvent event) throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    void signInBtnClick(ActionEvent event) throws IOException {
        String email = emailField.getText();
        String password = passwordField.getText();

        boolean valid = cdbop.checkCredentials(email, password);

        if (valid) {
            System.out.println("Login successful!");
            App.setRoot("homepage"); // or home page
        } else {
            System.out.println("Invalid email or password");
            // You can show an alert here
        }
    }

}
