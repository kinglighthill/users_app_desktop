package com.scholarly.data.util;

public enum Table {
    Subjects("subjects"),
    SubjectsTheory("subjects_theory"),
    Years("years"),
    QuestionDescriptions("ques_descriptions"),
    QuestionDescriptionTheory("question_description_theory"),
    Accounts("accounts"),
    AccountsTheory("accounts_theory"),
    Agric("agric"),
    AgricTheory("agric_theory"),
    Biology("biology"),
    BiologyTheory("biology_theory"),
    Chemistry("chemistry"),
    ChemistryTheory("chemistry_theory"),
    Civic("civic"),
    CivicTheory("civic_theory"),
    Commerce("commerce"),
    CommerceTheory("commerce_theory"),
    Computer("computer"),
    ComputerTheory("computer_theory"),
    Crs("crs"),
    CrsTheory("crs_theory"),
    Economics("economics"),
    English("english"),
    EnglishTheory("english_theory"),
    FurtherMaths("further_maths"),
    FurtherMathsTheory("further_maths_theory"),
    Geography("geography"),
    GeographyTheory("geography_theory"),
    Government("government"),
    GovernmentTheory("government_theory"),
    InterScience("inter_science"),
    InterScienceTheory("inter_science_theory"),
    Literature("literature"),
    LiteratureTheory("literature_theory"),
    Mathematics("mathematics"),
    MathematicsTheory("mathematics_theory"),
    Physics("physics"),
    PhysicsTheory("physics_theory");

    private String tableName;

    Table(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
