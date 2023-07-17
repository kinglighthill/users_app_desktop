package com.scholarly.utme.data.model.newDb;

public class FavoriteSubject extends ObjectiveSubject {
    private boolean selected;

    public FavoriteSubject(int id, int subjectId, int minutesAllotted, int order, String title, String shortTitle, String colorCode, String description, boolean selected) {
        super(id, subjectId, minutesAllotted, order, title, shortTitle, colorCode, description);
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
