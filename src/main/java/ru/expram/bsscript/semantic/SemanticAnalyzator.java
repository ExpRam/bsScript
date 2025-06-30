package ru.expram.bsscript.semantic;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.ast.expressions.*;
import ru.expram.bsscript.ast.statements.*;
import ru.expram.bsscript.environment.Environment;
import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.token.TokenType;
import ru.expram.bsscript.value.ValueType;

import java.util.List;

import static ru.expram.bsscript.utils.ErrorUtil.error;

public class SemanticAnalyzator {

    private EnvironmentWrapper wrapper = new EnvironmentWrapper();
    
    public void analyze(RootStatement ast) {
        ast.getStatements().forEach(this::analyzeStatement);
    }

    public void analyzeStatement(Statement statement) {
        Environment tempEnv = wrapper.getCurrentEnvironment();

        if(statement instanceof AssignStatement assign) {
            VarExpression var = (VarExpression) assign.getLeftExpr();
            String varName = var.getVariable().getValue();;

            Expression value = assign.getRightExpr();
            ValueType valueType = analyzeExpression(value);

            if(!tempEnv.isVariableExists(varName)) {
                tempEnv.getVariables().put(varName, valueType);
            }
        } else if(statement instanceof IfStatement ifs) {
            Expression condition = ifs.getCondition();
            ValueType valueType = analyzeExpression(condition);
            if(valueType != ValueType.BOOLEAN) {
                throw error("Condition of if statement must be boolean, but got %s".formatted(valueType));
            }

            parseBlock(ifs.getThenStmt());
            parseBlock(ifs.getElseStmt());

        } else if(statement instanceof WhileStatement whiles) {
            Expression condition = whiles.getCondition();
            ValueType valueType = analyzeExpression(condition);
            if(valueType != ValueType.BOOLEAN) {
                throw error("Condition of if statement must be boolean, but got %s".formatted(valueType));
            }

            parseBlock(whiles.getBody());
        } else if(statement instanceof UnarStatement unar) {
            analyzeExpression(unar.getOperand());
        }
        // throw error("WHAT THE FUCK IS A KILOMETER????!???");
    }

    private ValueType analyzeExpression(Expression expression) {
        Environment tempEnv = wrapper.getCurrentEnvironment();

        if(expression instanceof StringExpression) return ValueType.STRING;
        if(expression instanceof IntExpression) return ValueType.INTEGER;
        if(expression instanceof FloatExpression) return ValueType.FLOAT;
        if(expression instanceof BooleanExpression) return ValueType.BOOLEAN;
        if(expression instanceof VarExpression var) {
            String varName = var.getVariable().getValue();
            Object varValue = tempEnv.findVariable(varName);
            if(varValue == null) {
                throw error("Cannot find variable %s".formatted(varName));
            }

            return (ValueType) varValue;
        } else if(expression instanceof BinExpression bin) {
            ValueType leftType = analyzeExpression(bin.getLeft());
            ValueType rightType = analyzeExpression(bin.getRight());
            TokenType operator = bin.getOperator().getTokenType();

            switch (operator) {
                case PLUS, MINUS, MULTIPLY, DIVIDE:
                    if((leftType == ValueType.STRING || rightType == ValueType.STRING) && operator == TokenType.PLUS)
                        return ValueType.STRING;

                    ValueType mathType = analyzeNumericLiteral(leftType, rightType);
                    if(mathType != null)
                        return mathType;
                    break;
                case GT, LT, GTOET, LTOET:
                    ValueType compType = analyzeNumericLiteral(leftType, rightType);
                    if(compType != null)
                        return ValueType.BOOLEAN;
                    break;
                case EQUALS, NOT_EQUALS:
                    if(leftType == rightType) {
                        return ValueType.BOOLEAN;
                    }
                    break;
                case AND, OR:
                    if(leftType == rightType && leftType == ValueType.BOOLEAN)
                        return ValueType.BOOLEAN;
                    break;
            }
            throw error("Invalid operator %s for types %s and %s".formatted(operator, leftType, rightType));
        } else if(expression instanceof UnarExpression unar) {

            TokenType operator = unar.getOperator().getTokenType();
            ValueType operand = analyzeExpression(unar.getOperand());
            switch (operator) {
                case MINUS:
                    if(operand == ValueType.INTEGER || operand == ValueType.FLOAT)
                        return operand;
                    break;
                case NOT:
                    if(operand == ValueType.BOOLEAN)
                        return operand;
                case INPUT:
                    if(operand == ValueType.STRING)
                        return operand;
                    break;
            }

            throw error("Invalid unary operator %s for type %s".formatted(operator, operand));
        }
        throw error("Unknown expression type: %s".formatted(expression.getClass().getSimpleName()));
    }

    private ValueType analyzeNumericLiteral(ValueType leftType, ValueType rightType) {
        if((leftType == ValueType.INTEGER || leftType == ValueType.FLOAT)
                && (rightType == ValueType.INTEGER || rightType == ValueType.FLOAT)) {
            return leftType == ValueType.FLOAT || rightType == ValueType.FLOAT ? ValueType.FLOAT : ValueType.INTEGER;
        }
        return null;
    }

    private void parseBlock(List<Statement> statements) {
        wrapper.wrap();
        statements.forEach(this::analyzeStatement);
        wrapper.unwrap();
    }

}
