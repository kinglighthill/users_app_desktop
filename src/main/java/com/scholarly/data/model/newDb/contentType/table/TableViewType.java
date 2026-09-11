package com.scholarly.data.model.newDb.contentType.table;

import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentType.Title;

public class TableViewType extends ContentViewType {
    private Title title;
    private TableBody body;

    public TableViewType(Title title, TableBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public TableBody getBody() {
        return body;
    }
}
