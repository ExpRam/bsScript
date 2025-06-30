package ru.expram.bsscript.ast.expressions;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.value.Value;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.utils.ValueUtil;

public class StringExpression implements Expression {

    private final String value;

    public StringExpression(String value) {
        this.value = value;
    }

    @Override
    public Value execute(EnvironmentWrapper wrapper) {
        return ValueUtil.make(value.substring(1, value.length() - 1));
    }
}
