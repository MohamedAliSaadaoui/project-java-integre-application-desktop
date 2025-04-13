package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class Login {

        @FXML
        private TextField emailField;

        @FXML
        private PasswordField passwordField;

        @FXML
        private void handleLogin() {
                System.out.println("Connexion avec : " + emailField.getText());
        }

        @FXML
        private void goToRegister() throws Exception {
                app.Main.changeScene("register.fxml");
        }
}