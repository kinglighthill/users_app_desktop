package com.scholarly.utme.data.model.newDb;

public class SyllabusSection {
    private int id;
    private int subjectId;
    private String content;
    private String objectives;
    private int parentSectionId;
    private int mainSectionOrder;
    private int childSectionOrder;

    public SyllabusSection(int id, int subjectId, String content, String objectives, int parentSectionId, int mainSectionOrder, int childSectionOrder) {
        this.id = id;
        this.subjectId = subjectId;
        this.content = content;
        this.objectives = objectives;
        this.parentSectionId = parentSectionId;
        this.mainSectionOrder = mainSectionOrder;
        this.childSectionOrder = childSectionOrder;
    }

    public int getId() {
        return id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getContent() {
        return content;
    }

    public String getObjectives() {
        return objectives;
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
}
