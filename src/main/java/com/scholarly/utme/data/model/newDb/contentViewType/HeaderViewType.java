package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class HeaderViewType extends ContentViewType {
    private String text;
    private int level;

    public HeaderViewType(String text, int level) {
        this.text = text;
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public int getLevel() {
        return level;
    }
}
