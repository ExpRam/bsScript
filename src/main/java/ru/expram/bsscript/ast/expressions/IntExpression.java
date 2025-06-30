package ru.expram.bsscript.ast.expressions;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.value.Value;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.utils.ValueUtil;

public class IntExpression implements Expression {

    private final Integer value;

    public IntExpression(Integer value) {
        this.value = value;
    }

    @Override
    public Value execute(EnvironmentWrapper wrapper) {
        return ValueUtil.make(value);
    }
}
