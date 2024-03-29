package com.scholarly.data.model.newDb.contentType.video;

public class VideoBody {
    private String thumbnailUrl;
    private String url;
    private int widthPx;
    private int heightPx;
    private int durationSeconds;

    public VideoBody(String thumbnailUrl, String url, int widthPx, int heightPx, int durationSeconds) {
        this.thumbnailUrl = thumbnailUrl;
        this.url = url;
        this.widthPx = widthPx;
        this.heightPx = heightPx;
        this.durationSeconds = durationSeconds;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
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

    public int getDurationSeconds() {
        return durationSeconds;
    }
}
