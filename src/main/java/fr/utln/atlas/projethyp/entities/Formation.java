package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Formation implements Entity{
    private int id = -1;
    private String nomFormation;

    @Override
    public String toString(){
        return nomFormation;
    }
}
