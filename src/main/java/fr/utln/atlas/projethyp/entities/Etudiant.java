package fr.utln.atlas.projethyp.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Etudiant extends Utilisateur{
    private int idFormation;
    private String nomFormation;
    private Integer groupe;
}
