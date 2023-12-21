package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UtilisateurTest {

    @Test
    public void testUserInit() throws DataAccessException {
        Utilisateur utilisateur = InitDAOS.getUtilisateurDAO().createUser("Compte","Test","az","az",Date.valueOf("2002-03-18"), Utilisateur.TypeUser.Gestionnaire);

        try {
            UtilisateurDAO utilisateurDAO = InitDAOS.getUtilisateurDAO();
            utilisateurDAO.persist(utilisateur);
            int id = utilisateurDAO.login(new Authentication("az", "az"));
            System.out.println(id);
            Utilisateur user2 = utilisateurDAO.findUtilisateur(id);
            System.out.println(user2);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

}

