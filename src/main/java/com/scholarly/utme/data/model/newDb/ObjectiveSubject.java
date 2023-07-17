package com.scholarly.utme.data.model.newDb;

public class ObjectiveSubject extends PQSubject {

    private String description;


    public ObjectiveSubject(int id, int subjectId, int minutesAllotted, int order, String title, String shortTitle, String colorCode, String description) {
        super(id, subjectId, minutesAllotted, order, title, shortTitle, colorCode);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
