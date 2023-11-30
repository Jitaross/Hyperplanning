package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Salle;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO extends AbstractDAO<Salle>{

    private final PreparedStatement notTakenPS;
    protected SalleDAO(String persistPS, String updatePS) throws DataAccessException {
        super("INSERT INTO SALLE(ID, NOMSALLE) VALUE (?, ?)",
                "UPDATE SALLE SET NOMSALLE=? WHERE ID=?");
        try{
            notTakenPS = getConnection().prepareStatement("SELECT DISTINCT ID, NOMSALLE FROM SALLE AS s, COURS AS cs " +
                    "WHERE cs.IDSALLE = s.ID " +
                    "AND NOT cs.DEBUT=? " +
                    "AND TIMESTAMPDIFF(cs.FIN, cs.DEBUT, MINUTE()) <= 0");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getTableName() {
        return "SALLE";
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

    public Page<Salle> findNotTakenRoom(Timestamp date, int pageNumber, int pageSize) throws  DataAccessException {
        List<Salle> salleList = new ArrayList<>();
        try {
            notTakenPS.setTimestamp(1, date);
            ResultSet resultSet = notTakenPS.executeQuery();
            while (resultSet.next()) salleList.add(fromResultSet(resultSet));
        }catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, salleList);
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