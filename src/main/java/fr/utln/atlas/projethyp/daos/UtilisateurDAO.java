package fr.utln.atlas.projethyp.daos;


import fr.utln.atlas.projethyp.entities.Utilisateur;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;
import java.sql.*;

@Log
public class UtilisateurDAO extends AbstractDAO<Utilisateur> {
    private final PreparedStatement findNomUtilisateurPS;

    public UtilisateurDAO() throws DataAccessException {
        super("INSERT INTO UTILISATEUR(NOM,PRENOM,MAIL,PASSWORD,DATENAISSANCE) VALUES (?,?,?,?,?)",
                "UPDATE UTILISATEUR SET NOM=?, PRENOM=?, MAIL=?, PASSWORD=?, DATENAISSANCE=? WHERE ID=?");

        try{
            // The prepared statement to search a Utilisateur by id
            findNomUtilisateurPS = getConnection().prepareStatement("SELECT NOM FROM UTILISATEUR WHERE ID = ?");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }

    /**
     * Initialize an Utilisateur object with data collected in resultSet
     * @param resultSet The data collected from the SQL query
     * @return The object Utilisateur initialized with data from resultSet
     * @throws SQLException If collecting data from resultSet throw an error
     */
    @Override
    protected Utilisateur fromResultSet(ResultSet resultSet) throws SQLException {
        return Utilisateur.builder()
                .id(resultSet.getInt("ID"))
                .nom(resultSet.getString("NOM"))
                .prenom(resultSet.getString("PRENOM"))
                .mail(resultSet.getString("MAIL"))
                .motDePasse(resultSet.getString("PASSWORD"))
                .dateNaissance(resultSet.getDate("DATENAISSANCE"))
                .build();
    }

    /**
     * Add a row in the database in Utilisateur table from an object Utilisateur
     * @param utilisateur The entity to be persisted.
     * @return The entity persisted
     * @throws DataAccessException If unable to access data
     */
    @Override
    public Utilisateur persist(Utilisateur utilisateur) throws DataAccessException {
        return persist(utilisateur.getNom(),utilisateur.getPrenom(),utilisateur.getMail(),utilisateur.getMotDePasse(),utilisateur.getDateNaissance());
    }

    /**
     * Persist a Utilisateur object by passing all the data in parameters
     * @param nom The name of the Utilisateur
     * @param prenom The firstname of the Utilisateur
     * @param mail The mail address of the Utilisateur
     * @param motdepasse The password (which is hashed) of the Utilisateur
     * @param datenaissance The date of birth of Utilisateur
     * @return The entity persisted
     * @throws DataAccessException If unable to access data
     */
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

    /**
     * Update an Utilisateur in the table
     * @param utilisateur The entity to be updated. The id is used and cannot be updated.
     * @throws DataAccessException If unable to access data
     */
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


    /**
     * Search for Utilisateur with id in the table
     * @param id The id of Utilisateur we search
     * @return The name of Utilisateur
     * @throws DataAccessException If unable to access data
     */
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

    /**
     * Give the name of the Table
     * @return The table name
     */
    @Override
    public String getTableName() {
        return "UTILISATEUR";
    }


}
