package ru.expram.bsscript.ast.expressions;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.value.Value;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.token.Token;
import ru.expram.bsscript.token.TokenType;

import static ru.expram.bsscript.utils.ValueUtil.*;

public class BinExpression implements Expression {

    private final Token operator;
    private final Expression left;
    private final Expression right;

    public BinExpression(Token operator, Expression left, Expression right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    public Token getOperator() {
        return operator;
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    @Override
    public Value execute(EnvironmentWrapper wrapper) {
        Value leftVal = left.execute(wrapper);
        Value rightVal = right.execute(wrapper);

        TokenType type = operator.getTokenType();

        return switch (type) {
            case PLUS -> {
                if (leftVal.isNumber() && rightVal.isNumber()) {
                    yield wrapNumber(leftVal, rightVal, Float::sum);
                } else if (leftVal.isString() || rightVal.isString()) {
                    yield make(leftVal.asString() + rightVal.asString());
                }
                yield Value.NULL;
            }
            case MINUS -> wrapNumber(leftVal, rightVal, (a, b) -> a - b);
            case MULTIPLY -> wrapNumber(leftVal, rightVal, (a, b) -> a * b);
            case DIVIDE -> wrapNumber(leftVal, rightVal, (a, b) -> a / b);
            case EQUALS -> make(leftVal.equals(rightVal));
            case NOT_EQUALS -> make(!leftVal.equals(rightVal));
            case GT -> wrapComparison(leftVal, rightVal, (a, b) -> a > b);
            case LT -> wrapComparison(leftVal, rightVal, (a, b) -> a < b);
            case GTOET -> wrapComparison(leftVal, rightVal, (a, b) -> a >= b);
            case LTOET -> wrapComparison(leftVal, rightVal, (a, b) -> a <= b);
            case AND -> make(leftVal.asBoolean() && rightVal.asBoolean());
            case OR -> make(leftVal.asBoolean() || rightVal.asBoolean());
            default -> Value.NULL;
        };
    }

    private Value wrapNumber(Value l, Value r, FloatBinaryOp op) {
        if(l.isFloat() || r.isFloat()) {
            return make(op.apply(l.asFloat(), r.asFloat()));
        }
        return make((int) op.apply(l.asInteger(), r.asInteger()));
    }

    private Value wrapComparison(Value l, Value r, FloatComparisonOp op) {
        return make(op.apply(l.asFloat(), r.asFloat()));
    }

    interface FloatBinaryOp {
        float apply(float a, float b);
    }

    interface FloatComparisonOp {
        boolean apply(float a, float b);
    }
}
