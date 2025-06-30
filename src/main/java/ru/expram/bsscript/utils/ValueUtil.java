package ru.expram.bsscript.utils;

import ru.expram.bsscript.value.Value;

public class ValueUtil {

    public static Value make(String value) {
        return new Value(value);
    }

    public static Value make(boolean value) {
        return new Value(value);
    }

    public static Value make(int value) {
        return new Value(value);
    }

    public static Value make(float value) {
        return new Value(value);
    }
}
