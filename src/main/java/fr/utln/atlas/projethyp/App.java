package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;

import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

/**
 * Hello world!
 *
 */
@Log
public class App {
    public static void main(String[] args) throws DataAccessException {

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