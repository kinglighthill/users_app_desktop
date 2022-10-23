package com.scholarly.utme.network.model;

public class AuthResponse {
    private String status;
    private Data data;
    private String message;

    public AuthResponse(String status, Data data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
