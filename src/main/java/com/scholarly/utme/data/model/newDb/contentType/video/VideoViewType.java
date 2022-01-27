package com.scholarly.utme.data.model.newDb.contentType.video;

import com.scholarly.utme.data.model.newDb.contentType.Title;

public class VideoViewType {
    private Title title;
    private VideoBody body;

    public VideoViewType(Title title, VideoBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public VideoBody getBody() {
        return body;
    }
}
