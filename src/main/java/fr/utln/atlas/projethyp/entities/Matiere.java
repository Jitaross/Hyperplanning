package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Matiere implements Entity{
    private int id = -1;
    private String nomMatiere;
    private int idFormation;
}
