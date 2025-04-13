package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaces/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        stage.setScene(scene);
        stage.setTitle("Connexion");
        stage.show();
    }

    public static void changeScene(String fxmlFile) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/interfaces/" + fxmlFile));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(); // Démarre JavaFX
    }
}
