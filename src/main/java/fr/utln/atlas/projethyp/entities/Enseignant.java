package fr.utln.atlas.projethyp.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Enseignant extends Utilisateur {
    private String UFR;
}
