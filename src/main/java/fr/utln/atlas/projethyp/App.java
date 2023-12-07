package fr.utln.atlas.projethyp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Objects;
import java.util.Properties;

import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;

/**
 * Hello world!
 *
 */
@Log
public class App {
    public static void main(String[] args) throws DataAccessException {
        Cours cours = Cours.builder()
                .id(16)
                .description("Mathématiques")
                .idEnseignant(1)
                .idSalle(3)
                .idMatiere(2)
                .debut(Time.valueOf("16:00:00"))
                .fin(Time.valueOf("17:30:00"))
                .date(Date.valueOf("2023-12-05"))
                .typeCours("CM")
                .build();
        Cours cours2 = Cours.builder()
                .id(64)
                .description("Mathématiques")
                .idEnseignant(1)
                .idSalle(3)
                .idMatiere(2)
                .debut(Time.valueOf("16:00:00"))
                .fin(Time.valueOf("17:30:00"))
                .date(Date.valueOf("2023-12-05"))
                .typeCours("TD").build();

        try (CoursDAO coursDAO = new CoursDAO()) {
            coursDAO.persist(cours);
            coursDAO.update(cours2);

        }

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
