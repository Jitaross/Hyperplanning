package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Matiere;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import fr.utln.atlas.projethyp.exceptions.NotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MatiereDAO extends AbstractDAO<Matiere>{

    private PreparedStatement findMatPS;
    protected MatiereDAO(String persistPS, String updatePS) throws DataAccessException {
        super("INSERT INTO MATIERE(NOMMATIERE) VALUE (?)",
                "UPDATE MATIERE SET NOMMATIERE=? WHERE ID=?");
        try {
            findMatPS = getConnection().prepareStatement("SELECT NOMMATIERE FROM MATIERE WHERE ID=?");
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
                .build();
    }

    @Override
    public Matiere persist(Matiere matiere) throws DataAccessException {
        return persist(matiere.getNomMatiere());
    }

    public Matiere persist(String nomMatiere) throws DataAccessException {
        try {
            persistPS.setString(1, nomMatiere);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

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

    public String findMatId(int id) throws DataAccessException {
        Matiere temp;
        try {
            findMatPS.setInt(1, id);
            ResultSet rs = findMatPS.executeQuery();
            temp = fromResultSet(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        if (temp==null) throw new NotFoundException();
        return temp.getNomMatiere();
    }
}
