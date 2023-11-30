package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Salle;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SalleDAO extends AbstractDAO<Salle>{
    protected SalleDAO(String persistPS, String updatePS) throws DataAccessException {
        super("INSERT INTO Salle(ID, NOMSALLE) VALUE (?, ?)",
                "UPDATE Salle SET NOMSALLE=? WHERE ID=?");
    }

    @Override
    public String getTableName() {
        return "Salle";
    }

    @Override
    protected Salle fromResultSet(ResultSet resultSet) throws SQLException {
        return Salle.builder()
                .id(resultSet.getInt("ID"))
                .nomSalle(resultSet.getString("NOMSALLE"))
                .build();
    }

    @Override
    public Salle persist(Salle salle) throws DataAccessException {
        return persist(salle.getId(), salle.getNomSalle());
    }

    private Salle persist(long id, String nomSalle) throws DataAccessException {
        try {
            persistPS.setLong(1, id);
            persistPS.setString(2, nomSalle);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    @Override
    public void update(Salle salle) throws DataAccessException {
        try {
            updatePS.setString(1, salle.getNomSalle());
            persistPS.setLong(2, salle.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }
}
