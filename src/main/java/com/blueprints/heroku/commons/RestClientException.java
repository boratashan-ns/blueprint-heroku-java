package com.blueprints.heroku.commons;

import kong.unirest.UnirestException;

public class RestClientException extends Exception {
    public RestClientException(UnirestException e) {
        super(e);
    }

    public RestClientException(String message) {
        super(message);
    }
}
