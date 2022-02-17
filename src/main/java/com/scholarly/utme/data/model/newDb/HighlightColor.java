package com.scholarly.utme.data.model.newDb;

public class HighlightColor {
    private int id;
    private String colorCode;

    public HighlightColor(int id, String colorCode) {
        this.id = id;
        this.colorCode = colorCode;
    }

    public int getId() {
        return id;
    }

    public String getColorCode() {
        return colorCode;
    }
}
