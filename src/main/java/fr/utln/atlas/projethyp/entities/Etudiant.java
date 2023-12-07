package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Etudiant implements Entity{
    @Builder.Default
    private int id = -1;
    private String formation;
}
