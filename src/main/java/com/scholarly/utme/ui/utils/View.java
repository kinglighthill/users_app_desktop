package com.scholarly.utme.ui.utils;

import com.scholarly.utme.controller.*;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;

public enum View {
    AUTHENTICATION_SCREEN("/layouts/authentication_screen.fxml", AuthenticationController.class),
    LANDING_SCREEN("/layouts/landing_screen.fxml", LandingScreenController.class),
    HOME_SCREEN("/layouts/homeScreen.fxml", HomeScreenController.class),
    RESULT_SCREEN("/layouts/ResultScreen.fxml", ResultScreenController.class),
    PRACTICE_SCREEN("/layouts/PracticeScreen.fxml", PracticeScreenController.class),
    STUDY_PAST_QUESTION_SCREEN("/layouts/StudyPastQuestionsScreen.fxml", StudyPastQuestScreenController.class),
    CBT_GAME_SCREEN("/layouts/CBTGameScreen.fxml", CBTGameScreenController.class),
    EXPLANATION_SCREEN("/layouts/ExplanationScreen.fxml", ExplanationScreen.class);


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
