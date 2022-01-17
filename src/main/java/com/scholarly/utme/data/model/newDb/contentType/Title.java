package com.scholarly.utme.data.model.newDb.contentType;

public class Title {
    private String text;
    private int level;

    public Title(String text, int level) {
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
