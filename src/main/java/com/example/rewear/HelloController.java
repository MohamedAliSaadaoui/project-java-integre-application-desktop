package com.example.rewear;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private Button ConnectButton;
    
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            welcomeText.setText("Welcome, " + username + "!");
            // Here you can add your database connection logic
            try {
                DBUtil.getConnection();
                welcomeText.setText("Welcome, " + username + "! Connected to database successfully.");
            } catch (Exception e) {
                welcomeText.setText("Error connecting to database: " + e.getMessage());
            }
        } else {
            welcomeText.setText("Please enter a username");
        }
    }
}