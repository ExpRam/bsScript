package ru.expram.bsscript.environment;

import java.util.HashMap;

public class Environment {

    private Environment enclosing;
    private final HashMap<String, Object> variables;

    public Environment() {
        this.variables = new HashMap<>();
    }

    public boolean isVariableExists(String name) {
        if(variables.containsKey(name)) return true;
        if(enclosing == null) return false;
        return enclosing.isVariableExists(name);
    }

    public Object findVariable(String name) {
        if(variables.containsKey(name)) return variables.get(name);
        if(enclosing == null) return null;
        return enclosing.findVariable(name);
    }

    public void updateVariable(String name, Object value) {
        if(variables.containsKey(name)) {
            variables.put(name, value);
        }

        if(enclosing == null) return;
        enclosing.updateVariable(name, value);
    }

    public Environment getEnclosingEnvironment() {
        return enclosing;
    }

    public void setEnclosingEnvironment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }
}
