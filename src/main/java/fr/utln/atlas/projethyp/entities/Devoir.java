package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Devoir implements Entity{
    private int id;
    private int idUtilisateur;
    private int note;
    private String commentaire;

    public enum TypeDevoir {
        Surveille,
        Maison
    }

    private TypeDevoir typeDevoir;

}
