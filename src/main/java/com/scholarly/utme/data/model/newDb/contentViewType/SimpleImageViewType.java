package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class SimpleImageViewType extends ContentViewType {
    private String url;
    private String caption;

    public SimpleImageViewType(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }
}
