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
        Utilisateur utilisateur = Utilisateur.builder()
                .id(7)
                .nom("Prout")
                .prenom("Lola")
                .mail("lola.prout@gmail.com")
                .typeUser(Utilisateur.TypeUser.Gestionnaire)
                .dateNaissance(Date.valueOf("2023-05-06"))
                .motDePasse("gestionnaire")
                .build();

        try {
            UtilisateurDAO utilisateurDAO = InitDAOS.getUtilisateurDAO();
            utilisateurDAO.persist(utilisateur);
            int id = utilisateurDAO.login(new Authentication("lola.prout@gmail.com", "gestionnaire"));
            System.out.println(id);
            Utilisateur user2 = utilisateurDAO.findUtilisateur(id);
            System.out.println(user2);
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

}

