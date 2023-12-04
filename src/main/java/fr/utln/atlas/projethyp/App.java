package fr.utln.atlas.projethyp;
import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.daos.MatiereDAO;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.DateSemaine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.entities.Matiere;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;
import org.h2.tools.Server;

import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

/**
 * Hello world!
 *
 */
@Log
public class App {
    public static void main(String[] args) throws DataAccessException {

        MatiereDAO matiereDAO = new MatiereDAO();

        String testmat = matiereDAO.findMatId(4);
        log.info(testmat);

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
