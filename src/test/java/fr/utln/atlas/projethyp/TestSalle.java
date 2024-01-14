package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.daos.SalleDAO;
import fr.utln.atlas.projethyp.entities.Salle;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestSalle {

    @DisplayName("Test de la Salle")
    @Test
    public void testSalle() throws DataAccessException {
        Salle salle = Salle.builder()
                .id(5)
                .nomSalle("U.001")
                .nombrePlace(20)
                .build();

        try {
            SalleDAO salleDAO = InitDAOS.getSalleDAO();
            salleDAO.persist(salle);
            Page<Salle> sallePage = salleDAO.findNotTakenRoom(Date.valueOf("2023-12-10"), Time.valueOf("15:00:00"), 10, 10);
            assertNotNull(sallePage);
            System.out.println(sallePage);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }
}
