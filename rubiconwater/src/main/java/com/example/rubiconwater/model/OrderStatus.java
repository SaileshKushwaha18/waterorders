package com.example.rubiconwater.model;

public enum OrderStatus {
    REQUESTED("Order has been placed but not yet delivered"),
    INPROGRESS("Order is being delivered right now"),
    DELIVERED("Order has been delivered"),
    CANCELLED("Order was cancelled before delivery");

    private String value;

    private OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
