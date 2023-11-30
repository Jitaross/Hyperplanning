package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class Cours implements Entity {
    @Builder.Default
    private long id = -1;
    private String nomMatiere;
    private String description;
    private long idEnseignant;
    private long idsalle;
    private Timestamp debut;
    private Timestamp fin;

}
