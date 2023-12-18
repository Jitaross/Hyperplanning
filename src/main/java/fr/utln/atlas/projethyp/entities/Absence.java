package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Blob;

@Data
@Builder
public class Absence implements Entity{
	private int id;
	private int idCours;
	private int idEtudiant;
	String motif;
	private byte[] justificatif;
}