package ru.expram.bsscript.ast.expressions;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.value.Value;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.token.Token;

import java.util.Scanner;

import static ru.expram.bsscript.utils.ValueUtil.make;

public class UnarExpression implements Expression {

    private final Token operator;
    private final Expression operand;

    public UnarExpression(Token operator, Expression operand) {
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
    public Value execute(EnvironmentWrapper wrapper) {
        Value result = operand.execute(wrapper);

        return switch (operator.getTokenType()) {
            case MINUS -> {
                if(result.isFloat()) {
                    yield make(-result.asFloat());
                }

                if(result.isInteger()) {
                    yield make(result.asInteger());
                }

                yield Value.NULL;
            }
            case NOT -> {
                if(result.isBoolean()) {
                    yield make(!result.asBoolean());
                }

                yield Value.NULL;
            }
            case INPUT -> {
                Scanner scanner = new Scanner(System.in);
                System.out.println(result.getRaw());
                String scanResult = scanner.nextLine();
                yield make(scanResult);
            }
            default -> null;
        };
    }
}
