package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Expression;
import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.ast.statements.exceptions.BreakException;
import ru.expram.bsscript.environment.EnvironmentWrapper;

import java.util.List;

public class WhileStatement implements Statement {

    private Expression condition;
    private List<Statement> body;

    public WhileStatement(Expression condition, List<Statement> body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public List<Statement> getBody() {
        return body;
    }

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        while(condition.execute(wrapper).asBoolean()) {
            try {
                wrapper.wrap();
                body.forEach(stmt -> stmt.execute(wrapper));
                wrapper.unwrap();
            } catch (BreakException e) {
                break;
            }
        }
    }
}
