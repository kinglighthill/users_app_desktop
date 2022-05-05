package com.scholarly.utme.data.model.newDb.contentType.audio;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class AudioViewType extends ContentViewType {
    private Title title;
    private AudioBody body;

    public AudioViewType(Title title, AudioBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public AudioBody getBody() {
        return body;
    }
}
