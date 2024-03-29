package com.scholarly.data.model.newDb.contentViewType;

import com.scholarly.data.model.newDb.contentType.ContentViewType;

public class ParagraphViewType extends ContentViewType {
    private String text;

    public ParagraphViewType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
