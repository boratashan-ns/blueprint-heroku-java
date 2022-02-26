package com.blueprints.heroku.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataClient {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int doSomething() {
        int result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM public.breadboard", Integer.class);
        return result;
    }

    public String getAppInfo() {
        String result =  jdbcTemplate.queryForObject("SELECT appinfo from appinfo FETCH FIRST 1 ROW ONLY;", String.class);
        return result;
    }
}

