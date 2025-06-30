package ru.expram.bsscript.environment;

public class EnvironmentWrapper {

    private Environment environment = new Environment();

    public Environment wrap() {
        Environment newEnvironment = new Environment();
        newEnvironment.setEnclosingEnvironment(environment);
        environment = newEnvironment;

        return newEnvironment;
    }

    public Environment unwrap() {
        environment = environment.getEnclosingEnvironment();
        return environment;
    }

    public Environment getCurrentEnvironment() {
        return environment;
    }
}
