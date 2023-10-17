package com.scholarly.utme.data.model.newDb;

public class NoteLastSession {
    private int id;
    private int sectionId;
    private String sectionTitle;
    private String userId;

    public NoteLastSession(int id, int sectionId, String sectionTitle, String userId) {
        this.id = id;
        this.sectionId = sectionId;
        this.sectionTitle = sectionTitle;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
