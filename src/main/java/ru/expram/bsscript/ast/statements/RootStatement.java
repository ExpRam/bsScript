package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.environment.EnvironmentWrapper;

import java.util.ArrayList;
import java.util.List;

public class RootStatement implements Statement {

    private List<Statement> statements = new ArrayList<>();

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        statements.forEach(statement -> {
            statement.execute(wrapper);
        });
    }
}
