package fr.utln.atlas.projethyp.authentications;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
public class Authentication {
    private final String username;
    private final byte[] passwordBytes;

    public Authentication(String username, byte[] passwordBytes) {
        this.username = username;
        this.passwordBytes = hashPassword(passwordBytes);
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
