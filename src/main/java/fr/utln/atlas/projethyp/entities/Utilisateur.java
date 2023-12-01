package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Utilisateur implements Entity {
    private int id = -1;
    private String Nom;
    private String Prenom;
    private String Mail;
    private Date dateNaissance;
}
