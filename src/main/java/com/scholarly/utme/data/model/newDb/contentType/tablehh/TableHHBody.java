package com.scholarly.utme.data.model.newDb.contentType.tablehh;

import java.util.List;
import java.util.Map;

public class TableHHBody {
    private String[] header;
    private List<Map<String, List<String>>> rows;
    private Map<String, List<String>> footer;

    public TableHHBody(String[] header, List<Map<String, List<String>>> rows,  Map<String, List<String>> footer) {
        this.header = header;
        this.rows = rows;
        this.footer = footer;
    }

    public String[] getHeader() {
        return header;
    }

    public List<Map<String, List<String>>> getRows() {
        return rows;
    }

    public Map<String, List<String>> getFooter() {
        return footer;
    }

}
