package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.entities.Entity;
import fr.utln.atlas.projethyp.exceptions.DataAccessException;
import fr.utln.atlas.projethyp.exceptions.NotFoundException;
import fr.utln.atlas.projethyp.datasources.DBCPDataSource;
import lombok.Getter;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

@Log
public abstract class AbstractDAO<E extends Entity> implements DAO<E> {
    public static final int DEFAULT_PAGE_SIZE = 10;
    @Getter
    protected final Connection connection;
    protected final PreparedStatement persistPS;
    protected final PreparedStatement updatePS;
    private final PreparedStatement findPS;
    private final PreparedStatement findAllPS;

    protected AbstractDAO(String persistPS, String updatePS) throws DataAccessException {
        Connection _connection = null;
        PreparedStatement _findPS = null;
        PreparedStatement _findAllPS = null;
        PreparedStatement _persistPS = null;
        PreparedStatement _updatePS = null;
        try {
            _connection = DBCPDataSource.getConnection();
            _findPS = _connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE ID=?");
            _findAllPS = _connection.prepareStatement("SELECT * FROM " + getTableName() + " LIMIT ? OFFSET ?");
            _persistPS = _connection.prepareStatement(persistPS, Statement.RETURN_GENERATED_KEYS);
            _updatePS = _connection.prepareStatement(updatePS);
        } catch (SQLException throwables) {
            log.log(Level.SEVERE, "Erreur de création de la DAO: " + throwables.getMessage());
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
        this.connection = _connection;
        this.findPS = _findPS;
        this.findAllPS = _findAllPS;
        this.persistPS = _persistPS;
        this.updatePS = _updatePS;
        log.warning(getTableName() + " DAO Created.");
    }

    public abstract String getTableName();

    public E persist() throws DataAccessException {
        long id = -1;
        try {
            persistPS.executeUpdate();
            ResultSet rs = persistPS.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
                log.fine("Generated PK = " + id);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return find(id).orElseThrow(NotFoundException::new);
    }

    public Optional<E> find(long id) throws DataAccessException {
        E entity = null;
        try {
            findPS.setLong(1, id);
            ResultSet rs = findPS.executeQuery();
            while (rs.next())
                entity = fromResultSet(rs);
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        if (entity==null) throw new NotFoundException();
        return Optional.of(entity);
    }

    @Override
    public Page<E> findAll(int pageNumber, int pageSize) throws DataAccessException {
        List<E> entityList = new ArrayList<>();
        try {
            findAllPS.setInt(1, pageSize);
            findAllPS.setInt(2, (pageNumber - 1) * pageSize);
            ResultSet rs = findAllPS.executeQuery();
            while (rs.next()) entityList.add(fromResultSet(rs));
        } catch (SQLException e) {
            throw new DataAccessException(e.getLocalizedMessage());
        }
        return new Page<>(pageNumber, pageSize, entityList);
    }

    public void remove(long id) throws DataAccessException {
        try {
            connection.createStatement().execute("DELETE FROM " + getTableName() + " WHERE ID=" + id);
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
    }

    public void clean() throws DataAccessException {
        try {
            connection.createStatement().execute("DELETE FROM " + getTableName());
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
    }

    @Override
    public void close() throws DataAccessException {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
    }

    protected abstract E fromResultSet(ResultSet resultSet) throws SQLException;

    public void update() throws DataAccessException {
        try {
            updatePS.executeUpdate();
        } catch (SQLException throwables) {
            throw new DataAccessException(throwables.getLocalizedMessage());
        }
    }
}