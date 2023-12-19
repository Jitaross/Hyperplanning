package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Salle;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO extends AbstractDAO<Salle>{

    private final PreparedStatement notTakenPS;
    public SalleDAO() throws DataAccessException {
        super("INSERT INTO SALLE(NOMSALLE, NOMBREPLACE) VALUES (?, ?)",
                "UPDATE SALLE SET NOMSALLE=?, NOMBREPLACE=? WHERE ID=?");
        try{
            notTakenPS = getConnection().prepareStatement("SELECT DISTINCT s.ID, NOMSALLE FROM SALLE AS s, COURS AS cs " +
                    "WHERE cs.IDSALLE = s.ID " +
                    "AND cs.DATE=?" +
                    "AND NOT cs.DEBUT=? " +
                    "AND cs.FIN - cs.DEBUT < '00:00:00'" +
                    "LIMIT ? OFFSET ?");
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
                .nombrePlace(resultSet.getInt("NOMBREPLACE"))
                .build();
    }

    @Override
    public Salle persist(Salle salle) throws DataAccessException {
        return persist(salle.getNomSalle(), salle.getNombrePlace());
    }

    private Salle persist(String nomSalle, int nombreSalle) throws DataAccessException {
        try {
            persistPS.setString(1, nomSalle);
            persistPS.setInt(2, nombreSalle);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    public Page<Salle> findNotTakenRoom(Date date, Time deb, int pageNumber, int pageSize) throws  DataAccessException {
        List<Salle> salleList = new ArrayList<>();
        try {
            notTakenPS.setDate(1, date);
            notTakenPS.setTime(2, deb);
            notTakenPS.setInt(3, pageSize);
            notTakenPS.setInt(4, (pageNumber - 1) * pageSize);
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
            updatePS.setInt(2, salle.getNombrePlace());
            updatePS.setInt(3, salle.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }
}