package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import lombok.extern.java.Log;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Log
public class EtudiantDAO extends AbstractDAO<Etudiant> {
    private final PreparedStatement findEtudiantFromFormationPS;
    public EtudiantDAO() throws DataAccessException {
        super("INSERT INTO ETUDIANT(ID,IDFORMATION,GROUPE) VALUES (?,?,?)",
                "UPDATE ETUDIANT SET IDFORMATION=?, GROUPE=? WHERE ID=?");
        try{
            findEtudiantFromFormationPS = getConnection().prepareStatement("SELECT * FROM ETUDIANT WHERE IDFORMATION = ?");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }


    @Override
    protected Etudiant fromResultSet(ResultSet resultSet) throws SQLException {
        return Etudiant.builder()
                .idFormation(resultSet.getInt("IDFORMATION"))
                .build();
    }

    @Override
    public Etudiant persist(Etudiant etudiant) throws DataAccessException {
        return persist(etudiant.getId(),etudiant.getIdFormation(),etudiant.getGroupe());
    }

    public Etudiant persist(int id, int idFormation, int groupe ) throws DataAccessException {
        try {
            persistPS.setInt(1, id);
            persistPS.setInt(2,idFormation);
            persistPS.setInt(3,groupe);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Etudiant etudiant) throws DataAccessException {
        try {
            updatePS.setInt(1, etudiant.getIdFormation());
            updatePS.setInt(2, etudiant.getGroupe());
            updatePS.setInt(3,etudiant.getId());

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        super.update();
    }

    public List<Etudiant> findEtudiantFromFormation(int formation) throws DataAccessException {
        List<Etudiant> listeEtudiants = new ArrayList<>();

        try {
            findEtudiantFromFormationPS.setInt(1, formation);

            ResultSet resultSet = findEtudiantFromFormationPS.executeQuery();

            while (resultSet.next()) {
                Etudiant etudiant = fromResultSet(resultSet);
                listeEtudiants.add(etudiant);
            }

            resultSet.close();

        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }

        return listeEtudiants;
    }
    @Override
    public String getTableName() {
        return "ETUDIANT";
    }
}

