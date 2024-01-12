package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;
import java.sql.Date;
import java.sql.PreparedStatement;

import static fr.utln.atlas.projethyp.entities.DateSemaine.JourSemaine;

@Log
public class CoursDAO extends AbstractDAO<Cours> {
    private final PreparedStatement findCoursPS;
    private final PreparedStatement findCoursEtudiantPS;
    private final PreparedStatement findCoursByIdPS;
    private final PreparedStatement findAllEtudiantCours;
    private final PreparedStatement  findAllCoursEtudiantPS;
    public CoursDAO() throws DataAccessException {
        super("INSERT INTO COURS(DESCRIPTION,IDENSEIGNANT,IDMATIERE,IDSALLE,DEBUT,FIN,DATE,TYPECOURS,GROUPE) VALUES (?,?,?,?,?,?,?,?,?)",
                "UPDATE COURS SET DESCRIPTION=?, IDENSEIGNANT=?, IDMATIERE=?, IDSALLE=?, DEBUT=?, FIN=?, DATE=?, TYPECOURS=?, GROUPE=? WHERE ID=?");

        try{
            findCoursPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ?");
            findCoursEtudiantPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ? AND IDMATIERE " +
                    "IN (SELECT ID FROM MATIERE WHERE IDFORMATION = (SELECT IDFORMATION FROM ETUDIANT WHERE ID =?)) And (GROUPE = NULL OR GROUPE = (SELECT GROUPE FROM ETUDIANT WHERE ID = ?))");
            findCoursByIdPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE ID=?");
            findAllEtudiantCours = getConnection().prepareStatement("SELECT e.*, u.* FROM COURS as c, ETUDIANT as e, UTILISATEUR as u, MATIERE as m " +
                    "WHERE c.ID=?" +
                    "AND c.IDMATIERE=m.ID " +
                    "AND m.IDFORMATION=e.IDFORMATION " +
                    "AND e.ID=u.ID");
            findAllCoursEtudiantPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE IDMATIERE IN (SELECT ID FROM MATIERE WHERE IDFORMATION = (SELECT IDFORMATION FROM ETUDIANT WHERE ID =?)) And (GROUPE = NULL OR GROUPE = (SELECT GROUPE FROM ETUDIANT WHERE ID = ?)) LIMIT ? OFFSET ?");
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
                .groupe((Integer) resultSet.getObject("GROUPE"))
                .build();
    }

    @Override
    public Cours persist(Cours cours) throws DataAccessException {
        return persist(cours.getDescription(),cours.getIdEnseignant(),cours.getIdMatiere(),cours.getIdSalle(),cours.getDebut(),cours.getFin(), cours.getDate(), cours.getTypeCours().getValue(), cours.getGroupe());
    }

    public Cours persist(String description, int idEnseignant, int idMatiere, int idSalle, Time debut, Time fin, Date date, int typeCours, Integer groupe ) throws DataAccessException {
        try {
            persistPS.setString(1, description);
            persistPS.setInt(2, idEnseignant);
            persistPS.setInt(3, idMatiere);
            persistPS.setInt(4, idSalle);
            persistPS.setTime(5,debut);
            persistPS.setTime(6,fin);
            persistPS.setDate(7,date);
            persistPS.setInt(8, typeCours);
            persistPS.setObject(9, groupe);
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
            updatePS.setObject(9, cours.getGroupe());
            updatePS.setInt(10, cours.getId());
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

    public Page<Cours> findCoursSemaine(int numeroSemaine, int pageNumber, int pageSize, int annee) throws  DataAccessException {
        List<Date> semaine = JourSemaine(numeroSemaine, annee);
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
            findCoursEtudiantPS.setInt(3, id);

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


    public Page<Cours> findCoursSemaineEtudiant(int numeroSemaine,int id, int pageNumber, int pageSize, int annee) throws  DataAccessException {
        List<Date> semaine = JourSemaine(numeroSemaine, annee);
        List<Cours> listeCours = new ArrayList<>();
        for (Date jour : semaine) {
            listeCours.addAll(findCoursJourEtudiant(jour,id));
        }
        return new Page<>(pageNumber, pageSize, listeCours);
    }

    public Cours findCoursById(int id) throws SQLException {
        Cours cours = null;
        try{
            findCoursByIdPS.setInt(1,id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ResultSet resultSet = findCoursByIdPS.executeQuery();
        while (resultSet.next()) {cours = fromResultSet(resultSet);}

        return cours;

    }

    public List<Etudiant> findAllEtudiant(int idCours) throws DataAccessException {
        List<Etudiant> utilisateurs = new ArrayList<>();
        try {
            findAllEtudiantCours.setInt(1, idCours);
            ResultSet resultSet = findAllEtudiantCours.executeQuery();
            while(resultSet.next()) utilisateurs.add(InitDAOS.getEtudiantDAO().fromResultSet(resultSet));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return utilisateurs;
    }


    public Page<Cours> findAllCoursEtudiantPS(int id, int pageNumber, int pageSize) throws DataAccessException {
        List<Cours> entityList = new ArrayList<>();
        try {
            findAllCoursEtudiantPS.setInt(1, id);
            findAllCoursEtudiantPS.setInt(2, id);
            findAllCoursEtudiantPS.setInt(3, pageSize);
            findAllCoursEtudiantPS.setInt(4, (pageNumber - 1) * pageSize);
            ResultSet rs = findAllCoursEtudiantPS.executeQuery();
            while (rs.next()) entityList.add(fromResultSet(rs));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, entityList);
    }

            @Override
    public String getTableName() {
        return "COURS";
    }


}
