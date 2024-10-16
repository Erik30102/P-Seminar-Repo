package com.Pseminar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Damit die Console schön aussieht
 */
public class Logger {
    private static final String ANSI_RED = "\u001B[31m";
	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_WHITE = "\u001B[37m";

    public static void info(String message) {
        System.out.println(formatText(message, ""));
    }

    public static void warm(String message) {
        System.out.println(formatText(message, ANSI_YELLOW));
    }

    public static void error(String message) {
        System.out.println(formatText(message, ANSI_RED));
    }

    private static String formatText(String message, String Color)  {
        return Color + "[ " +  (new SimpleDateFormat("HH:mm:ss").format(new Date())) + "] " + message + ANSI_WHITE;
    }
}
