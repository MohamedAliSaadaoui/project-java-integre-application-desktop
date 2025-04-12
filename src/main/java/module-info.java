module com.example.rewear {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;


    opens com.example.rewear to javafx.fxml;
    opens com.example.rewear.controller to javafx.fxml;
    
    exports com.example.rewear;
    exports com.example.rewear.controller;
}