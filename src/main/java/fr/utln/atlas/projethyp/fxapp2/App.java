package fr.utln.atlas.projethyp.fxapp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("fxapp02.fxml"));
         BorderPane page = loader.load();
        Scene scene = new Scene(page, 1024, 768);

        primaryStage.setTitle("Java FX Ex02");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
