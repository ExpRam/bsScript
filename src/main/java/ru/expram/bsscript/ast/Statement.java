package ru.expram.bsscript.ast;

import ru.expram.bsscript.environment.EnvironmentWrapper;

public interface Statement {

    void execute(EnvironmentWrapper wrapper);
}
