package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.cellFactories.RecentlyViewedCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screen.LandingScreenHomeVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_home.fxml")
public class LandingScreenHomeController implements FxmlView<LandingScreenHomeVM>, Initializable {

    @InjectViewModel
    private LandingScreenHomeVM viewModel;

    @FXML
    ListView<Course> recentlyViewedListView;

    @FXML
    private Button practiceButton, pastQuestionsButton, cbtGameButton, videosButton, audioButton, studyNotesButton, learningCenterButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String defaultImageURL = getClass().getResource("/drawable/app_logo.png").toString();
        ObservableList<Course> items = FXCollections.observableArrayList(
                new Course("Math 101", defaultImageURL),
                new Course("Chem 202", defaultImageURL),
                new Course("Eng 103", defaultImageURL),
                new Course("Phy 212", defaultImageURL)
        );

        recentlyViewedListView.setItems(items);

        recentlyViewedListView.setCellFactory(new RecentlyViewedCellFactory());

        practiceButton.setOnAction(e -> {
            ViewSwitcher.passData(practiceButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        pastQuestionsButton.setOnAction(e -> {
            ViewSwitcher.passData(pastQuestionsButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        cbtGameButton.setOnAction(e -> {
            ViewSwitcher.passData(cbtGameButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        videosButton.setOnAction(e -> {
            ViewSwitcher.passData(videosButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        audioButton.setOnAction(e -> {
            ViewSwitcher.passData(audioButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        learningCenterButton.setOnAction(e -> {
            ViewSwitcher.passData(learningCenterButton);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        studyNotesButton.setOnAction(e -> {
            ViewSwitcher.passData(studyNotesButton);
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });


    }
}
