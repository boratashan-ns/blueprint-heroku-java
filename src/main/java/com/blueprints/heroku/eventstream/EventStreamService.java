package com.blueprints.heroku.eventstream;

import com.blueprints.heroku.commons.InvalidCredentialsException;
import com.blueprints.heroku.commons.NewstoreRestClient;
import com.blueprints.heroku.commons.RestClientException;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.List;

@Service
public class EventStreamService {
    private final static String ENDPOINT_GET_INTEGRATION_DETAILS = "/api/v1/org/integrations/eventstream/%s";
    private final static String ENDPOINT_INTEGRATION_START = "/api/v1/org/integrations/eventstream/%s/_start";
    private final static String ENDPOINT_INTEGRATION_STOP = "/api/v1/org/integrations/eventstream/%s/_stop";
    private final static String ENDPOINT_INTEGRATION_REGISTER = "/api/v1/org/integrations/eventstream";





    @Autowired
    private NewstoreRestClient apiClient;


    public HttpResponse<String> registerNewIntegration(String integrationId, String callbackUrl, String apiKey) throws InvalidCredentialsException, RestClientException {
        Instant instant =Instant.now().minusSeconds(60*60*24);
        String startsAt = instant.toString();
        String payload = "{\n" +
                "\"id\": \"%s\",\n" +
                "\"integration_type\": \"permanent\",\n" +
                "\"starts_at\": \"%s\",\n" +
                "\"callback_parameters\": {\n" +
                "\"callback_url\": \"%s\",\n" +
                "\"api_key\": \"%s\"\n" +
                "}\n" +
                "}";
        payload = String.format(payload,  integrationId, startsAt, callbackUrl, apiKey);
        String path = ENDPOINT_INTEGRATION_REGISTER;
        return apiClient.post(path, payload);
    }


    public HttpResponse<String> startTheIntegration(String integrationId) throws InvalidCredentialsException, RestClientException {
        String path = String.format(ENDPOINT_INTEGRATION_START, integrationId);
        return apiClient.post(path, "");
    }

    public HttpResponse<String> stopTheIntegration(String integrationId) throws InvalidCredentialsException, RestClientException {
        String path = String.format(ENDPOINT_INTEGRATION_STOP, integrationId);
        return apiClient.post(path, "");
    }

    public HttpResponse<String> getIntegrationDetails(String integrationId) throws InvalidCredentialsException, RestClientException {
        String path = String.format(ENDPOINT_GET_INTEGRATION_DETAILS, integrationId);
        return apiClient.get(path, null, null);
        //return  result.map(s -> JsonParser.parseString(result.getBody().toString()).getAsJsonObject());
        //return JsonParser.parseString(result.toString()).getAsJsonObject();
    }
}
