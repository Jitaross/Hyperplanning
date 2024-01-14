package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.entities.Formation;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log
public class EtudiantDAO extends AbstractDAO<Etudiant> {
    private final PreparedStatement findEtudiantFromFormationPS;
    private final PreparedStatement findFormation;
    public EtudiantDAO() throws DataAccessException {
        super("INSERT INTO ETUDIANT(ID,IDFORMATION) VALUES (?,?)",
                "UPDATE ETUDIANT SET IDFORMATION=? WHERE ID=?");
        try{
            findFormation = getConnection().prepareStatement("SELECT f.* FROM ETUDIANT as e,FORMATION as f where e.ID=?" +
                    "AND e.IDFORMATION=f.ID");
            findEtudiantFromFormationPS = getConnection().prepareStatement("SELECT * FROM ETUDIANT WHERE IDFORMATION = ?");
        } catch(SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
    }


    @Override
    protected Etudiant fromResultSet(ResultSet resultSet) throws SQLException {
        return Etudiant.builder()
                .id(resultSet.getInt("ID"))
                .idFormation(resultSet.getInt("IDFORMATION"))
                .build();
    }

    @Override
    public Etudiant persist(Etudiant etudiant) throws DataAccessException {
        return persist(etudiant.getId(),etudiant.getIdFormation());
    }

    public Etudiant persist(int id, int idFormation ) throws DataAccessException {
        try {
            persistPS.setInt(1, id);
            persistPS.setInt(2,idFormation);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        return super.persist();
    }
    @Override
    public void update(Etudiant etudiant) throws DataAccessException {
        try {
            updatePS.setInt(1, etudiant.getIdFormation());
            updatePS.setInt(2,etudiant.getId());

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

    public Formation findFormation(int idEtud) throws DataAccessException {
        Formation formation = null;
        try {
            findFormation.setInt(1, idEtud);
            ResultSet resultSet = findFormation.executeQuery();
            while (resultSet.next()) formation = Formation.builder()
                    .id(resultSet.getInt("ID"))
                    .nomFormation(resultSet.getString("NOMFORMATION"))
                    .build();
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return formation;
    }

    @Override
    public String getTableName() {
        return "ETUDIANT";
    }
}

