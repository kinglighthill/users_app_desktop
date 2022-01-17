package com.scholarly.utme.data.model.newDb.contentType.image;

public class ImageBody {
    private String url;
    private int widthPx;
    private int heightPx;

    public ImageBody(String url, int widthPx, int heightPx) {
        this.url = url;
        this.widthPx = widthPx;
        this.heightPx = heightPx;
    }

    public String getUrl() {
        return url;
    }

    public int getWidthPx() {
        return widthPx;
    }

    public int getHeightPx() {
        return heightPx;
    }
}
