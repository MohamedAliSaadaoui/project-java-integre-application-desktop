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
    
    exports com.example.rewear;
    exports com.example.rewear.controller;
    exports com.example.rewear.util;
}