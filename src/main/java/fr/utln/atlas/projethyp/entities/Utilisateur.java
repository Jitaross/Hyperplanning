package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.*;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Utilisateur implements Entity {
    @Builder.Default
    private int id = -1;
    private String nom;
    private String prenom;
    private String mail;
    private String motDePasse;
    private Date dateNaissance;
}
