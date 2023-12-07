package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Salle;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO extends AbstractDAO<Salle>{

    private final PreparedStatement notTakenPS;
    protected SalleDAO(String persistPS, String updatePS) throws DataAccessException {
        super("INSERT INTO SALLE(ID, NOMSALLE, NOMBREPLACE) VALUE (?, ?, ?)",
                "UPDATE SALLE SET NOMSALLE=?, NOMNREPLACE=? WHERE ID=?");
        try{
            notTakenPS = getConnection().prepareStatement("SELECT DISTINCT s.ID, NOMSALLE FROM SALLE AS s, COURS AS cs " +
                    "WHERE cs.IDSALLE = s.ID " +
                    "AND cs.DATE=?" +
                    "AND NOT cs.DEBUT=? " +
                    "AND cs.FIN - cs.DEBUT < 0");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    /**
     * Give the name of the table
     * @return The table name
     */
    @Override
    public String getTableName() {
        return "SALLE";
    }

    /**
     * Initialize a Salle object with data collected in resultSet
     * @param resultSet The data collected from the SQL query
     * @return The object Salle initialized with data from resultSet
     * @throws SQLException If collecting data from resultSet throw an error
     */
    @Override
    protected Salle fromResultSet(ResultSet resultSet) throws SQLException {
        return Salle.builder()
                .id(resultSet.getInt("ID"))
                .nomSalle(resultSet.getString("NOMSALLE"))
                .nombrePlace(resultSet.getInt("NOMBREPLACE"))
                .build();
    }

    /**
     * Persist the salle entity in DB
     * @param salle The entity to be persisted.
     * @return The entity persisted
     * @throws DataAccessException If unable to find data
     * */
    @Override
    public Salle persist(Salle salle) throws DataAccessException {
        return persist(salle.getNomSalle(), salle.getNombrePlace());
    }

    /**
     * Persist the salle entity in DB with all parameters given
     * @param nomSalle The name of salle
     * @param nombreSalle The Capacity of salle
     * @return the entity persisted
     * @throws DataAccessException If unable to find data
     */
    private Salle persist(String nomSalle, int nombreSalle) throws DataAccessException {
        try {
            persistPS.setString(1, nomSalle);
            persistPS.setInt(2, nombreSalle);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    /**
     * Search for free romms in a day
     * @param date The date to search which rooms are available
     * @param deb The time when the lesson starts
     * @param pageNumber Number of page of Salle returned
     * @param pageSize The amount of Salle by page
     * @return A page of Salle
     * @throws DataAccessException If unable to find data
     */
    public Page<Salle> findNotTakenRoom(Date date, Time deb, int pageNumber, int pageSize) throws  DataAccessException {
        List<Salle> salleList = new ArrayList<>();
        try {
            notTakenPS.setDate(1, date);
            notTakenPS.setTime(2, deb);
            ResultSet resultSet = notTakenPS.executeQuery();
            while (resultSet.next()) salleList.add(fromResultSet(resultSet));
        }catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, salleList);
    }

    /**
     * Update the entity salle in DB
     * @param salle The entity to be updated. The id is used and cannot be updated.
     * @throws DataAccessException If unalble to find data
     */
    @Override
    public void update(Salle salle) throws DataAccessException {
        try {
            updatePS.setString(1, salle.getNomSalle());
            updatePS.setInt(2, salle.getNombrePlace());
            updatePS.setInt(3, salle.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }
}