package com.scholarly.utme.data.model;

public class Highlights {
    private int id;
    private String noteTableName;
    private int noteId;
    private String color;


    public Highlights(int id, String noteTableName, int noteId, String color) {
        this.id = id;
        this.noteTableName = noteTableName;
        this.noteId = noteId;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getNoteTableName() {
        return noteTableName;
    }

    public int getNoteId() {
        return noteId;
    }

    public String getColor() {
        return color;
    }
}
