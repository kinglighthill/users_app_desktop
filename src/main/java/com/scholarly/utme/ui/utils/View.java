package com.scholarly.utme.ui.utils;

import com.scholarly.utme.controller.*;
import com.scholarly.utme.controller.landing_screen.LandingScreenController;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;

public enum View {
    WELCOME_SCREEN("/layouts/WelcomeScreen.fxml", WelcomeScreenController.class),
    PRE_AUTHENTICATION_SCREEN("/layouts/PreAuthenticationScreen.fxml", PreAuthenticationController.class),
    AUTHENTICATION_SCREEN("/layouts/AuthenticationScreen.fxml", AuthenticationController.class),
    LANDING_SCREEN("/layouts/landing_screen/landing_screen.fxml", LandingScreenController.class),
    HOME_SCREEN("/layouts/HomeScreen.fxml", HomeScreenController.class),
    RESULT_SCREEN("/layouts/ResultScreen.fxml", ResultScreenController.class),
    PRACTICE_SCREEN("/layouts/PracticeScreen.fxml", PracticeScreenController.class),
    STUDY_PAST_QUESTION_SCREEN("/layouts/StudyPastQuestionsScreen.fxml", StudyPastQuestScreenController.class),
    CBT_GAME_SCREEN("/layouts/CBTGameScreen.fxml", CBTGameScreenController.class),
    SELECT_NOTE_SCREEN("/layouts/SelectNoteScreen.fxml", SelectNoteController.class),
    NOTES_SCREEN("/layouts/NotesScreen.fxml", NotesScreenController.class),
    EXPLANATION_SCREEN("/layouts/ExplanationScreen.fxml", ExplanationScreen.class),
    SELECT_SYLLABUS_SCREEN("/layouts/SelectSyllabusScreen.fxml", SelectSyllabusController.class),
    SYLLABUS_SCREEN("/layouts/SyllabusScreen.fxml", SyllabusScreenController.class),
    NOVEL_GRID_SCREEN("/layouts/NovelGridScreen.fxml", NovelGridScreenController.class),
    NOVEL_CHAPTER_LIST_SCREEN("/layouts/NovelChapterListScreen.fxml", NovelChapterListController.class),
    NOVEL_CONTENT_SCREEN("/layouts/NovelContentScreen.fxml", NovelContentScreenController.class),
    VIDEOS_GRID_SCREEN("/layouts/VideosGridScreen.fxml", VideosGridScreenController.class),
    VIDEO_CONTENT_SCREEN("/layouts/VideoContentScreen.fxml", VideoContentScreenController.class);



    private String viewPath;
    private Class<? extends FxmlView<? extends ViewModel>> controllerClass;

    View(String viewPath, Class<? extends FxmlView<? extends ViewModel>> controllerClass) {
        this.viewPath = viewPath;
        this.controllerClass = controllerClass;
    }

    public String getViewPath() {
        return viewPath;
    }

    public Class<? extends FxmlView<? extends ViewModel>> getControllerClass() {
        return controllerClass;
    }
}
