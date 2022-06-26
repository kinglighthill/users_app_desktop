package com.scholarly.utme.data.model.newDb.contentType.tablehh;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class TableHHViewType extends ContentViewType {
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
