package com.scholarly.utme.data.model.newDb;

public class SyllabusTopic {
    private int id;
    private String generalObjectives;
    private String recommendedTexts;
    private int subjectId;
    private int order;

    public SyllabusTopic(int id, String generalObjectives, String recommendedTexts, int subjectId, int order) {
        this.id = id;
        this.generalObjectives = generalObjectives;
        this.recommendedTexts = recommendedTexts;
        this.subjectId = subjectId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public String getGeneralObjectives() {
        return generalObjectives;
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
