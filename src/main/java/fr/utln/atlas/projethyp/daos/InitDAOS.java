package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.exceptions.DataAccessException;

public class InitDAOS {
    private static AbsenceDAO absenceDAO;
    private static CoursDAO coursDAO;
    private static DevoirDAO devoirDAO;
    private static EnseignantDAO enseignantDAO;
    private static EtudiantDAO etudiantDAO;
    private static MatiereDAO matiereDAO;
    private static SalleDAO salleDAO;
    private static UtilisateurDAO utilisateurDAO;

    static {
        try {
            initializeDaos();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void initializeDaos() throws DataAccessException {
        utilisateurDAO = new UtilisateurDAO();
        absenceDAO = new AbsenceDAO();
        coursDAO = new CoursDAO();
        devoirDAO = new DevoirDAO();
        enseignantDAO = new EnseignantDAO();
        etudiantDAO = new EtudiantDAO();
        matiereDAO = new MatiereDAO();
        //salleDAO = new SalleDAO();

    }

    public static AbsenceDAO getAbsenceDAO() {
        return absenceDAO;
    }

    public static CoursDAO getCoursDAO() {
        return coursDAO;
    }

    public static DevoirDAO getDevoirDAO() {
        return devoirDAO;
    }

    public static EnseignantDAO getEnseignantDAO() {
        return enseignantDAO;
    }

    public static EtudiantDAO getEtudiantDAO() {
        return etudiantDAO;
    }

    public static MatiereDAO getMatiereDAO() {
        return matiereDAO;
    }

    public static SalleDAO getSalleDAO() {
        return salleDAO;
    }

    public static UtilisateurDAO getUtilisateurDAO() {
        return utilisateurDAO;
    }

    public static void closeAll() throws DataAccessException {
        absenceDAO.close();
        coursDAO.close();
        devoirDAO.close();
        enseignantDAO.close();
        etudiantDAO.close();
        matiereDAO.close();
        //salleDAO.close();
        utilisateurDAO.close();
    }
}
