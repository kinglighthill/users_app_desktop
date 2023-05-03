package com.scholarly.utme.data.model.newDb.contentViewType;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

import java.util.List;

public class TableViewType extends ContentViewType {
    private List<List<String>> content;
    private boolean withHeadings;

    public TableViewType(List<List<String>> content, boolean withHeadings) {
        this.content = content;
        this.withHeadings = withHeadings;
    }

    public List<List<String>> getContent() {
        return content;
    }

    public boolean isWithHeadings() {
        return withHeadings;
    }
}
