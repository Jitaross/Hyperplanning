package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Salle implements Entity{
    private int Id;
    private int nombrePlace;
}
