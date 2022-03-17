package com.blueprints.heroku.commons;

public enum NwsEnvironment {
    SANDBOX("x"),
    STAGING("s"),
    PRODUCTION("p");

    private final String env;

    NwsEnvironment(String env) {
        this.env = env;
    }

    public String getEnv() {
        return this.env;
    }

    public static NwsEnvironment fromString(String text) {
        for (NwsEnvironment n : NwsEnvironment.values()) {
            if (text.equalsIgnoreCase(n.getEnv())) {
                return n;
            }
        }
        throw new IllegalArgumentException(String.format("Can not convert string %s to enum %s",  text, NwsEnvironment.class.getName()));
    }
}
