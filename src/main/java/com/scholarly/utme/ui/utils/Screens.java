package com.scholarly.utme.ui.utils;

public enum Screens {

    HOME_SCREEN("HOME_SCREEN"),
    ACCOUNT_SCREEN("ACCOUNT_SCREEN"),
    ACTIVATE_SCREEN("ACTIVATE_SCREEN"),
    APPS_SCREEN("APPS_SCREEN"),
    SETTINGS_SCREEN("SETTINGS_SCREEN"),
    PRACTICE_SCREEN("PRACTICE_SCREEN"),
    PAST_QUESTION_SCREEN("PAST_QUESTION_SCREEN"),
    CBT_GAME_SCREEN("CBT_GAME_SCREEN"),
    NOVELS_SCREEN("NOVELS_SCREEN"),
    NOTES_SCREEN("NOTES_SCREEN"),
    SYLLABUS_SCREEN("SYLLABUS_SCREEN"),
    VIDEOS_SCREEN("VIDEOS_SCREEN"),
    AUDIOS_SCREEN("AUDIOS_SCREEN"),
    LEARNING_CENTER_SCREEN("LEARNING_CENTER_SCREEN");


    // Screen Constants
//    public static final String PRACTICE_SCREEN = "PRACTICE_SCREEN";
//    public static final String PAST_QUESTION_SCREEN = "PAST_QUESTION_SCREEN";
//    public static final String CBT_GAME_SCREEN = "CBT_GAME_SCREEN";
//    public static final String NOVELS_SCREEN = "NOVELS_SCREEN";
//    public static final String VIDEOS_SCREEN = "VIDEOS_SCREEN";
//    public static final String AUDIOS_SCREEN = "AUDIOS_SCREEN";
//    public static final String LEARNING_CENTER_SCREEN = "LEARNING_CENTER_SCREEN";
//    public static final String NOTES_SCREEN = "NOTES_SCREEN";
//    public static final String SYLLABUS_SCREEN = "SYLLABUS_SCREEN";

    private String name;

    Screens(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
