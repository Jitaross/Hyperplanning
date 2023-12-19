package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.exceptions.DataAccessException;

public class InitDAOS {
    public static AbsenceDAO absenceDAO;
    public static CoursDAO coursDAO;
    public static DevoirDAO devoirDAO;
    public static EnseignantDAO enseignantDAO;
    public static EtudiantDAO etudiantDAO;
    public static MatiereDAO matiereDAO;
    public static SalleDAO salleDAO;
    public static UtilisateurDAO utilisateurDAO;

    public InitDAOS() throws DataAccessException {
        absenceDAO = new AbsenceDAO();
        coursDAO = new CoursDAO();
        devoirDAO = new DevoirDAO();
        enseignantDAO = new EnseignantDAO();
        etudiantDAO = new EtudiantDAO();
        matiereDAO = new MatiereDAO();
        //salleDAO = new SalleDAO();
        utilisateurDAO = new UtilisateurDAO();
    }
}
