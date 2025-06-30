package ru.expram.bsscript.ast;

import ru.expram.bsscript.environment.EnvironmentWrapper;
import ru.expram.bsscript.value.Value;

public interface Expression {

    Value execute(EnvironmentWrapper wrapper);
}
