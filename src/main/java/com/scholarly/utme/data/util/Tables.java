package com.scholarly.utme.data.util;

/**
 * A static class that contains all table names in the Database.
 * NOTE: Table names should not be changed to avoid drastic effect on Database queries.
 */
public class Tables {

    public static final String FREE_CONTENTS = "free_contents";
    public static final String TOPICS = "topics";
    public static final String YEARS = "years";
    public static final String SUBJECTS = "subjects";
    public static final String PQ_OBJECTIVE_QUESTIONS = "pq_objective_questions";
    public static final String PQ_THEORY_QUESTIONS = "pq_theory_questions";
    public static final String PQ_OBJECTIVE_QUES_DESCRIPTIONS = "pq_objective_questions_descriptions";
    public static final String PQ_THEORY_QUES_DESCRIPTIONS = "pq_theory_questions_descriptions";
    public static final String PQ_OBJECTIVE_SUBJECTS = "pq_objective_subjects";
    public static final String PQ_THEORY_SUBJECTS = "pq_theory_subjects";


    // Novel Tables
    public static final String NOVELS = "novels";
    public static final String NOVEL_AUTHORS = "novel_authors";
    public static final String NOVEL_CHAPTERS = "novel_chapters";
    public static final String NOVEL_GENRES = "novel_genres";
    public static final String NOVEL_CATEGORIES = "novel_categories";
    public static final String NOVEL_SECTIONS = "novel_sections";
    public static final String NOVEL_OBJECTIVE_QUESTIONS = "novel_objective_questions";

    // Note Tables
    public static final String NOTE_SUBJECTS = "note_subjects";
    public static final String NOTE_TOPICS = "note_topics";
    public static final String NOTE_SUB_TOPICS = "note_sub_topics";
    public static final String NOTE_SECTIONS = "note_sections";

    // Syllabus Tables
    public static final String SYLLABUS_SUBJECTS = "syllabus_subjects";
    public static final String SYLLABUS_TOPICS = "syllabus_topics";
    public static final String SYLLABUS_CATEGORIES = "syllabus_categories";
    public static final String SYLLABUS_SECTIONS = "syllabus_sections";


    // User generated Tables
    public static final String BOOKMARKS_OBJECTIVE_QUESTIONS = "bookmarks_objective_questions";
    public static final String BOOKMARKS_THEORY_QUESTIONS = "bookmarks_theory_questions";
    public static final String BOOKMARKS_NOVEL_OBJECTIVE_QUESTION = "novel_bookmarks_objective_question";

}
