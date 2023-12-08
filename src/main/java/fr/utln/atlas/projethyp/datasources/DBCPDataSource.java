package fr.utln.atlas.projethyp.datasources;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {

    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl("jdbc:h2:tcp://nas.richardsebastien.fr/~/atlas");
        ds.setUsername("user");
        ds.setPassword("@tlas");

        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    private DBCPDataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}