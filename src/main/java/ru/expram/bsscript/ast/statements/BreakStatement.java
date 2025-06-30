package ru.expram.bsscript.ast.statements;

import ru.expram.bsscript.ast.Statement;
import ru.expram.bsscript.ast.statements.exceptions.BreakException;
import ru.expram.bsscript.environment.EnvironmentWrapper;

public class BreakStatement implements Statement {

    @Override
    public void execute(EnvironmentWrapper wrapper) {
        throw new BreakException();
    }
}
