package com.scholarly.network.model.response;

import com.scholarly.network.model.Data;

public class BaseResponse {
    private String status;
    private Data data;
    private String message;

    public BaseResponse(String status, Data data, String message) {
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