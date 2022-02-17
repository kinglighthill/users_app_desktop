package com.scholarly.utme.data.model;

public class Note {
    private int id;
    private String noteTableName;
    private int noteId;
    private String note;

    public Note(int id, String noteTableName, int noteId, String note) {
        this.id = id;
        this.noteTableName = noteTableName;
        this.noteId = noteId;
        this.note = note;
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

    public String getNote() {
        return note;
    }
}
