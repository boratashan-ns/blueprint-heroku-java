package com.blueprints.heroku.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class ApplicationInfoProvider {


    @Value("${appinfo.appname}")
    private String appname;


    public String getInfo() {
        return "This is blueprint app runs on Heroku. ";
    }

    public String getAppName() {
        return appname;
    }
}
