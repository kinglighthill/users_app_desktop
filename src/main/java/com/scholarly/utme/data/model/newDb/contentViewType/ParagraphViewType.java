package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class ParagraphViewType extends ContentViewType {
    private String text;

    public ParagraphViewType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
