package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Formation;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import fr.utln.atlas.projethyp.exceptions.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FormationDAO extends AbstractDAO<Formation>{
    private PreparedStatement findNombreGroupePS;
    public FormationDAO() throws DataAccessException {
        super("INSERT INTO FORMATION(NOMFORMATION) VALUES (?)",
                "UPDATE FORMATION SET NOMFORMATION=? WHERE ID=?");
        /*try {
            findNombreGroupePS = getConnection().prepareStatement("SELECT NOMBREGROUPE FROM FORMATION WHERE ID=?");
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }*/
    }

    @Override
    public String getTableName() {
        return "FORMATION";
    }

    @Override
    protected Formation fromResultSet(ResultSet resultSet) throws SQLException {
        return Formation.builder()
                .id(resultSet.getInt("ID"))
                .nomFormation(resultSet.getString("NOMFORMATION"))
                .build();
    }

    @Override
    public Formation persist(Formation formation) throws DataAccessException {
        return persist(formation.getNomFormation());
    }

    public Formation persist(String nomFormation) throws DataAccessException {
        try {
            persistPS.setString(1, nomFormation);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    @Override
    public void update(Formation formation) throws DataAccessException {
        try {
            updatePS.setString(1, formation.getNomFormation());
            updatePS.setInt(2, formation.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }

    /*public int findFormationNombreGroupe(int id) throws DataAccessException {
        int temp = -1;
        try {
            findNombreGroupePS.setInt(1, id);
            ResultSet rs = findNombreGroupePS.executeQuery();
            while(rs.next()) {
                temp = rs.getInt("NOMBREGROUPE");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        if (temp==-1) throw new NotFoundException();
        return temp;
    }*/
}
