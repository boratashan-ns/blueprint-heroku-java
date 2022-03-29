package com.blueprints.heroku.services.eventstream;

public enum EventEntity {
    ORDER("order"),
    INVENTORY("inventory"),
    UNKNOWN("");
    private final String entity;

    EventEntity(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return this.entity;
    }

    public static EventEntity from(String eventName) {
        String name = eventName.substring(0, eventName.indexOf('.'));
        for (EventEntity et : EventEntity.values()) {
            if (et.getEntity().equalsIgnoreCase(name)) {
                return et;
            }
        }
        return EventEntity.UNKNOWN;
    }


}
