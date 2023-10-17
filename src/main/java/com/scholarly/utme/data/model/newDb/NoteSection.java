package com.scholarly.utme.data.model.newDb;

import org.json.JSONObject;

public class NoteSection {
    private int id;
    private int subjectId;
    private int topicId;
    private int subtopicId;
    private String content;
    private int parentSectionId;
    private int mainSectionOrder;
    private int childSectionOrder;
    private int contentViewTypeId;

    private JSONObject contentJson;

    public NoteSection(int id, int subjectId, int topicId, int subtopicId, String content, int parentSectionId, int mainSectionOrder, int childSectionOrder, int contentViewTypeId, JSONObject contentJson) {
        this.id = id;
        this.subjectId = subjectId;
        this.topicId = topicId;
        this.subtopicId = subtopicId;
        this.content = content;
        this.parentSectionId = parentSectionId;
        this.mainSectionOrder = mainSectionOrder;
        this.childSectionOrder = childSectionOrder;
        this.contentViewTypeId = contentViewTypeId;
        this.contentJson = contentJson;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getSubtopicId() {
        return subtopicId;
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

    public JSONObject getContentJson() {
        return contentJson;
    }
}
