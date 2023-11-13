package fr.utln.atlas.projethyp.planningview;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                App.class.getResource("Test_Planning_Template.fxml"));

        AnchorPane page = loader.load();
        Scene scene = new Scene(page);

        primaryStage.setTitle("Java FX Ex01");
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
