package com.scholarly.utme.data.model.newDb.contentType.audio;

public class AudioBody {
    private String url;
    private int durationSeconds;

    public AudioBody(String url, int durationSeconds) {
        this.url = url;
        this.durationSeconds = durationSeconds;
    }

    public String getUrl() {
        return url;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }
}
