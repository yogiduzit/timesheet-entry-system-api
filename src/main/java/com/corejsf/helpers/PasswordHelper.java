package com.corejsf.helpers;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHelper {
    private static final Argon2 argon2 = Argon2Factory.create();

    private static final int MEMORY = 65536;
    private static final int ITERATIONS = 2;
    private static final int THREADS = 1;

    public static String encrypt(String password) {
        final char[] pwd = password.toCharArray();

        String hashed = "";
        try {
            // Hash password
            hashed = argon2.hash(ITERATIONS, MEMORY, THREADS, pwd);
        } finally {
            // Wipe confidential data
            argon2.wipeArray(pwd);
        }
        return hashed;
    }

    public static boolean validate(String expHash, String actualPwd) {
        return argon2.verify(expHash, actualPwd.toCharArray());
    }

}
