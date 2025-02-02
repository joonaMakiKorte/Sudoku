package com.example.sudokuguifx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PasswordController {

    private AtomicInteger tries;
    private AtomicBoolean passwordCorrect;

    @FXML
    private Label triesLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button passwordEnter;
    @FXML
    private Button cancelButton;

    public void setPasswordCorrect(AtomicBoolean passwordCorrect, AtomicInteger tries) {
        this.passwordCorrect = passwordCorrect;
        this.tries = tries;
        triesLabel.setText("Tries left: " + tries.get());
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
            tries.decrementAndGet();
        }

        // Update tries left
        triesLabel.setText("Tries left: " + tries.get());

        // If 0 tries left close window
        if (tries.get() == 0) {
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
