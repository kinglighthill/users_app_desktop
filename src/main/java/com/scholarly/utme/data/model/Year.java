package com.scholarly.utme.data.model;

public class Year {

    private int id;
    private String year;
    private String shortDescription;
    private int isNew;
    private int isAvailable;
    private boolean free;

    public Year(int id, String year, String shortDescription, int isNew, int isAvailable) {
        this.id = id;
        this.year = year;
        this.shortDescription = shortDescription;
        this.isNew = isNew;
        this.isAvailable = isAvailable;
    }

    public Year(String year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(int isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        if (free)
            return year;
        else
            return year + " Locked";
    }
}
