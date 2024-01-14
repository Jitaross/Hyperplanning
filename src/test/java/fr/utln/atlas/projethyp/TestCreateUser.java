package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.entities.Enseignant;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.Test;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestCreateUser {

    @Test
    public void createUserTest() throws DataAccessException {
        try {
            UtilisateurDAO utilisateurDAO = InitDAOS.getUtilisateurDAO();
            Utilisateur utilisateur = Utilisateur.builder()
                    .nom("Potter")
                    .prenom("Harry")
                    .mail("harry.potter@gmail.com")
                    .motDePasse("jesaispastrop")
                    .dateNaissance(Date.valueOf("1980-07-31"))
                    .build();

            Utilisateur utilisateur1 = Utilisateur.builder()
                    .nom("Londubat")
                    .prenom("Neville")
                    .mail("neville.londubat@gmail.com")
                    .motDePasse("testdiff")
                    .dateNaissance(Date.valueOf("1980-07-31"))
                    .build();
            assertNotNull(utilisateur);
            assertNotNull(utilisateur1);

            String ufr = "Magie numérique";

            Enseignant enseignant = utilisateurDAO.createEnseignant(utilisateur.getNom(), utilisateur.getPrenom(),
                    utilisateur.getMail(), utilisateur.getMotDePasse(), utilisateur.getDateNaissance(), ufr);
            assertNotNull(enseignant);
            System.out.println(enseignant);

            Etudiant etudiant = utilisateurDAO.createEtudiant(utilisateur1.getNom(), utilisateur1.getPrenom(),
                    utilisateur1.getMail(), utilisateur1.getMotDePasse(), utilisateur1.getDateNaissance(), 1);

            assertNotNull(etudiant);
            System.out.println(etudiant);

            int modify = utilisateurDAO.modifyMDP("harry.potter@gmail.com", "testbidon",
                    "jesaistoujourspasenfait");

            assertEquals(-1, modify);

            System.out.println(modify);


            int modify1 = utilisateurDAO.modifyMDP("harry.potter@gmail.com", "jesaispastrop",
                    "jesaistoujourspasenfait");

            assertNotEquals(-1, modify1);

            System.out.println(modify1);
        } catch (DataAccessException e){
            throw new DataAccessException(e.getLocalizedMessage());
        }


    }
}
