package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Devoir;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DevoirDAO extends AbstractDAO<Devoir>{
    private PreparedStatement findAllNotesUser;
    public DevoirDAO() throws DataAccessException {
        super("INSERT INTO DEVOIR(NOTE, COMMENTAIRE, TYPEDEVOIR, IDUSER) VALUES (?, ?, ?, ?)",
                "UPDATE DEVOIR SET NOTE=?, COMMENTAIRE=?, TYPEDEVOIR=?, IDUSER=? WHERE ID=?");
        try {
            findAllNotesUser = getConnection().prepareStatement("SELECT d.* FROM DEVOIR as d, UTILISATEUR as u WHERE d.IDUSER=?" +
                    "AND d.IDUSER=u.ID LIMIT ? OFFSET ?");
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
                .note(resultSet.getInt("NOTE"))
                .commentaire(resultSet.getString("COMMENTAIRE"))
                .typeDevoir(Devoir.TypeDevoir.valueOf(resultSet.getString("TYPEDEVOIR")))
                .build();
    }
    /**
     * @param devoir The entity to be persisted. 
     * @return
     * @throws DataAccessException
     */
    @Override
    public Devoir persist(Devoir devoir) throws DataAccessException {
        return persist(devoir.getNote(), devoir.getCommentaire(), devoir.getTypeDevoir(), devoir.getIdUtilisateur());
    }

    public Devoir persist(int note, String commentaire, Devoir.TypeDevoir typeDevoir, int idUser) throws DataAccessException {
        try {
            persistPS.setInt(1, note);
            persistPS.setString(2, commentaire);
            persistPS.setString(3, String.valueOf(typeDevoir));
            persistPS.setInt(4, idUser);
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
            updatePS.setInt(1, devoir.getNote());
            updatePS.setString(2, devoir.getCommentaire());
            updatePS.setString(3, String.valueOf(devoir.getTypeDevoir()));
            updatePS.setInt(4, devoir.getIdUtilisateur());
            updatePS.setInt(5, devoir.getId());

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
}
