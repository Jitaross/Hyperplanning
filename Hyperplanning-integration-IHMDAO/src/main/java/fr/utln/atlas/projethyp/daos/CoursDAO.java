package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.DateSemaine;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;
import java.sql.Date;
import java.sql.PreparedStatement;

import static fr.utln.atlas.projethyp.datasources.DBCPDataSource.getConnection;
import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

@Log
public class CoursDAO extends AbstractDAO<Cours> {
    private final PreparedStatement findCoursPS;
    private final PreparedStatement findCoursEtudiantPS;

    public CoursDAO() throws DataAccessException {
        super("INSERT INTO COURS(DESCRIPTION,IDENSEIGNANT,IDMATIERE,IDSALLE,DEBUT,FIN,DATE,TYPECOURS) VALUES (?,?,?,?,?,?,?,?)",
                "UPDATE COURS SET DESCRIPTION=?, IDENSEIGNANT=?, IDMATIERE=?, IDSALLE=?, DEBUT=?, FIN=?, DATE=?, TYPECOURS=? WHERE ID=?");

        try{
            findCoursPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ?");
            findCoursEtudiantPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ? AND IDMATIERE IN (SELECT ID FROM MATIERE WHERE IDFORMATION = (SELECT IDFORMATION FROM ETUDIANT WHERE ID =?))");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }


    @Override
    protected Cours fromResultSet(ResultSet resultSet) throws SQLException {
        return Cours.builder()
                .id(resultSet.getInt("ID"))
                .description(resultSet.getString("DESCRIPTION"))
                .idEnseignant(resultSet.getInt("IDENSEIGNANT"))
                .idMatiere(resultSet.getInt("IDMATIERE"))
                .idMatiere(resultSet.getInt("IDSALLE"))
                .debut(resultSet.getTime("DEBUT"))
                .fin(resultSet.getTime("FIN"))
                .date(resultSet.getDate("DATE"))
                .typeCours(Cours.TypeCours.fromValue(resultSet.getInt("TYPECOURS")))
                .build();
    }

    @Override
    public Cours persist(Cours cours) throws DataAccessException {
        return persist(cours.getDescription(),cours.getIdEnseignant(),cours.getIdMatiere(),cours.getIdSalle(),cours.getDebut(),cours.getFin(), cours.getDate(), cours.getTypeCours().getValue());
    }

    public Cours persist(String description, int idEnseignant, int idMatiere, int idSalle, Time debut, Time fin, Date date, int typeCours ) throws DataAccessException {
        try {
            persistPS.setString(1, description);
            persistPS.setInt(2, idEnseignant);
            persistPS.setInt(3, idMatiere);
            persistPS.setInt(4, idSalle);
            persistPS.setTime(5,debut);
            persistPS.setTime(6,fin);
            persistPS.setDate(7,date);
            persistPS.setInt(8, typeCours);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Cours cours) throws DataAccessException {
        try {
            updatePS.setString(1, cours.getDescription());
            updatePS.setInt(2, cours.getIdEnseignant());
            updatePS.setInt(3, cours.getIdMatiere());
            updatePS.setInt(4, cours.getIdSalle());
            updatePS.setTime(5, cours.getDebut());
            updatePS.setTime(6, cours.getFin());
            updatePS.setDate(7,cours.getDate());
            updatePS.setInt(8, cours.getTypeCours().getValue());
            updatePS.setInt(9, cours.getId());
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }



    public List<Cours> findCoursJour(Date date) throws DataAccessException {
        List<Cours> listeCours = new ArrayList<>();

        try {
            findCoursPS.setDate(1, date);

            ResultSet resultSet = findCoursPS.executeQuery();

            while (resultSet.next()) {
                Cours cours = fromResultSet(resultSet);
                listeCours.add(cours);
            }

            resultSet.close();

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return listeCours;
    }

    public Page<Cours> findCoursSemaine(int numeroSemaine, int pageNumber, int pageSize) throws  DataAccessException {
        List<Date> semaine = JourSemaine(numeroSemaine, 2023);
        List<Cours> listeCours = new ArrayList<>();
        for (Date jour : semaine) {
            listeCours.addAll(findCoursJour(jour));
        }
        return new Page<>(pageNumber, pageSize, listeCours);
    }




    public List<Cours> findCoursJourEtudiant(Date date,int id) throws DataAccessException {
        List<Cours> listeCours = new ArrayList<>();

        try {
            findCoursEtudiantPS.setDate(1, date);
            findCoursEtudiantPS.setInt(2, id);

            ResultSet resultSet = findCoursEtudiantPS.executeQuery();

            while (resultSet.next()) {
                Cours cours = fromResultSet(resultSet);
                listeCours.add(cours);
            }

            resultSet.close();

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return listeCours;
    }


    public Page<Cours> findCoursSemaineEtudiant(int numeroSemaine,int id, int pageNumber, int pageSize) throws  DataAccessException {
        List<Date> semaine = JourSemaine(numeroSemaine, 2023);
        List<Cours> listeCours = new ArrayList<>();
        for (Date jour : semaine) {
            listeCours.addAll(findCoursJourEtudiant(jour,id));
        }
        return new Page<>(pageNumber, pageSize, listeCours);
    }


            @Override
    public String getTableName() {
        return "COURS";
    }


}
