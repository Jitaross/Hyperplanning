package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.sql.Date;

@Data
@Builder
public class Cours implements Entity {
    @Builder.Default
    private long id = -1;
    private String nomMatiere;
    private String description;
    private long idEnseignant;
    private Time debut;
    private Time fin;
    private Date date;

}
