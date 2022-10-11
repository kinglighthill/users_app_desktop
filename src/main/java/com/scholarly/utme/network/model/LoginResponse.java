package com.scholarly.utme.network.model;

public class LoginResponse {
    private String status;
    private Data data;
    private String message;

    public LoginResponse(String status, Data data, String message) {
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
