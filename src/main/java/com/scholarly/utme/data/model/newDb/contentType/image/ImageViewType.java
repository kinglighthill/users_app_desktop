package com.scholarly.utme.data.model.newDb.contentType.image;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class ImageViewType extends ContentViewType {
    private Title title;
    private ImageBody body;

    public ImageViewType(Title title, ImageBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public ImageBody getBody() {
        return body;
    }
}
