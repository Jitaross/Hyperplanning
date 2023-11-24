package fr.utln.atlas.projethyp.datasources;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {

    private static final BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl(System.getProperty("jdbcdao.datasource.url"));
        ds.setUsername(System.getProperty("jdbcdao.datasource.username"));
        ds.setPassword(System.getProperty("jdbcdao.datasource.password"));

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