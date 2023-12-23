package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Salle implements Entity{
    private int id = -1;
    private String nomSalle;
    private int nombrePlace;

    @Override
    public String toString(){
        return nomSalle;
    }
}
