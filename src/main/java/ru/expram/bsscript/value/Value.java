package ru.expram.bsscript.value;

import java.util.Objects;

public class Value {

    public static final Value NULL = new Value(null, ValueType.NULL);

    private final Object value;
    private final ValueType type;

    public Value(Object value) {
        this.value = value;
        this.type = resolveType(value);
    }

    private Value(Object value, ValueType type) {
        this.value = value;
        this.type = type;
    }

    private ValueType resolveType(Object value) {
        if (value == null) return ValueType.NULL;
        if (value instanceof Boolean) return ValueType.BOOLEAN;
        if (value instanceof Integer) return ValueType.INTEGER;
        if (value instanceof Float) return ValueType.FLOAT;
        if (value instanceof String) return ValueType.STRING;
        throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
    }

    public boolean isInteger() {
        return type == ValueType.INTEGER;
    }

    public boolean isFloat() {
        return type == ValueType.FLOAT;
    }

    public boolean isNumber() {
        return isInteger() || isFloat();
    }

    public boolean isBoolean() {
        return type == ValueType.BOOLEAN;
    }

    public boolean isString() {
        return type == ValueType.STRING;
    }

    public int asInteger() {
        switch (type) {
            case INTEGER -> { return (Integer) value; }
            case FLOAT -> { return ((Float) value).intValue(); }
            default -> throw new UnsupportedOperationException("Value is not an integer: " + type);
        }
    }

    public float asFloat() {
        switch (type) {
            case INTEGER -> { return ((Integer) value).floatValue(); }
            case FLOAT -> { return (Float) value; }
            default -> throw new UnsupportedOperationException("Value is not a float: " + type);
        }
    }

    public boolean asBoolean() {
        if (type == ValueType.BOOLEAN) {
            return (Boolean) value;
        }

        throw new UnsupportedOperationException("Value is not a boolean: " + type);
    }

    public String asString() {
        if (type == ValueType.STRING) {
            return (String) value;
        }
        return String.valueOf(value);
    }

    public Object getRaw() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Value other)) return false;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return asString();
    }
}
