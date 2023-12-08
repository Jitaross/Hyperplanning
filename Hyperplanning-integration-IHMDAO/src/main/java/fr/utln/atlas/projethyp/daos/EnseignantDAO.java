package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Enseignant;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;


import java.sql.*;


@Log
public class EnseignantDAO extends AbstractDAO<Enseignant> {

    public EnseignantDAO() throws DataAccessException {
        super("INSERT INTO ENSEIGNANT(ID,UFR) VALUES (?,?)",
                "UPDATE ENSEIGNANT SET UFR=? WHERE ID=?");

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
    @Override
    public String getTableName() {
        return "ENSEIGNANT";
    }
}