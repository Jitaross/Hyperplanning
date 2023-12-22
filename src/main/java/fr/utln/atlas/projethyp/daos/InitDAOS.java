package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.Getter;
import lombok.extern.java.Log;

@Log
public class InitDAOS {
    @Getter
    private static AbsenceDAO absenceDAO;
    @Getter
    private static CoursDAO coursDAO;
    @Getter
    private static DevoirDAO devoirDAO;
    @Getter
    private static EnseignantDAO enseignantDAO;
    @Getter
    private static EtudiantDAO etudiantDAO;
    @Getter
    private static MatiereDAO matiereDAO;
    @Getter
    private static SalleDAO salleDAO;
    @Getter
    private static UtilisateurDAO utilisateurDAO;

    static {
        try {
            initializeDaos();
        } catch (DataAccessException e) {
            try {
                throw new DataAccessException(e.getLocalizedMessage());
            } catch (DataAccessException ex) {
                throw new RuntimeException(ex);
            }
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
        salleDAO = new SalleDAO();

    }

    public static void closeAll() throws DataAccessException {
        absenceDAO.close();
        coursDAO.close();
        devoirDAO.close();
        enseignantDAO.close();
        etudiantDAO.close();
        matiereDAO.close();
        salleDAO.close();
        utilisateurDAO.close();
        log.info("All DAOS closed.");
    }
}
