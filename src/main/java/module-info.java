module rewear {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    opens com.example.rewear to javafx.fxml;
    opens com.example.rewear.Gestionevent to javafx.fxml;
    opens com.example.rewear.Gestionevent.Controller to javafx.fxml;

    exports com.example.rewear;
    exports com.example.rewear.Gestionevent;
    exports com.example.rewear.Gestionevent.Controller;
}