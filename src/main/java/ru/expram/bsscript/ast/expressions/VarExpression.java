package ru.expram.bsscript.ast.expressions;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.value.Value;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.token.Token;

import static ru.expram.bsscript.utils.ValueUtil.make;

public class VarExpression implements Expression {

    private final Token variable;

    public VarExpression(Token variable) {
        this.variable = variable;
    }

    @Override
    public Value execute(EnvironmentWrapper wrapper) {
        return (Value) wrapper.getCurrentEnvironment().findVariable(variable.getValue());
    }

    public Token getVariable() {
        return variable;
    }

}
