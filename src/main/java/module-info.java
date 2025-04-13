module com.example.rewear {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.example.rewear to javafx.fxml;
    opens com.example.rewear.gestionuser.app.entities to javafx.base;
    opens com.example.rewear.gestionuser.app.gui to javafx.fxml;
    opens com.example.rewear.gestionuser.app.controllers to javafx.fxml;
    opens com.example.rewear.gestionuser.app to javafx.graphics, javafx.fxml;

    exports com.example.rewear;
    exports com.example.rewear.gestionuser.app.entities;
    exports com.example.rewear.gestionuser.app.services;
    exports com.example.rewear.gestionuser.app.gui;
    exports com.example.rewear.gestionuser.app.controllers;
    exports com.example.rewear.gestionuser.app;
}
