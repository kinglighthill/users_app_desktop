package com.scholarly.data.model;

public class SubjectTheory {

    private int id;
    private String tableName;
    private String subjectName;
    private int timeAllotted;
    private String subjectDescription;
    private String shortDescription;
    private String subjectColor;
    private String colorName;

    public SubjectTheory(int id, String tableName, String subjectName, int timeAllotted, String subjectDescription, String shortDescription, String subjectColor, String colorName) {
        this.id = id;
        this.tableName = tableName;
        this.subjectName = subjectName;
        this.timeAllotted = timeAllotted;
        this.subjectDescription = subjectDescription;
        this.shortDescription = shortDescription;
        this.subjectColor = subjectColor;
        this.colorName = colorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTimeAllotted() {
        return timeAllotted;
    }

    public void setTimeAllotted(int timeAllotted) {
        this.timeAllotted = timeAllotted;
    }

    public String getSubjectDescription() {
        return subjectDescription;
    }

    public void setSubjectDescription(String subjectDescription) {
        this.subjectDescription = subjectDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSubjectColor() {
        return subjectColor;
    }

    public void setSubjectColor(String subjectColor) {
        this.subjectColor = subjectColor;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
}
