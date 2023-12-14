package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.daos.DevoirDAO;
import fr.utln.atlas.projethyp.daos.Page;
import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestDevoir {
    @Test
    public void testDevoir1() throws DataAccessException {
        Devoir devoir = Devoir.builder()
                .id(1)
                .idUtilisateur(1)
                .typeDevoir(Devoir.TypeDevoir.Surveille)
                .note(15)
                .commentaire("Bon travail, quelques coquilles sur les formules mathématiques mais rien d'inquiétant.")
                .build();
        Devoir devoir1 = Devoir.builder()
                .id(1)
                .idUtilisateur(2)
                .typeDevoir(Devoir.TypeDevoir.Maison)
                .note(12)
                .commentaire("Bon travail, cependant il faut revoir les notions de bases du cours.")
                .build();
        assertNotNull(devoir1);
        assertNotNull(devoir);

        try (DevoirDAO devoirDAO = new DevoirDAO()) {
            devoirDAO.persist(devoir);
            Page<Devoir> devoirPage = devoirDAO.findNotesUser(1, 2, 1);
            assertNotNull(devoirPage);
            System.out.println(devoirPage);
            devoirDAO.update(devoir1);
        }
    }
}
