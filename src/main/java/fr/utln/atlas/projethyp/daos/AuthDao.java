package fr.utln.atlas.projethyp.daos;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.datasources.Datasource;
import lombok.extern.java.Log;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;

@Log
public class AuthDao implements AutoCloseable {
    private final  Connection connection;
    private final PreparedStatement findLogin;
    private final PreparedStatement createLogin;


    public AuthDao() {
        try {
            connection = Datasource.getConn();
            findLogin = connection.prepareStatement("SELECT * from " + "Utilisateur" + " WHERE USERNAME=? AND PASSWORD=?");
            createLogin = connection.prepareStatement("INSERT INTO " + "Utilisateur" + "(USERNAME, Password) VALUES (?, ? )");
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Error while creating the DAO : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int creationLogin(Authentication auth) {
        try {
            createLogin.setString(1, auth.getUsername());
            createLogin.setString(2, new String(auth.getPasswordBytes(), StandardCharsets.UTF_8));
            createLogin.executeUpdate();
            log.info("Utilisateur " + auth.getUsername() + " B=bien créée.");
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(Authentication authentication) {
        try {
            findLogin.setString(1, authentication.getUsername());
            findLogin.setString(2, new String(authentication.getPasswordBytes(), StandardCharsets.UTF_8));
            ResultSet person = findLogin.executeQuery();
            log.info("Person id : %s".formatted(person.getString(1)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        try {
            connection.close();
        } catch (SQLException throwable) {
            throw new RuntimeException(throwable.getLocalizedMessage());
        }
    }
}
