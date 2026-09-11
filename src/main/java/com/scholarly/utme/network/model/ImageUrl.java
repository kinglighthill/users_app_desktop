package com.scholarly.utme.network.model;

public class ImageUrl {
    private String type;
    private byte[] data;

    public ImageUrl(String type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public byte[] getData() {
        return data;
    }
}
