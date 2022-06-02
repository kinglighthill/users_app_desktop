package com.scholarly.utme.data.model.novels;

public class Novel {

    private int id;
    private String imagePath;
    private String name;
    private String summary;
    private String about;
    private int chaptersCount;
    private int genreId;
    private int typeId;
    private int divisionId;
    private int position;
    private int isNew;
    private int available;
    private int creditId;

    public Novel() {

    }

    public Novel(int id, String imagePath, String name, String summary, String about, int chaptersCount, int genreId, int typeId, int divisionId, int position, int isNew, int available, int creditId) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
        this.summary = summary;
        this.about = about;
        this.chaptersCount = chaptersCount;
        this.genreId = genreId;
        this.typeId = typeId;
        this.divisionId = divisionId;
        this.position = position;
        this.isNew = isNew;
        this.available = available;
        this.creditId = creditId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getCreditId() {
        return creditId;
    }

    public void setCreditId(int creditId) {
        this.creditId = creditId;
    }


    public enum Type {
        JAMB(1),
        AFRICAN(2),
        NON_AFRICAN(3),
        SHAKESPEAREAN(4);

        private int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum Genre {
        PROSE(1),
        DRAMA(2),
        TEXT(3),
        POETRY(4);

        private int id;

        Genre(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
