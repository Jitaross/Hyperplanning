package fr.utln.atlas.projethyp.entities;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Enseignant extends Utilisateur {
    private String UFR;
}
