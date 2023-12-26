package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.entities.Enseignant;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import lombok.extern.java.Log;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Log
public class EnseignantDAO extends AbstractDAO<Enseignant> {

    private PreparedStatement findAllCoursIdDayPS;

    public EnseignantDAO() throws DataAccessException {
        super("INSERT INTO ENSEIGNANT(ID,UFR) VALUES (?,?)",
                "UPDATE ENSEIGNANT SET UFR=? WHERE ID=?");
        try {
            findAllCoursIdDayPS = getConnection().prepareStatement("SELECT c.* FROM ENSEIGNANT as e, COURS as c WHERE e.ID=?" +
                    "AND c.IDENSEIGNANT=e.ID " +
                    "AND c.DATE=?");
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }

    }


    @Override
    protected Enseignant fromResultSet(ResultSet resultSet) throws SQLException {
        return Enseignant.builder()
                .UFR(resultSet.getString("UFR"))
                .build();
    }

    @Override
    public Enseignant persist(Enseignant enseignant) throws DataAccessException {
        return persist(enseignant.getId(),enseignant.getUFR());
    }

    public Enseignant persist(int id, String ufr ) throws DataAccessException {
        try {
            persistPS.setInt(1, id);
            persistPS.setString(2, ufr);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Enseignant enseignant) throws DataAccessException {
        try {
            updatePS.setString(1, enseignant.getUFR());
            updatePS.setInt(2,enseignant.getId());

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }

    public Devoir createDevoir(Devoir devoir) throws DataAccessException {
        DevoirDAO devoirDAO = InitDAOS.getDevoirDAO();
        return devoirDAO.persist(devoir);

    }

    public void updateDevoir(Devoir devoir) throws DataAccessException {
        DevoirDAO devoirDAO = InitDAOS.getDevoirDAO();
        devoirDAO.update(devoir);

    }

    public List<Cours> findAllCoursIdDay(int idEnseignant, Date dateCours) throws DataAccessException{
        List<Cours> cours = new ArrayList<>();
        CoursDAO coursDAO = InitDAOS.getCoursDAO();
        try {
            findAllCoursIdDayPS.setInt(1, idEnseignant);
            findAllCoursIdDayPS.setDate(2, dateCours);
            ResultSet rs = findAllCoursIdDayPS.executeQuery();
            while(rs.next()) cours.add(coursDAO.fromResultSet(rs));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return cours;
    }

    @Override
    public String getTableName() {
        return "ENSEIGNANT";
    }
}