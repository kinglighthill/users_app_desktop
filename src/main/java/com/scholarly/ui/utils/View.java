package com.scholarly.ui.utils;

import com.scholarly.controller.*;
import com.scholarly.controller.account_screens.*;
import com.scholarly.controller.audio_video_screens.AudioContentScreenController;
import com.scholarly.controller.audio_video_screens.AudiosGridScreenController;
import com.scholarly.controller.audio_video_screens.VideoContentScreenController;
import com.scholarly.controller.audio_video_screens.VideosGridScreenController;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.controller.note_screens.NotesScreenController;
import com.scholarly.controller.note_screens.SelectNoteController;
import com.scholarly.controller.novel_screens.NovelChapterListController;
import com.scholarly.controller.novel_screens.NovelContentScreenController;
import com.scholarly.controller.novel_screens.NovelGridScreenController;
import com.scholarly.controller.novel_screens.NovelScreenController;
import com.scholarly.controller.practice_screens.*;
import com.scholarly.controller.settings_screens.SettingsAboutUsScreenController;
import com.scholarly.controller.settings_screens.SettingsAppInfoScreenController;
import com.scholarly.controller.settings_screens.SettingsHelpScreenController;
import com.scholarly.controller.syllabus_screens.SelectSyllabusController;
import com.scholarly.controller.syllabus_screens.SyllabusScreenController;
import com.scholarly.controller.trivia_screens.*;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.ViewModel;

public enum View {
    WELCOME_SCREEN("/layouts/WelcomeScreen.fxml", WelcomeScreenController.class),
    PRE_AUTHENTICATION_SCREEN("/layouts/PreAuthenticationScreen.fxml", PreAuthenticationController.class),
    AUTHENTICATION_SCREEN("/layouts/AuthenticationScreen.fxml", AuthenticationController.class),
    LANDING_SCREEN("/layouts/landing_screens/landing_screen.fxml", LandingScreenController.class),
    PQ_SCREEN("/layouts/PQScreen.fxml", PQScreenController.class),
    PQ_SCREEN_WAEC("/layouts/PQScreenWaec.fxml", PQScreenControllerWaec.class),
    RESULT_SCREEN("/layouts/practice_screens/ResultScreen.fxml", ResultScreenController.class),
    PRACTICE_SCREEN("/layouts/practice_screens/PracticeScreen.fxml", PracticeScreenController.class),
    STUDY_PAST_QUESTION_SCREEN("/layouts/practice_screens/StudyPastQuestionsScreen.fxml", StudyPastQuestScreenController.class),
//    STUDY_PAST_QUESTION_SCREEN2("/layouts/practice_screens/StudyPastQuestionsScreen.fxml", StudyPastQuestScreenController2.class),
    CBT_GAME_SCREEN("/layouts/practice_screens/CBTGameScreen.fxml", CBTGameScreenController.class),
    SELECT_NOTE_SCREEN("/layouts/note_screens/SelectNoteScreen.fxml", SelectNoteController.class),
    NOTES_SCREEN("/layouts/note_screens/NotesScreen.fxml", NotesScreenController.class),
    EXPLANATION_SCREEN("/layouts/practice_screens/ExplanationScreen.fxml", ExplanationScreenController.class),
    SELECT_SYLLABUS_SCREEN("/layouts/syllabus_screens/SelectSyllabusScreen.fxml", SelectSyllabusController.class),
    SYLLABUS_SCREEN("/layouts/syllabus_screens/SyllabusScreen.fxml", SyllabusScreenController.class),
    NOVEL_SCREEN("/layouts/novel_screens/NovelScreen.fxml", NovelScreenController.class),
    NOVEL_GRID_SCREEN("/layouts/novel_screens/NovelGridScreen.fxml", NovelGridScreenController.class),
    NOVEL_CHAPTER_LIST_SCREEN("/layouts/novel_screens/NovelChapterListScreen.fxml", NovelChapterListController.class),
    NOVEL_CONTENT_SCREEN("/layouts/novel_screens/NovelContentScreen.fxml", NovelContentScreenController.class),
    VIDEOS_GRID_SCREEN("/layouts/audio_video_screens/VideosGridScreen.fxml", VideosGridScreenController.class),
    VIDEO_CONTENT_SCREEN("/layouts/audio_video_screens/VideoContentScreen.fxml", VideoContentScreenController.class),
    AUDIOS_GRID_SCREEN("/layouts/audio_video_screens/AudiosGridScreen.fxml", AudiosGridScreenController.class),
    AUDIO_CONTENT_SCREEN("layouts/audio_video_screens/AudioContentScreen.fxml", AudioContentScreenController.class),
    TRIVIA_CHALLENGE_SCREEN("/layouts/trivia_screens/TriviaChallengeScreen.fxml", TriviaChallengeScreenController.class),
    TRIVIA_CHALLENGE_QUIZ_SCREEN("/layouts/trivia_screens/TriviaChallengeQuizScreen.fxml", TriviaChallengeQuizScreenController.class),
    TRIVIA_QUIZ_RESULT_SCREEN("/layouts/trivia_screens/TriviaQuizResultScreen.fxml", TriviaQuizResultScreenController.class),
    TRIVIA_QUIZ_EXPLANATION_SCREEN("/layouts/trivia_screens/TriviaQuizExplanationScreen.fxml", TriviaQuizExplanationScreenController.class),
    TRIVIA_RANKING_SCREEN("/layouts/trivia_screens/TriviaRankingScreen.fxml", TriviaRankingScreenController.class),
    ACTIVATE_PAYMENT_SCREEN("/layouts/ActivatePaymentScreen.fxml", ActivatePaymentScreenController.class),
    APPS_GRID_SCREEN("/layouts/AppsGridScreen.fxml", AppsGridScreenController.class),
    APP_DETAILS_SCREEN("/layouts/AppDetailsScreen.fxml", AppDetailsScreenController.class),
    UPDATE_SCREEN("/layouts/UpdateScreen.fxml", UpdateScreenController.class),
    SETTINGS_ABOUT_US_SCREEN("/layouts/settings_screens/SettingsAboutUsScreen.fxml", SettingsAboutUsScreenController.class),
    SETTINGS_HELP_SCREEN("/layouts/settings_screens/SettingsHelpScreen.fxml", SettingsHelpScreenController.class),
    SETTINGS_APP_INFO_SCREEN("/layouts/settings_screens/SettingsAppInfoScreen.fxml", SettingsAppInfoScreenController.class),
    ACCOUNT_PROFILE_SCREEN("/layouts/account_screens/AccountProfileScreen.fxml", AccountProfileScreenController.class),
    ACCOUNT_REFERRAL_SCREEN("/layouts/account_screens/AccountReferralScreen.fxml", AccountReferralScreenController.class),
    ACCOUNT_BOOKMARKS_SCREEN("/layouts/account_screens/AccountBookmarksScreen.fxml", AccountBookmarksScreenController.class),
    ACCOUNT_TRIVIA_SCREEN("/layouts/account_screens/AccountTriviaScreen.fxml", AccountTriviaScreenController.class),
    ACCOUNT_NOTIFICATIONS_SCREEN("/layouts/account_screens/AccountNotificationsScreen.fxml", AccountNotificationsScreenController.class),
    NOTIFICATION_DETAILS_SCREEN("/layouts/account_screens/NotificationDetailsScreen.fxml", NotificationDetailsScreenController.class);



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
