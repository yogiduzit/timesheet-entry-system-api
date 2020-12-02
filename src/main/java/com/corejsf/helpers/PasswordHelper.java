package com.corejsf.helpers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class PasswordHelper {
    private static final String ALGORITHM = "SHA-256";

    private final MessageDigest digest;

    public PasswordHelper() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(ALGORITHM);
    }

    public byte[] encrypt(String password) {
        final byte[] pwd = password.getBytes(StandardCharsets.UTF_8);

        byte[] hashed = new byte[] {};
        try {
            // Hash password
            hashed = digest.digest(pwd);
        } finally {
            // Wipe confidential data
            Arrays.fill(pwd, (byte) 0);
        }
        return hashed;
    }

    public boolean validate(byte[] expHash, String actualPwd) {
        return MessageDigest.isEqual(expHash, encrypt(actualPwd));
    }

}
