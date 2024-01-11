package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.daos.CoursDAO;
import fr.utln.atlas.projethyp.daos.EnseignantDAO;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestFinds {
    @Test
    public void testsFinds() throws DataAccessException {
        CoursDAO coursDAO = InitDAOS.getCoursDAO();
        EnseignantDAO enseignantDAO = InitDAOS.getEnseignantDAO();
        Date date = Date.valueOf("2023-12-04");
        List<Etudiant> etudiants = coursDAO.findAllEtudiant(97);
        assertNotNull(etudiants);
        for (Etudiant etudiant : etudiants) System.out.println(etudiant);
        List<Cours> cours = enseignantDAO.findAllCoursIdDay(97, date);
        assertNotNull(cours);
        for(Cours cours1 : cours) System.out.println(cours1);
    }
}
