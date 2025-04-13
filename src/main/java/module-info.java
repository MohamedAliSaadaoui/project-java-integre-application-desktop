module com.example.rewear {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.sql;
    requires batik.transcoder;
    requires batik.util;
    requires batik.awt.util;
    requires batik.constants;

    opens com.example.rewear to javafx.fxml;
    opens com.example.rewear.controller to javafx.fxml;
    opens com.example.rewear.model to javafx.fxml;
    opens com.example.rewear.service to javafx.fxml;
    
    exports com.example.rewear;
    exports com.example.rewear.controller;
    exports com.example.rewear.util;
    exports com.example.rewear.model;
    exports com.example.rewear.service;
}