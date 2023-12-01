package fr.utln.atlas.projethyp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("main.fxml"));
        Parent rootNode = loader.load();
        Scene scene = new Scene(rootNode);

        primaryStage.setTitle("Hyper-planning");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
