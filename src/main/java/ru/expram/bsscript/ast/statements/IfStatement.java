package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.environment.EnvironmentWrapper;

import java.util.List;

public class IfStatement implements Statement {

    private Expression condition;
    private List<Statement> thenStmt;
    private List<Statement> elseStmt;

    public IfStatement(Expression condition, List<Statement> thenStmt, List<Statement> elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getThenStmt() {
        return thenStmt;
    }

    public List<Statement> getElseStmt() {
        return elseStmt;
    }

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        if(condition.execute(wrapper).asBoolean()) {
            wrapper.wrap();
            thenStmt.forEach(stmt -> stmt.execute(wrapper));
            wrapper.unwrap();
        } else {
            wrapper.wrap();
            elseStmt.forEach(stmt -> stmt.execute(wrapper));
            wrapper.unwrap();
        }
    }
}
