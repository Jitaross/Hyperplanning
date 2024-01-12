package fr.utln.atlas.projethyp.exceptions;

public class NotFoundException extends DataAccessException {
    public NotFoundException() {
        super("Entity not found");
    }
}
