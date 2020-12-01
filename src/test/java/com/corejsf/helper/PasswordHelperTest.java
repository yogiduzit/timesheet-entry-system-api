package com.corejsf.helper;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.corejsf.helpers.PasswordHelper;

public class PasswordHelperTest {

    @Test
    public void verifyPassword() {
        final String password = "fbnjkfsd@!1fds";
        final String hash = PasswordHelper.encrypt(password);

        assertTrue(PasswordHelper.validate(hash, password));
    }

}
