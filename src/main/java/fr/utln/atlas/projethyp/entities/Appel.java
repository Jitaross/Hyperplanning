package fr.utln.atlas.projethyp.entities;


import fr.utln.atlas.projethyp.daos.AbsenceDAO;
import fr.utln.atlas.projethyp.daos.InitDAOS;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Appel {
    private int idCours;
    private List<Etudiant> etudiantList;
    private List<Etudiant> etudiantAbsent;

    public Appel(int idCours, List<Etudiant> etudiantList) {
        this.idCours = idCours;
        this.etudiantList = etudiantList;
        this.etudiantAbsent = new ArrayList<>();
    }

    public void addEtudiantAbsent(Etudiant etudiant) {
        etudiantAbsent.add(etudiant);
    }

    public void removeEtudiantAbsent(Etudiant etudiant) {
        etudiantAbsent.remove(etudiant);
    }

    public void etudiantAbsentConfirme() throws DataAccessException {
        AbsenceDAO absenceDAO = InitDAOS.getAbsenceDAO();
        for (Etudiant etudiant : etudiantAbsent) {
            Absence absence = Absence.builder()
                    .idEtudiant(etudiant.getId())
                    .idCours(getIdCours())
                    .build();
            absenceDAO.persist(absence);
        }
    }

}
