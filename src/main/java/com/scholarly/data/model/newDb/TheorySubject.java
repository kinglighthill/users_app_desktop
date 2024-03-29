package com.scholarly.data.model.newDb;

public class TheorySubject extends PQSubject {
    private String description;

    public TheorySubject(int id, int subjectId, int minutesAllotted, int order, String title, String shortTitle, String colorCode, String description) {
        super(id, subjectId, minutesAllotted, order, title, shortTitle, colorCode, false);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
