package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Salle implements Entity{
    @Builder.Default
    private long id = -1;
    private String nomSalle;

    @Override
    public long getId() {
        return id;
    }
}
