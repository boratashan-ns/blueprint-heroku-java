package com.blueprints.heroku.commons;

public class Credential {
    public String userName;
    public String password;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Credential(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }


}
