package com.example.sudokuguifx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class PasswordController {

    private int tries = 3;
    private AtomicBoolean passwordCorrect;

    @FXML
    private Label triesLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button passwordEnter;
    @FXML
    private Button cancelButton;

    public void setPasswordCorrect(AtomicBoolean passwordCorrect) {
        this.passwordCorrect = passwordCorrect;
    }

    @FXML
    private void handleEnterPassword() {
        String correctPassword = "1234";
        // Get the entered password and compare to correct
        if (passwordField.getText().equals(correctPassword)) {
            passwordCorrect.set(true);
            closeWindow();
        } else {
            passwordCorrect.set(false);
            passwordField.clear();
            --tries;
        }

        // Update tries left
        triesLabel.setText("Tries left: " + tries);

        // If 0 tries left close window
        if (tries == 0) {
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) passwordEnter.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        passwordCorrect.set(false);
        closeWindow();
    }

}
