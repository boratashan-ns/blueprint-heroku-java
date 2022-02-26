package com.blueprints.heroku.configuration;

import org.springframework.stereotype.Component;

public class ApplicationInfoProvider {

    public String getInfo() {
        return "This is blueprint app runs on Heroku.";
    }
}
