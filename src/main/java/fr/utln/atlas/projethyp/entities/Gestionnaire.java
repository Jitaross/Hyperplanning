package fr.utln.atlas.projethyp.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class Gestionnaire extends Utilisateur implements Entity{
    private String UFR;
}
