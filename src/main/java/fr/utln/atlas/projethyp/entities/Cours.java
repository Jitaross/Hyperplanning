package fr.utln.atlas.projethyp.entities;

import lombok.Builder;
import lombok.Data;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class Cours implements Entity {
    public enum TypeCours {
        CM(1), TD(2), TP(3), DS(4), EXAMEN(5), REUNION(6), AUTRE(7);

        private final int value;

        TypeCours(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static TypeCours fromValue(int value) {
            for (TypeCours type : TypeCours.values()) {
                if (type.getValue() == value) {
                    return type;
                }
            }
            // Gérer le cas où aucune correspondance n'est trouvée, par exemple :
            throw new IllegalArgumentException("Aucun type de cours ne correspond à la valeur " + value);
        }
    }

    @Builder.Default
    private int id = -1;
    private String description;
    private int idEnseignant;
    private int idMatiere;
    private int idSalle;
    private Time debut;
    private Time fin;
    private Date date;
    private TypeCours typeCours;
    private int groupe;

}
