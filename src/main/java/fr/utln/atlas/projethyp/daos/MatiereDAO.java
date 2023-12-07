package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Matiere;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import fr.utln.atlas.projethyp.exceptions.NotFoundException;
import lombok.extern.java.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Log
public class MatiereDAO extends AbstractDAO<Matiere>{

    private PreparedStatement findMatPS;
    public MatiereDAO() throws DataAccessException {
        super("INSERT INTO MATIERE(NOMMATIERE) VALUES (?)",
                "UPDATE MATIERE SET NOMMATIERE=? WHERE ID=?");
        try {
            findMatPS = getConnection().prepareStatement("SELECT NOMMATIERE FROM MATIERE WHERE ID=?");
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    /**
     * give the name of the table
     * @return The table name
     */
    @Override
    public String getTableName() {
        return "MATIERE";
    }

    /**
     * Initialize a Matiere object with data collected in resultSet
     * @param resultSet The data collected from the SQL query
     * @return The object Matiere initialized with data from resultSet
     * @throws SQLException If collecting data from resultSet throw an error
     */
    @Override
    protected Matiere fromResultSet(ResultSet resultSet) throws SQLException {
        return Matiere.builder()
                .id(resultSet.getInt("ID"))
                .nomMatiere(resultSet.getString("NOMMATIERE"))
                .build();
    }

    /**
     * Persist an entity Matiere in DB
     * @param matiere The entity to be persisted.
     * @return The entity persisted
     * @throws DataAccessException If unable to access data
     */
    @Override
    public Matiere persist(Matiere matiere) throws DataAccessException {
        return persist(matiere.getNomMatiere());
    }

    /**
     * Persist an entity Matiere in DB
     * @param nomMatiere The name of Matiere
     * @return The entity persisted
     * @throws DataAccessException If unable to access data
     */
    public Matiere persist(String nomMatiere) throws DataAccessException {
        try {
            persistPS.setString(1, nomMatiere);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    /**
     * Update the entity Matiere in DB
     * @param matiere The entity to be updated. The id is used and cannot be updated.
     * @throws DataAccessException If unable to access data
     */
    @Override
    public void update(Matiere matiere) throws DataAccessException {
        try {
            updatePS.setString(1, matiere.getNomMatiere());
            updatePS.setInt(2, matiere.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }

    /**
     * Search Matiere by id
     * @param id the id of Matiere
     * @return The name of Matiere found
     * @throws DataAccessException If unable to access data
     */
    public String findMatId(int id) throws DataAccessException {
        String temp = null;
        try {
            findMatPS.setInt(1, id);
            ResultSet rs = findMatPS.executeQuery();
            while(rs.next()) {
                temp = rs.getString("NOMMATIERE");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        if (temp==null) throw new NotFoundException();
        return temp;
    }
}
