package com.scholarly.utme.data.model.newDb.contentType.table;

import java.util.List;

public class TableBody {
    private String[] header;
    private String[][] rows;
    private String[] footer;

    public TableBody(String[] header, String[][] rows, String[] footer) {
        this.header = header;
        this.rows = rows;
        this.footer = footer;
    }

    public String[] getHeader() {
        return header;
    }

    public String[][] getRows() {
        return rows;
    }

    public String[] getFooter() {
        return footer;
    }
}
