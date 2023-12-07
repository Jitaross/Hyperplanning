package fr.utln.atlas.projethyp.authentications;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
public class Authentication {
    private final String userMail;
    private final byte[] passwordHash;

    public Authentication(String userMail, String passwordString) {
        byte[] passwordBytes = passwordString.getBytes(StandardCharsets.UTF_8);
        this.userMail = userMail;
        this.passwordHash = hashPassword(passwordBytes);
    }

    public byte[] hashPassword(byte[] psswBytes) {
        try {
            psswBytes = MessageDigest.getInstance("SHA-256").digest(psswBytes);
            return psswBytes;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
