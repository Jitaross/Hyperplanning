package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Entity;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This interface defines the generic method for DAO Objects.
 *
 * @param <E> is the type of entities managed by the DAO
 */
public interface DAO<E extends Entity> extends AutoCloseable {
    /**
     * Finds and returns an entity by id in the database.
     * Note the usage of Optional to avoir Null Pointer Exception (NPE).
     *
     * @param id the id of the entity
     * @return the entity with the given id from the database.
     * @throws DataAccessException If there is a data access error (see message).
     */
    Optional<E> find(long id) throws DataAccessException;

    /**
     * Return a given page entities managed by the DAO from the database.
     *
     * @return the list of entities managed by the DAO.
     * @throws DataAccessException If there is a data access error (see message).
     */
    Page<E> findAll(int pageNumber, int pageSize) throws DataAccessException;

    /**
     * Return the first page of entities managed by the DAO from the database.
     *
     * @return the list of entities managed by the DAO.
     * @throws DataAccessException If there is a data access error (see message).
     */
    default Page<E> findAll() throws DataAccessException {
        return findAll(1, AbstractDAO.DEFAULT_PAGE_SIZE);
    }

    /**
     * Persists a new entity in the database.
     *
     * @param e The entity to be persisted.
     * @return The entity or a clone if the id has been updated by the database.
     * @throws DataAccessException If there is a data access error (see message).
     */
    E persist(E e) throws DataAccessException;

    /**
     * A default method to persist a list of entities.
     *
     * @param list The list of entities to be persisted.
     * @return The list of entities or clones if their id has been updated by the database.
     * @throws DataAccessException If there is a data access error (see message).
     */
    default List<E> persist(List<E> list) throws DataAccessException {
        List<E> resultList = new ArrayList<>();
        for (E e : list) resultList.add(persist(e));
        return resultList;
    }

    /**
     * Update the entity in the database with the parameter.
     *
     * @param e The entity to be updated. The id is used and cannot be updated.
     * @throws DataAccessException If there is a data access error (see message).
     */
    void update(E e) throws DataAccessException;

    /**
     * Removes the entity with the given id from the database.
     *
     * @param id the id of the entity to remove
     * @throws DataAccessException If there is a data access error (see message).
     */
    void remove(long id) throws DataAccessException;

    /**
     * Removes the given entity from the database using its ID.
     *
     * @param e the entity to be removed.
     * @throws DataAccessException If there is a data access error (see message).
     */
    default void remove(E e) throws DataAccessException {
        remove(e.getId());
    }

    /**
     * Removes all the entities managed by the DAO from the database.
     *
     * @throws DataAccessException If there is a data access error (see message).
     */
    void clean() throws DataAccessException;

    /**
     * Release the connection to the connection pool
     */
    void close() throws DataAccessException;

}
