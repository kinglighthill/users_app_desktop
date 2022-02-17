package com.scholarly.utme.data.model.newDb.contentType.table;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

import java.util.List;

public class TableViewType extends ContentType {
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
