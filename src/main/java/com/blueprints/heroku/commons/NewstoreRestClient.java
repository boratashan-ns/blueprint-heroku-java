package com.blueprints.heroku.commons;




import kong.unirest.HttpResponse;

import java.util.Map;

public interface NewstoreRestClient {
    HttpResponse<String> post(String path, String query) throws RestClientException, InvalidCredentialsException;
    HttpResponse<String> get(String path, Map<String, Object> query, String body) throws InvalidCredentialsException, RestClientException;

}
