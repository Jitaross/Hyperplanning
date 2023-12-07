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

    /**
     * Initialize a Enseignant object with data collected in resultSet
     * @param resultSet The data collected from the SQL query
     * @return The object Enseignant initialized with data from resultSet
     * @throws SQLException If collecting data from resultSet throw an error
     */
    @Override
    protected Enseignant fromResultSet(ResultSet resultSet) throws SQLException {
        return Enseignant.builder()
                .UFR(resultSet.getString("UFR"))
                .build();
    }

    /**
     * Persist an entity Enseignant in DB
     * @param enseignant The entity to be persisted.
     * @return the entity persisted
     * @throws DataAccessException If unable to access data
     */
    @Override
    public Enseignant persist(Enseignant enseignant) throws DataAccessException {
        return persist(enseignant.getId(),enseignant.getUFR());
    }

    /**
     * Persist an entity Enseignant in DB
     * @param id The Enseignant id
     * @param ufr The ufr where Enseignant works
     * @return The entity persisted
     * @throws DataAccessException If unable to access data
     */
    public Enseignant persist(int id, String ufr ) throws DataAccessException {
        try {
            persistPS.setInt(1, id);
            persistPS.setString(2, ufr);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }

    /**
     * Update an entity Enseignant in dB
     * @param enseignant The entity to be updated. The id is used and cannot be updated.
     * @throws DataAccessException If unable to access data
     */
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

    /**
     * Give the name of the table
     * @return The table name
     */
    @Override
    public String getTableName() {
        return "ENSEIGNANT";
    }
}