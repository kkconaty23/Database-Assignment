package org.example.javafxdb_sql_shellcode;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.sql.*;

public class PrimaryController {

    @FXML
    private Button createAcctBtn;

    @FXML
    void createAcctBtnClick(ActionEvent event) throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

}
