package com.scholarly.utme.data.model.novels;

public class Novel {

    private int id;
    private String imagePath;
    private String name;
    private String summary;
    private String about;
    private int chaptersCount;
    private int genreId;
    private int categoryId;
    private int divisionId;
    private int position;
    private int isNew;
    private int available;
    private int creditId;

    public Novel() {

    }

    public Novel(int id, String imagePath, String name, String summary, String about, int chaptersCount, int genreId, int categoryId, int divisionId, int position, int isNew, int available, int creditId) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.summary = summary;
        this.about = about;
        this.chaptersCount = chaptersCount;
        this.genreId = genreId;
        this.categoryId = categoryId;
        this.divisionId = divisionId;
        this.position = position;
        this.isNew = isNew;
        this.available = available;
        this.creditId = creditId;
    }

    public int getId() {
        return id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getAbout() {
        return about;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public int getPosition() {
        return position;
    }

    public int getIsNew() {
        return isNew;
    }

    public int getAvailable() {
        return available;
    }

    public int getCreditId() {
        return creditId;
    }


}
