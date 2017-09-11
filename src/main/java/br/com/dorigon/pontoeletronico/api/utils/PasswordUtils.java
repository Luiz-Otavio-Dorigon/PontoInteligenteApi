package br.com.dorigon.pontoeletronico.api.utils;

import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static java.util.Objects.isNull;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public class PasswordUtils {

    public PasswordUtils() {
    }

    /**
     * Genare the hash by BCrypt.
     *
     * @param senha Your password.
     * @return Return hash.
     */
    @Nullable
    public static String geraBCrypt(String senha) {
        if (isNull(senha)) {
            return null;
        }

        DebugUtils.log("Genarating hash by BCrypt.");

        return new BCryptPasswordEncoder().encode(senha);
    }
}
