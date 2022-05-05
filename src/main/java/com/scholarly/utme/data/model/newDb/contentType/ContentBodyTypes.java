package com.scholarly.utme.data.model.newDb.contentType;

public enum ContentBodyTypes {
    EXAMPLE(1, "example"),
    EQUATION(2, "equation"),
    CALCULATION(3, "calculation"),
    QUOTE(4, "quote"),
    DEFINITION(5, "definition"),
    DEEP_LINK(6, "deep_link"),
    HYPERLINK(7, "hyperlink"),
    DYK(8, "dyk"),
    REFERENCE(9, "reference");


    private int id;
    private String type;

    ContentBodyTypes(int id, String type) {
        this.id = id;
        this.type = type;
    }
}
