package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Absence implements Entity{
    private int id;
    private int idCours;
    private int idEtudiant;
}
