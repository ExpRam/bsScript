package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.ast.expressions.VarExpression;
import ru.expram.bsscript.environment.Environment;
import ru.expram.bsscript.environment.EnvironmentWrapper;

public class AssignStatement implements Statement {

    private final Expression leftExpr, rightExpr;

    public AssignStatement(Expression leftExpr, Expression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    public Expression getLeftExpr() {
        return leftExpr;
    }

    public Expression getRightExpr() {
        return rightExpr;
    }

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        VarExpression varExpression = (VarExpression) leftExpr;
        String varName = varExpression.getVariable().getValue();

        Environment env = wrapper.getCurrentEnvironment();

        if(env.isVariableExists(varName)) {
            env.updateVariable(varName, rightExpr.execute(wrapper));
            return;
        }

        env.getVariables().put(
                varName,
                rightExpr.execute(wrapper)
        );
    }
}
