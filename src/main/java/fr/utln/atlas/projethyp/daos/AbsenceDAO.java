package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Absence;
import fr.utln.atlas.projethyp.entities.Cours;
import fr.utln.atlas.projethyp.entities.Etudiant;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDAO extends AbstractDAO<Absence>{
	private final PreparedStatement findAbsenceEtudiantPS;
	private final PreparedStatement findAbsenceCoursPS;
	private final PreparedStatement insertJustificatifPS;
	public AbsenceDAO() throws DataAccessException {
		super("INSERT INTO ABSENCE(IDCOURS,IDETUDIANT,MOTIF) VALUES (?,?,?)",
				"UPDATE ABSENCE SET IDCOURS=?, IDETUDIANT=? WHERE ID=?");
		try{
			findAbsenceEtudiantPS = getConnection().prepareStatement("SELECT ID,IDCOURS,IDETUDIANT,MOTIF FROM ABSENCE WHERE IDETUDIANT = ?");
			findAbsenceCoursPS = getConnection().prepareStatement("SELECT ID,IDCOURS,IDETUDIANT,MOTIF FROM ABSENCE WHERE IDCOURS = ?");
			insertJustificatifPS = getConnection().prepareStatement("UPDATE ABSENCE SET JUSTIFICATIF=? WHERE ID=?");
		} catch(SQLException e) {
			throw new DataAccessException(e.getLocalizedMessage());
		}
	}


	@Override
	protected Absence fromResultSet(ResultSet resultSet) throws SQLException {
		return Absence.builder()
				.id(resultSet.getInt("ID"))
				.idCours(resultSet.getInt("IDCOURS"))
				.idEtudiant(resultSet.getInt("IDETUDIANT"))
				.motif(resultSet.getString("MOTIF"))
				.build();
	}

	@Override
	public Absence persist(Absence absence) throws DataAccessException {
		return persist(absence.getIdCours(),absence.getIdEtudiant(),absence.getMotif());
	}

	public Absence persist(int idCours, int idEtudiant , String motif) throws DataAccessException {
		try {
			persistPS.setInt(1,idCours);
			persistPS.setInt(2,idEtudiant);
			persistPS.setString(3,motif);
		} catch (SQLException throwables) {
			throw new DataAccessException(throwables.getLocalizedMessage());
		}
		return super.persist();
	}
	@Override
	public void update(Absence absence) throws DataAccessException {
		try {
			updatePS.setInt(1, absence.getIdCours());
			updatePS.setInt(2, absence.getIdEtudiant());
			updatePS.setInt(3, absence.getId());

		} catch (SQLException throwables) {
			throw new DataAccessException(throwables.getLocalizedMessage());
		}
		super.update();
	}

	public void insertJustificatif(int id, byte[] justificatif) throws DataAccessException, SQLException {
		try{
			insertJustificatifPS.setBytes(1,justificatif);
			insertJustificatifPS.setInt(2,id);
		} catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
		insertJustificatifPS.executeUpdate();
    }

	public List<Absence> findAbsenceEtudiant(int id) throws DataAccessException {
		List<Absence> listeAbsences = new ArrayList<>();

		try {
			findAbsenceEtudiantPS.setInt(1, id);

			ResultSet resultSet = findAbsenceEtudiantPS.executeQuery();

			while (resultSet.next()) {
				Absence absence = fromResultSet(resultSet);
				listeAbsences.add(absence);
			}

			resultSet.close();

		} catch (SQLException throwables) {
			throw new DataAccessException(throwables.getLocalizedMessage());
		}

		return listeAbsences;
	}

	public List<Absence> findAbsenceCours(int id) throws DataAccessException {
		List<Absence> listeAbsences = new ArrayList<>();

		try {
			findAbsenceCoursPS.setInt(1, id);

			ResultSet resultSet = findAbsenceCoursPS.executeQuery();

			while (resultSet.next()) {
				Absence absence = fromResultSet(resultSet);
				listeAbsences.add(absence);
			}

			resultSet.close();

		} catch (SQLException throwables) {
			throw new DataAccessException(throwables.getLocalizedMessage());
		}

		return listeAbsences;
	}
	@Override
	public String getTableName() {
		return "ABSENCE";
	}
}