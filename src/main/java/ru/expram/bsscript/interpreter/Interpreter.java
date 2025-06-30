package ru.expram.bsscript.interpreter;

import ru.expram.bsscript.ast.statements.RootStatement;
import ru.expram.bsscript.environment.EnvironmentWrapper;


public class Interpreter {

    public void execute(RootStatement ast) {
        ast.execute(new EnvironmentWrapper());
    }
}
