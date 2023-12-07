package fr.utln.atlas.projethyp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.exceptions.DataAccessException;
public class App extends Application {

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
  public static void main(String[] args) throws DataAccessException{
      launch(args);
    }

    static void loadProperties(String propFileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputstream = App.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputstream == null) throw new FileNotFoundException();
        properties.load(inputstream);
        System.setProperties(properties);
    }

    static void configureLogger() {
        //Regarder src/main/ressources/logging.properties pour fixer le niveau de log
        String path;
        path = Objects.requireNonNull(App.class.getClassLoader().getResource("logging.properties")).getFile();
        System.setProperty("java.util.logging.config.file", path);
    }
}
