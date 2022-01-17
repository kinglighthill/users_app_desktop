package com.scholarly.utme.data.model.newDb.contentType.orderedList;

public class OrderedListBody {
    private String numberType;
    private String[] list;

    public OrderedListBody(String numberType, String[] list) {
        this.numberType = numberType;
        this.list = list;
    }

    public String getNumberType() {
        return numberType;
    }

    public String[] getList() {
        return list;
    }
}
