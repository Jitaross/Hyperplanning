package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;

@Log
public class CoursDAO extends AbstractDAO<Cours> {

    public CoursDAO() throws DataAccessException {
        super("INSERT INTO COURS(NOMMATIERE,DESCRIPTION,IDENSEIGNANT,DEBUT,FIN) VALUES (?,?,?,?,?)",
                "UPDATE COURS SET NOMMATIERE=?, DESCRIPTION=?, IDENSEIGNANT=?, DEBUT=?, FIN=? WHERE ID=?");
    }

    @Override
    protected Cours fromResultSet(ResultSet resultSet) throws SQLException {
        return Cours.builder()
                .id(resultSet.getInt("ID"))
                .nomMatiere(resultSet.getString("NOMMATIERE"))
                .description(resultSet.getString("DESCRIPTION"))
                .idEnseignant(resultSet.getInt("IDENSEIGNANT"))
                .debut(resultSet.getTime("DEBUT"))
                .fin(resultSet.getTime("FIN"))
                .date(resultSet.getDate("DATE"))
                .build();
    }

    @Override
    public Cours persist(Cours cours) throws DataAccessException {
        return persist(cours.getNomMatiere(), cours.getDescription(),cours.getIdEnseignant(),cours.getDebut(),cours.getFin(), cours.getDate());
    }

    private Cours persist(String nomMatiere, String description, long idEnseignant, Time debut, Time fin, Date date ) throws DataAccessException {
        try {
            persistPS.setString(1, nomMatiere);
            persistPS.setString(2, description);
            persistPS.setLong(3, idEnseignant);
            persistPS.setTime(4,debut);
            persistPS.setTime(5,fin);
            persistPS.setDate(6,date);
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
            updatePS.setTime(4, cours.getDebut());
            updatePS.setTime(5, cours.getFin());
            updatePS.setDate(6,cours.getDate());
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
