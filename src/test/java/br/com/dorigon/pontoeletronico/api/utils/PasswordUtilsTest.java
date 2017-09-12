package br.com.dorigon.pontoeletronico.api.utils;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public class PasswordUtilsTest {

    private static final String PASSWORD = "123456";
    private final BCryptPasswordEncoder mBCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testPasswordIsNull() throws Exception {
        assertNull("Password is Null!!!", PasswordUtils.geraBCrypt(null));
    }

    @Test
    public void testPasswordNotNull() throws Exception {
        String password = PasswordUtils.geraBCrypt(PASSWORD);

        assertTrue("Password matche!!!", mBCryptPasswordEncoder.matches(PASSWORD, password));
    }
}
