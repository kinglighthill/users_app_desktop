package com.scholarly.utme.network.model.response;

public class UploadResponse {
    private String status;
    private String data;
    private String message;

    public UploadResponse(String status, String data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
