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
    private int id = -1;
    private String description;
    private int idEnseignant;
    private int idMatiere;
    private int idsalle;
    private Time debut;
    private Time fin;
    private Date date;

}
