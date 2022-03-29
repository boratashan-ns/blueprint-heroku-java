package com.blueprints.heroku.services.eventstream;

import java.util.ArrayList;
import java.util.List;

public class Orders {
    private String orderId;
    private String orderExtId;
    private List<String> events = new ArrayList<>();
    private OrderStatus status;

    public String getOrderId() {
        return orderId;
    }

    public Orders setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getOrderExtId() {
        return orderExtId;
    }

    public Orders setOrderExtId(String orderExtId) {
        this.orderExtId = orderExtId;
        return this;
    }

    public List<String> getEvents() {
        return events;
    }

    public Orders setEvents(List<String> events) {
        this.events = events;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Orders setStatus(OrderStatus status) {
        this.status = status;
        return this;
    }
}
