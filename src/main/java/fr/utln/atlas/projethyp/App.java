package fr.utln.atlas.projethyp;

import fr.utln.atlas.projethyp.authentications.Authentication;
import fr.utln.atlas.projethyp.daos.AuthDao;
import lombok.extern.java.Log;

import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 *
 */
@Log
public class App {
    public static void main(String[] args) throws Exception {

        String username = "Jitaross";
        String passwordnormal = "123jesaispas";
        byte[] password = passwordnormal.getBytes(StandardCharsets.UTF_8);

        Authentication auth = new Authentication(username, password);

        try(
                AuthDao authDao = new AuthDao()
                ){
            authDao.creationLogin(auth);
            log.info("Done, now verification...");

            authDao.login(auth);
            log.info("Great !");
        }
    }
}
