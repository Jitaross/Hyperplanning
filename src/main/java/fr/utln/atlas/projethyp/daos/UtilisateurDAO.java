package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.entities.*;
import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import lombok.extern.java.Log;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.sql.PreparedStatement;

@Log
public class UtilisateurDAO extends AbstractDAO<Utilisateur> {
    private final PreparedStatement findUtilisateurPS;
    private final PreparedStatement findLogin;
    private final PreparedStatement modifyMDPPS;
    private final PreparedStatement getMailWithIDPS;
    private final PreparedStatement getNomFormationWithIdPS;

    public UtilisateurDAO() throws DataAccessException {
        super("INSERT INTO UTILISATEUR(NOM,PRENOM,MAIL,PASSWORD,DATENAISSANCE, TYPEUTILISATEUR) VALUES (?,?,?,?,?,?)",
                "UPDATE UTILISATEUR SET NOM=?, PRENOM=?, MAIL=?, PASSWORD=?, DATENAISSANCE=?, TYPEUTILISATEUR=? WHERE ID=?");
        try {
            findUtilisateurPS = getConnection().prepareStatement("SELECT * FROM UTILISATEUR WHERE ID=?");
            findLogin = getConnection().prepareStatement("SELECT ID FROM UTILISATEUR WHERE MAIL=? AND PASSWORD=?");
            modifyMDPPS = getConnection().prepareStatement("UPDATE UTILISATEUR SET PASSWORD=? WHERE ID=?");
            getMailWithIDPS = getConnection().prepareStatement("SELECT MAIL FROM UTILISATEUR WHERE ID=?");
            getNomFormationWithIdPS = getConnection().prepareStatement("SELECT NOMFORMATION FROM FORMATION WHERE ID=(SELECT IDFORMATION FROM ETUDIANT WHERE ID=?)");


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
                .motDePasse(resultSet.getString("PASSWORD"))
                .dateNaissance(resultSet.getDate("DATENAISSANCE"))
                .typeUser(Utilisateur.TypeUser.valueOf(resultSet.getString("TYPEUTILISATEUR")))
                .build();
    }

    @Override
    public Utilisateur persist(Utilisateur utilisateur) throws DataAccessException {
        return persist(utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getMail(),
                utilisateur.getMotDePasse(),utilisateur.getDateNaissance(), utilisateur.getTypeUser());
    }

    public Utilisateur persist(String nom, String prenom, String mail, String motdepasse, Date datenaissance, Utilisateur.TypeUser typeUser) throws DataAccessException {
        try {
            persistPS.setString(1, nom);
            persistPS.setString(2, prenom);
            persistPS.setString(3, mail);
            persistPS.setString(4, motdepasse);
            persistPS.setDate(5,datenaissance);
            persistPS.setString(6, String.valueOf(typeUser));
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
            updatePS.setString(6, String.valueOf(utilisateur.getTypeUser()));
            updatePS.setInt(7,utilisateur.getId());
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }

    public int login(Authentication authentication) throws DataAccessException{
        int idUtilisateur = -1;
        try {
            findLogin.setString(1, authentication.getUserMail());
            findLogin.setString(2, new String(authentication.getPasswordHash(), StandardCharsets.UTF_8));
            ResultSet person = findLogin.executeQuery();
            while (person.next())
                idUtilisateur = person.getInt("ID");

        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return idUtilisateur;
    }


    public Utilisateur findUtilisateur(int id) throws DataAccessException {
        Utilisateur user = null;

        try {
            findUtilisateurPS.setInt(1, id);

            ResultSet resultSet = findUtilisateurPS.executeQuery();
            while(resultSet.next()){
                user = fromResultSet(resultSet);
            }

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return user;
    }

    public Utilisateur createUser(String nom, String prenom, String mail,
                                  String motDePasse, Date dateNaissance, Utilisateur.TypeUser typeuser) throws DataAccessException {
        Authentication authentication = new Authentication(mail, motDePasse);
        return persist(nom, prenom, authentication.getUserMail(),
                new String(authentication.getPasswordHash(), StandardCharsets.UTF_8), dateNaissance, typeuser);
    }

    public Etudiant createEtudiant(String nom, String prenom, String mail, String motDePasse,
                                   Date dateNaissance, int idFormation) throws DataAccessException {
        Utilisateur utilisateur = createUser(nom, prenom, mail, motDePasse, dateNaissance, Utilisateur.TypeUser.Etudiant);
        EtudiantDAO etudiantDAO = InitDAOS.getEtudiantDAO();
        return etudiantDAO.persist(utilisateur.getId(), idFormation);
    }

    public Enseignant createEnseignant(String nom, String prenom, String mail, String motDePasse,
                                   Date dateNaissance, String ufr) throws DataAccessException {
        Utilisateur utilisateur = createUser(nom, prenom, mail, motDePasse, dateNaissance, Utilisateur.TypeUser.Enseignant);
        EnseignantDAO enseignantDAO = InitDAOS.getEnseignantDAO();
        return enseignantDAO.persist(utilisateur.getId(), ufr);
    }

    public String getMailWithId(int id) throws DataAccessException {
        String mail = null;
        try {
            getMailWithIDPS.setInt(1, id);

            ResultSet resultSet = getMailWithIDPS.executeQuery();
            while (resultSet.next())
                mail = resultSet.getString("MAIL");

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return mail;
    }

    public String getNomFormationWithId(int id) throws DataAccessException {
        String nomFormation = null;
        try {
            getNomFormationWithIdPS.setInt(1, id);

            ResultSet resultSet = getNomFormationWithIdPS.executeQuery();
            while (resultSet.next())
                nomFormation = resultSet.getString("NOMFORMATION");

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return nomFormation;
    }

    public int modifyMDP(String mail, String oldMDP, String newMdp) throws DataAccessException {
        Authentication authentication = new Authentication(mail, oldMDP);
        String newMDP = new String(authentication.hashPassword(newMdp.getBytes(StandardCharsets.UTF_8)));
        try {
            int ret = -1;
            int idUser = login(authentication);
            if (idUser != -1) {
                modifyMDPPS.setString(1, newMDP);
                modifyMDPPS.setInt(2, idUser);
                modifyMDPPS.executeUpdate();
                ret = 1;
            }
            return ret;
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }


    }


    @Override
    public String getTableName() {
        return "UTILISATEUR";
    }


}
