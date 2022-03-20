package com.blueprints.heroku.eventstream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.sql.Timestamp;
import java.time.Instant;


public class Event {

    private JsonObject payload;

    private String name;

    private Timestamp publishedAt;

    private String tenant;

    public static Event parse(String input) {
        Event event = new Event();
        JsonElement jsonElement = JsonParser.parseString(input);
        String tenant = jsonElement.getAsJsonObject().get("tenant").getAsString();
        String name = jsonElement.getAsJsonObject().get("name").getAsString();
        String timestamp = jsonElement.getAsJsonObject().get("published_at").getAsString();
        Timestamp published_at = Timestamp.from(Instant.parse(timestamp));
        //String id = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("id").getAsString();
        //UUID eventId = UUID.fromString(id);
        JsonObject pload = jsonElement.getAsJsonObject();
        event.setName(name);
        event.setTenant(tenant);
        event.setPublishedAt(published_at);
        event.setPayload(pload);
        return event;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public void setPayload(JsonObject payload) {
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Timestamp publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

}