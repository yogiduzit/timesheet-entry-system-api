package com.corejsf.helper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.Test;

import com.corejsf.helpers.PasswordHelper;

public class PasswordHelperTest {

    @Test
    public void verifyPassword() {
        PasswordHelper helper = null;
        try {
            helper = new PasswordHelper();
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (helper == null) {
            fail("No such algorithm");
            return;
        }
        final String password = "fbnjkfsd@!1fds";
        final byte[] hash = helper.encrypt(password);

        assertTrue(helper.validate(hash, password));
    }

}
