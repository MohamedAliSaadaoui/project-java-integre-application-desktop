module com.example.rewear {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.example.rewear to javafx.fxml;
    opens entities to javafx.base;
    opens gui to javafx.fxml;
    opens controllers to javafx.fxml;
    opens app to javafx.graphics, javafx.fxml;

    exports com.example.rewear;
    exports entities;
    exports services;
    exports gui;
    exports controllers;
    exports app;
}
