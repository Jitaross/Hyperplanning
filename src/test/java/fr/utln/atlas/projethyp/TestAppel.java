package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.daos.UtilisateurDAO;
import fr.utln.atlas.projethyp.entities.Appel;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAppel {
    @DisplayName("Test de l'appel")
    @Test
    public void testAppel() throws DataAccessException {
        List<Etudiant> etudiantList = new ArrayList<>(5);
        etudiantList.add(Etudiant.builder()
                .id(54)
                .dateNaissance(Date.valueOf("1990-10-12"))
                .nom("Michel")
                .mail("michel.michel@gmail.com")
                .prenom("Michel")
                .idFormation(1)
                .nomFormation("Informatique")
                .motDePasse("motdepassemichel")
                .typeUser(Utilisateur.TypeUser.Etudiant)
                .build());
        etudiantList.add(Etudiant.builder()
                .id(55)
                .dateNaissance(Date.valueOf("1960-10-18"))
                .nom("Vandamme")
                .mail("jeanclaude.vandamme@gmail.com")
                .prenom("Jean-Claude")
                .idFormation(1)
                .nomFormation("Informatique")
                .motDePasse("jcvd")
                .typeUser(Utilisateur.TypeUser.Etudiant)
                .build());
        etudiantList.add(Etudiant.builder()
                .id(56)
                .dateNaissance(Date.valueOf("1984-12-03"))
                .nom("Romanoff")
                .mail("n.romanoff.com")
                .prenom("Natasha")
                .idFormation(1)
                .nomFormation("Informatique")
                .motDePasse("nromanoff13")
                .typeUser(Utilisateur.TypeUser.Etudiant)
                .build());
        etudiantList.add(Etudiant.builder()
                .id(57)
                .dateNaissance(Date.valueOf("1920-07-04"))
                .nom("Rogers")
                .mail("steve.rogers@gmail.com")
                .prenom("Steve")
                .idFormation(1)
                .nomFormation("Informatique")
                .motDePasse("steverogers1920")
                .typeUser(Utilisateur.TypeUser.Etudiant)
                .build());
        etudiantList.add(Etudiant.builder()
                .id(58)
                .dateNaissance(Date.valueOf("1970-05-27"))
                .nom("Stark")
                .mail("tony.stark@gmail.com")
                .prenom("Anthony")
                .idFormation(1)
                .nomFormation("Informatique")
                .motDePasse("jdaT7S?44toYKLX3kGamet4t!&a&$oq#@xYK3bk#")
                .typeUser(Utilisateur.TypeUser.Etudiant)
                .build());

        Appel appel = new Appel(65, etudiantList);
        for (Etudiant etudiant : etudiantList) {
            appel.addEtudiantAbsent(etudiant);
        }
        assertNotNull(appel);
        System.out.println(appel.getEtudiantAbsent());

        appel.removeEtudiantAbsent(etudiantList.get(4));
        appel.removeEtudiantAbsent(etudiantList.get(0));

        System.out.println(appel.getEtudiantAbsent());

        UtilisateurDAO utilisateurDAO = InitDAOS.getUtilisateurDAO();

        for(Etudiant etudiant : etudiantList) {
            Etudiant temp = utilisateurDAO.createEtudiant(etudiant.getNom(), etudiant.getPrenom(),
                    etudiant.getMail(), etudiant.getMotDePasse(), etudiant.getDateNaissance(), etudiant.getIdFormation());

            etudiantList.get(etudiantList.indexOf(etudiant)).setId(temp.getId());

        }

        appel.etudiantAbsentConfirme();

    }
}
