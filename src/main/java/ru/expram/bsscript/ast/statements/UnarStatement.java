package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.token.Token;

public class UnarStatement implements Statement {

    private final Token operator;
    private final Expression operand;

    public UnarStatement(Token operator, Expression operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getOperand() {
        return operand;
    }

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        switch (operator.getTokenType()) {
            case PRINT -> System.out.println(operand.execute(wrapper).getRaw());
            case INPUT -> operand.execute(wrapper);
        }
    }
}
