package com.scholarly.utme.data.model.newDb.contentType.tablehh;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class TableHHViewType extends ContentType {
    private Title title;
    private TableHHBody body;

    public TableHHViewType(Title title, TableHHBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public TableHHBody getBody() {
        return body;
    }
}
