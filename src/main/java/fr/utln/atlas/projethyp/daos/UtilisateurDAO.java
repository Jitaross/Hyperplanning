package fr.utln.atlas.projethyp.daos;


import fr.utln.atlas.projethyp.entities.Enseignant;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.entities.DateSemaine;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.Data;
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
public class UtilisateurDAO extends AbstractDAO<Utilisateur> {
    private final PreparedStatement findNomUtilisateurPS;

    public UtilisateurDAO() throws DataAccessException {
        super("INSERT INTO UTILISATEUR(NOM,PRENOM,MAIL,MOTDEPASSE,DATENAISSANCE) VALUES (?,?,?,?,?)",
                "UPDATE UTILISATEUR SET NOM=?, PRENOM=?, MAIL=?, MOTDEPASSE=?, DATENAISSANCE=? WHERE ID=?");

        try{
            findNomUtilisateurPS = getConnection().prepareStatement("SELECT NOM FROM UTILISATEUR WHERE ID = ?");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }


    @Override
    protected Utilisateur fromResultSet(ResultSet resultSet) throws SQLException {
        return Utilisateur.builder()
                .id(resultSet.getInt("ID"))
                .nom(resultSet.getString("NOM"))
                .prenom(resultSet.getString("PRENOM"))
                .mail(resultSet.getString("MAIL"))
                .motDePasse(resultSet.getString("MOTDEPASSE"))
                .dateNaissance(resultSet.getDate("DATENAISSANCE"))
                .build();
    }

    @Override
    public Utilisateur persist(Utilisateur utilisateur) throws DataAccessException {
        return persist(utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getMail(),utilisateur.getMotDePasse(),utilisateur.getDateNaissance());
    }

    public Utilisateur persist(String nom, String prenom, String mail, String motdepasse, Date datenaissance) throws DataAccessException {
        try {
            persistPS.setString(1, nom);
            persistPS.setString(2, prenom);
            persistPS.setString(3, mail);
            persistPS.setString(4, motdepasse);
            persistPS.setDate(5,datenaissance);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Utilisateur utilisateur) throws DataAccessException {
        try {
            updatePS.setString(1, utilisateur.getNom());
            updatePS.setString(2, utilisateur.getPrenom());
            updatePS.setString(3, utilisateur.getMail());
            updatePS.setString(4, utilisateur.getMotDePasse());
            updatePS.setDate(5,utilisateur.getDateNaissance());
            updatePS.setInt(6,utilisateur.getId());
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }



    public String findUtilisateurNom(int id) throws DataAccessException {
        String nom;

        try {
            findNomUtilisateurPS.setInt(1, id);

            ResultSet resultSet = findNomUtilisateurPS.executeQuery();
            nom = fromResultSet(resultSet).getNom();

            resultSet.close();

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return nom;
    }


    @Override
    public String getTableName() {
        return "UTILISATEUR";
    }


}
