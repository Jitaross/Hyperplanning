package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Utilisateur implements Entity {
    @Builder.Default
    private int id = -1;
    private String nom;
    private String prenom;
    private String mail;
    private String motDePasse;
    private Date dateNaissance;
    public enum TypeUser {
        Etudiant,
        Enseignant,
        Gestionnaire,
        SuperAdmin
    }
    private TypeUser typeUser;
}
