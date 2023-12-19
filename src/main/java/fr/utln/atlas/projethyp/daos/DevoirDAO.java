package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DevoirDAO extends AbstractDAO<Devoir>{
    private PreparedStatement findAllNotesUser;
    private PreparedStatement findAllMoyennesUser;
    public DevoirDAO() throws DataAccessException {
        super("INSERT INTO DEVOIR(NOTE, COMMENTAIRE, TYPEDEVOIR, IDUSER, IDMATIERE) VALUES (?, ?, ?, ?, ?)",
                "UPDATE DEVOIR SET NOTE=?, COMMENTAIRE=?, TYPEDEVOIR=?, IDUSER=?, IDMATIERE=? WHERE ID=?");
        try {
            findAllNotesUser = getConnection().prepareStatement("SELECT d.*, m.* FROM DEVOIR as d, UTILISATEUR as u," +
                    " MATIERE as m WHERE d.IDUSER=?" +
                    "AND d.IDUSER=u.ID " +
                    "AND d.IDMATIERE=m.ID LIMIT ? OFFSET ?");
            findAllMoyennesUser = getConnection().prepareStatement("SELECT m.NOMMATIERE, AVG(d.NOTE) AS MOYENNE FROM DEVOIR as d, MATIERE as m" +
                    " WHERE IDUSER = ? " +
                    "AND d.IDMATIERE=m.ID GROUP BY IDMATIERE");
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    /**
     * @return 
     */
    @Override
    public String getTableName() {
        return "DEVOIR";
    }

    /**
     * @param resultSet 
     * @return
     * @throws SQLException
     */
    @Override
    protected Devoir fromResultSet(ResultSet resultSet) throws SQLException {
        return Devoir.builder()
                .id(resultSet.getInt("ID"))
                .idUtilisateur(resultSet.getInt("IDUSER"))
                .note(resultSet.getDouble("NOTE"))
                .commentaire(resultSet.getString("COMMENTAIRE"))
                .typeDevoir(Devoir.TypeDevoir.valueOf(resultSet.getString("TYPEDEVOIR")))
                .idMatiere(resultSet.getInt("IDMATIERE"))
                .build();
    }
    /**
     * @param devoir The entity to be persisted. 
     * @return
     * @throws DataAccessException
     */
    @Override
    public Devoir persist(Devoir devoir) throws DataAccessException {
        return persist(devoir.getNote(), devoir.getCommentaire(), devoir.getTypeDevoir(), devoir.getIdUtilisateur(), devoir.getIdMatiere());
    }

    public Devoir persist(Double note, String commentaire, Devoir.TypeDevoir typeDevoir, int idUser, int idMatiere) throws DataAccessException {
        try {
            persistPS.setDouble(1, note);
            persistPS.setString(2, commentaire);
            persistPS.setString(3, String.valueOf(typeDevoir));
            persistPS.setInt(4, idUser);
            persistPS.setInt(5, idMatiere);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return super.persist();
    }

    /**
     * @param devoir The entity to be updated. The id is used and cannot be updated. 
     * @throws DataAccessException
     */
    @Override
    public void update(Devoir devoir) throws DataAccessException {
        try {
            updatePS.setDouble(1, devoir.getNote());
            updatePS.setString(2, devoir.getCommentaire());
            updatePS.setString(3, String.valueOf(devoir.getTypeDevoir()));
            updatePS.setInt(4, devoir.getIdUtilisateur());
            updatePS.setInt(5, devoir.getIdMatiere());
            updatePS.setInt(6, devoir.getId());

        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        super.update();
    }

    public Page<Devoir> findNotesUser(int pageSize, int pageNumber, int idUser) throws DataAccessException {
        List<Devoir> devoirs = new ArrayList<>();
        try {
            findAllNotesUser.setInt(1, idUser);
            findAllNotesUser.setInt(2, pageSize);
            findAllNotesUser.setInt(3, (pageNumber - 1) * pageSize);
            ResultSet resultSet = findAllNotesUser.executeQuery();
            while (resultSet.next()) devoirs.add(fromResultSet(resultSet));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, devoirs);
    }

    public Map<String,Double> findMoyennesUser(int iduser) throws DataAccessException {
        Map<String,Double> moyennes = new HashMap<>();
        try{
            findAllMoyennesUser.setInt(1,iduser);
            ResultSet resultSet = findAllMoyennesUser.executeQuery();
            while (resultSet.next()) moyennes.put(resultSet.getString("NOMMATIERE"), resultSet.getDouble("MOYENNE"));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return moyennes;
    }
}
