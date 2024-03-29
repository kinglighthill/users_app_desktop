package com.scholarly.data.model.newDb.contentViewType;

import com.scholarly.data.model.newDb.contentType.ContentViewType;

import java.util.List;

public class ListViewType extends ContentViewType {
    private String style;
    private List<String> items;

    public ListViewType(String style, List<String> items) {
        this.style = style;
        this.items = items;
    }

    public String getStyle() {
        return style;
    }

    public List<String> getItems() {
        return items;
    }
}
