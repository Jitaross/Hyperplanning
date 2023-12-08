package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Etudiant extends Utilisateur{
    private int idFormation;
}
