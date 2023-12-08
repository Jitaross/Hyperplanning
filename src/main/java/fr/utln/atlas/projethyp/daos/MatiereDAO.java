package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Matiere;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import fr.utln.atlas.projethyp.exceptions.NotFoundException;
import lombok.extern.java.Log;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log
public class MatiereDAO extends AbstractDAO<Matiere>{

    private PreparedStatement findMatPS;
    private PreparedStatement findMatFormationPS;
    public MatiereDAO() throws DataAccessException {
        super("INSERT INTO MATIERE(NOMMATIERE,IDFORMATION) VALUES (?,?)",
                "UPDATE MATIERE SET NOMMATIERE=?, IDFORMATION=? WHERE ID=?");
        try {
            findMatPS = getConnection().prepareStatement("SELECT * FROM MATIERE WHERE ID=?");
            findMatFormationPS = getConnection().prepareStatement("SELECT * FROM MATIERE WHERE IDFORMATION=?");
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    @Override
    public String getTableName() {
        return "MATIERE";
    }

    @Override
    protected Matiere fromResultSet(ResultSet resultSet) throws SQLException {
        return Matiere.builder()
                .id(resultSet.getInt("ID"))
                .nomMatiere(resultSet.getString("NOMMATIERE"))
                .idFormation(resultSet.getInt("IDFORMATION"))
                .build();
    }

    @Override
    public Matiere persist(Matiere matiere) throws DataAccessException {
        return persist(matiere.getNomMatiere(),matiere.getIdFormation());
    }

    public Matiere persist(String nomMatiere,int idFormation) throws DataAccessException {
        try {
            persistPS.setString(1, nomMatiere);
            persistPS.setInt(2, idFormation);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    @Override
    public void update(Matiere matiere) throws DataAccessException {
        try {
            updatePS.setString(1, matiere.getNomMatiere());
            updatePS.setInt(2, matiere.getIdFormation());
            updatePS.setInt(3, matiere.getId());
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }

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

    public Page<Matiere> findMatFormation(int idFormation, int pageNumber, int pageSize) throws DataAccessException {
        List<Matiere> listeMatiere = new ArrayList<>();
        try {
            findMatFormationPS.setInt(1, idFormation);

            ResultSet resultSet = findMatFormationPS.executeQuery();

            while (resultSet.next()) {
                Matiere matiere = fromResultSet(resultSet);
                listeMatiere.add(matiere);
            }

            resultSet.close();
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, listeMatiere);
    }
}
