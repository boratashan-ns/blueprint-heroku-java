package com.blueprints.heroku.eventstream;

public enum OrderStatus {
    INPROCESS("inprocess"),
    COMPLETED("completed");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
