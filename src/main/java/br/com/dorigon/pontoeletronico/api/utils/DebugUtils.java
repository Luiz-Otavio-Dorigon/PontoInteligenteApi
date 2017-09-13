package br.com.dorigon.pontoeletronico.api.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public final class DebugUtils {

    private static final String TAG = "Ponto_Inteligent_Debug";

    private DebugUtils() {
    }

    public static void log(Class<?> clazz, String message, Exception e) {
        Logger.getLogger(clazz.getSimpleName()).log(Level.SEVERE, message, e);
    }

    public static void log(Class<?> clazz, String message) {
        Logger.getLogger(clazz.getSimpleName()).log(Level.INFO, message);
    }

    public static void log(String message) {
        Logger.getLogger(TAG).log(Level.INFO, message);
    }

}
