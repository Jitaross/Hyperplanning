package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;


import java.sql.*;

@Log
public class CoursDAO extends AbstractDAO<Cours> {

    public CoursDAO() throws DataAccessException {
        super("INSERT INTO COURS(NOMMATIERE,DESCRIPTION,IDENSEIGNANT, IDSALLE,DEBUT,FIN) VALUES (?,?,?,?,?,?)",
                "UPDATE COURS SET NOMMATIERE=?, DESCRIPTION=?, IDENSEIGNANT=?, IDSALLE=?, DEBUT=?, FIN=? WHERE ID=?");
    }

    @Override
    protected Cours fromResultSet(ResultSet resultSet) throws SQLException {
        return Cours.builder()
                .id(resultSet.getInt("ID"))
                .nomMatiere(resultSet.getString("NOMMATIERE"))
                .description(resultSet.getString("DESCRIPTION"))
                .idEnseignant(resultSet.getInt("IDENSEIGNANT"))
                .idsalle(resultSet.getLong("IDSALLE"))
                .debut(resultSet.getTimestamp("DEBUT"))
                .fin(resultSet.getTimestamp("FIN"))
                .build();
    }

    @Override
    public Cours persist(Cours cours) throws DataAccessException {
        return persist(cours.getNomMatiere(), cours.getDescription(),cours.getIdEnseignant(),
                cours.getIdsalle(), cours.getDebut(),cours.getFin());
    }

    private Cours persist(String nomMatiere, String description, long idEnseignant,
                          long idSalle, Timestamp debut, Timestamp fin) throws DataAccessException {
        try {
            persistPS.setString(1, nomMatiere);
            persistPS.setString(2, description);
            persistPS.setLong(3, idEnseignant);
            persistPS.setLong(4, idSalle);
            persistPS.setTimestamp(5,debut);
            persistPS.setTimestamp(6,fin);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Cours cours) throws DataAccessException {
        try {
            updatePS.setString(1, cours.getNomMatiere());
            updatePS.setString(2, cours.getDescription());
            updatePS.setLong(3, cours.getIdEnseignant());
            updatePS.setLong(4, cours.getIdsalle());
            updatePS.setTimestamp(5, cours.getDebut());
            updatePS.setTimestamp(6, cours.getFin());
            updatePS.setLong(7, cours.getId());
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }

    @Override
    public String getTableName() {
        return "COURS";
    }


}
