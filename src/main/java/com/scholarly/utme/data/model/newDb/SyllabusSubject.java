package com.scholarly.utme.data.model.newDb;

public class SyllabusSubject {
    private int id;
    private String generalObjectives;
    private String recommendedTexts;
    private int subjectId;
    private String title;
    private int order;

    public SyllabusSubject(int id, String generalObjectives, String recommendedTexts, int subjectId, String title, int order) {
        this.id = id;
        this.generalObjectives = generalObjectives;
        this.recommendedTexts = recommendedTexts;
        this.subjectId = subjectId;
        this.title = title;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getGeneralObjectives() {
        return generalObjectives;
    }

    public String getTitle() {
        return title;
    }

    public String getRecommendedTexts() {
        return recommendedTexts;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getOrder() {
        return order;
    }
}
