package fr.utln.atlas.projethyp.datasources;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Datasource {
    private static final BasicDataSource BDatasource = new BasicDataSource();

    static {
        BDatasource.setMaxIdle(10);
        BDatasource.setMinIdle(5);
        BDatasource.setMaxOpenPreparedStatements(100);

        BDatasource.setUrl("jdbc:h2:tcp://nas.richardsebastien.fr/~/atlas");
        BDatasource.setUsername("user");
        BDatasource.setPassword("@tlas");

    }

    private Datasource() {
    }

    public static Connection getConn() throws SQLException {
        return BDatasource.getConnection();
    }
}
