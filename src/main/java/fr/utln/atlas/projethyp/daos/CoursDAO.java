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
    private final PreparedStatement findAllByIdPS;
    private final PreparedStatement findCoursFormationPS;
    private final PreparedStatement deleteCoursByIdPS;

    public CoursDAO() throws DataAccessException {
        super("INSERT INTO COURS(DESCRIPTION,IDENSEIGNANT,IDMATIERE,IDSALLE,DEBUT,FIN,DATE,TYPECOURS) VALUES (?,?,?,?,?,?,?,?)",
                "UPDATE COURS SET DESCRIPTION=?, IDENSEIGNANT=?, IDMATIERE=?, IDSALLE=?, DEBUT=?, FIN=?, DATE=?, TYPECOURS=? WHERE ID=?");

        try{
            findCoursPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ?");
            findCoursFormationPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ? AND IDMATIERE " +
                    "IN (SELECT ID FROM MATIERE WHERE IDFORMATION = ?)");
            findCoursEtudiantPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE DATE = ? AND IDMATIERE " +
                    "IN (SELECT ID FROM MATIERE WHERE IDFORMATION = (SELECT IDFORMATION FROM ETUDIANT WHERE ID =?))");
            findCoursByIdPS = getConnection().prepareStatement("SELECT * FROM COURS WHERE ID=?");
            findAllEtudiantCours = getConnection().prepareStatement("SELECT e.*, u.* FROM COURS as c, ETUDIANT as e, UTILISATEUR as u, MATIERE as m " +
                    "WHERE c.ID=?" +
                    "AND c.IDMATIERE=m.ID " +
                    "AND m.IDFORMATION=e.IDFORMATION " +
                    "AND e.ID=u.ID");
            findAllByIdPS = getConnection().prepareStatement("SELECT Cours.* FROM Etudiant JOIN Matiere ON Etudiant.idformation = Matiere.idformation JOIN Cours ON Matiere.id = Cours.idmatiere WHERE Etudiant.id = ?;");
            deleteCoursByIdPS = getConnection().prepareStatement("DELETE FROM COURS WHERE ID=?");
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

    public List<Cours> findAllById(int id) throws DataAccessException {
        List<Cours> listeCours = new ArrayList<>();

        try {
            findAllByIdPS.setInt(1, id);

            ResultSet resultSet = findAllByIdPS.executeQuery();

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

    public Page<Cours> findCoursSemaineFormation(int numeroSemaine, int idFormation, int pageNumber, int pageSize, int annee) throws DataAccessException{
        List<Date> semaine = JourSemaine(numeroSemaine, annee);
        List<Cours> listeCours = new ArrayList<>();
        for(Date jour : semaine){
            List<Cours> listeCoursJour = new ArrayList<>();

            try {
                findCoursFormationPS.setDate(1, jour);
                findCoursFormationPS.setInt(2, idFormation);

                ResultSet resultSet = findCoursFormationPS.executeQuery();

                while (resultSet.next()) {
                    Cours cours = fromResultSet(resultSet);
                    listeCoursJour.add(cours);
                }

                resultSet.close();

            } catch (SQLException throwables) {
                throw new DataAccessException(throwables.getLocalizedMessage());
            }
            listeCours.addAll(listeCoursJour);
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
    public void deleteCoursById(int id) throws SQLException{
        try{
            deleteCoursByIdPS.setInt(1,id);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        deleteCoursByIdPS.executeUpdate();
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


            @Override
    public String getTableName() {
        return "COURS";
    }


}
