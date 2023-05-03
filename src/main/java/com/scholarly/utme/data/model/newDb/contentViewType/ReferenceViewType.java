package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class ReferenceViewType extends ContentViewType {
    private String text;

    public ReferenceViewType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
