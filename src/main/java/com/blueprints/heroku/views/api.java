package com.blueprints.heroku.views;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class api {
    @GetMapping("/hello")
    public String hello() {
        return "Hello...";
    }
}
