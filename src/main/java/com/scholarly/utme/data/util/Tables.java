package com.scholarly.utme.data.util;

/**
 * A static class that contains all table names in the Database.
 * NOTE: Table names should not be changed to avoid drastic effect on Database queries.
 */
public class Tables {

    public static final String TOPICS = "topics";
    public static final String YEARS = "years";
    public static final String SUBJECTS = "subjects";
    public static final String PQ_SUBJECTS = "pq_subjects";
    public static final String PQ_OBJECTIVE_QUESTIONS = "pq_objective_questions";
    public static final String PQ_THEORY_QUESTIONS = "pq_theory_questions";
    public static final String PQ_QUES_DESCRIPTIONS = "pq_ques_descriptions";


    // Novel Tables
    public static final String NOVELS = "novels";
    public static final String NOVEL_AUTHORS = "novel_authors";
    public static final String NOVEL_CHAPTERS = "novel_chapters";
    public static final String NOVEL_GENRES = "novel_genres";
    public static final String NOVEL_CATEGORIES = "novel_categories";
    public static final String NOVEL_SECTIONS = "novel_sections";
    public static final String NOVEL_OBJECTIVE_QUESTIONS = "novel_objective_questions";


    // User generated Tables
    public static final String BOOKMARKS_OBJECTIVE_QUESTION = "bookmarks_objective_questions";
    public static final String BOOKMARKS_THEORY_QUESTIONS = "bookmarks_theory_questions";
    public static final String BOOKMARKS_NOVEL_OBJECTIVE_QUESTION = "novel_bookmarks_objective_question";

}
