package com.blueprints.heroku.commons;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SimpleQueueMessage {
    private String caption;
    private String message;

    public SimpleQueueMessage(String caption, String message) {
        this.caption = caption;
        this.message = message;
    }

    public String getCaption() {
        return caption;
    }

    public SimpleQueueMessage setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SimpleQueueMessage setMessage(String message) {
        this.message = message;
        return this;
    }

    public String convertToJsonString() {
        JsonObject object = new JsonObject();
        object.addProperty("caption", this.getCaption());
        object.addProperty("message", this.message);
        return object.toString();
    }

    public static SimpleQueueMessage parse(String input) {
        JsonElement element = JsonParser.parseString(input);
        JsonObject object =  element.getAsJsonObject();
        String caption = object.get("caption").getAsString();
        String message = object.get("message").getAsString();
        return new SimpleQueueMessage(caption, message);
    }


}
