package com.example.spring_reactive_data_demo.error;

import lombok.Data;

public class ProductNotFoundException extends Exception {
    private String details;
    public ProductNotFoundException(String message, String causeId) {
        super(message);
        details = "No product found for id: " + causeId;
    }

    public ProductNotFoundException(String causeId) {
        super("Product not found!");
        details = "No product found for id: " + causeId;
    }

    public ProductNotFoundException() {
        super("Product not found!");
        details = getClass().getName();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
