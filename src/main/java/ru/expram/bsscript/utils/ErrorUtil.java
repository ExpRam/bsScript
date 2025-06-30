package ru.expram.bsscript.utils;

public class ErrorUtil {

    public static RuntimeException error(String message) {
        return new RuntimeException(message);
    }

}
