package com.scholarly.utme.data.model.newDb;

public class Section {
    private int id;
    private String content;
    private int parentSectionId;
    private int mainSectionOrder;
    private int childSectionOrder;
    private int contentViewTypeId;

    public Section(int id, String content, int parentSectionId, int mainSectionOrder, int childSectionOrder, int contentViewTypeId) {
        this.id = id;
        this.content = content;
        this.parentSectionId = parentSectionId;
        this.mainSectionOrder = mainSectionOrder;
        this.childSectionOrder = childSectionOrder;
        this.contentViewTypeId = contentViewTypeId;
    }

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public int getParentSectionId() {
        return parentSectionId;
    }

    public int getMainSectionOrder() {
        return mainSectionOrder;
    }

    public int getChildSectionOrder() {
        return childSectionOrder;
    }

    public int getContentViewTypeId() {
        return contentViewTypeId;
    }
}