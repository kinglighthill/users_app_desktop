package com.scholarly.utme.controller.landing_screen;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.cellFactories.RecentlyViewedCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.landing_screen.LandingScreenHomeVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.MouseEvent;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screen/landing_screen_home.fxml")
public class LandingScreenHomeController implements FxmlView<LandingScreenHomeVM>, Initializable {

    @InjectViewModel
    private LandingScreenHomeVM viewModel;

    @FXML
    ListView<Course> recentlyViewedListView;

    ObservableList<Course> recentlyViewedCourses = FXCollections.observableArrayList();

    @FXML
    private Panel practicePanel, pastQuestionsPanel, cbtGamePanel, videosPanel, audioPanel, studyNotesPanel, learningCenterPanel;

    @FXML
    private Label practiceLabel, pastQuestionsLabel, cbtGameLabel, videosLabel, audiosLabel, studyNotesLabel, learningCenterLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String defaultImageURL = getClass().getResource("/drawable/app_logo.png").toString();

        ObservableList<String> recentlyViewedSubjects = viewModel.getRecentlyViewedSubjects();

        // Create courses representing the recently viewed subjects
        for (String recentlyViewedSubject : recentlyViewedSubjects) {
            recentlyViewedCourses.add(new Course(recentlyViewedSubject, defaultImageURL));
        }

        recentlyViewedListView.setCellFactory(new RecentlyViewedCellFactory());

        /*recentlyViewedListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        System.out.println("Selected item: " + newValue)
        );*/

        recentlyViewedListView.setItems(recentlyViewedCourses);


        // Set fontStyles for the Label texts
        practiceLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        pastQuestionsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        cbtGameLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        videosLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        audiosLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        learningCenterLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));
        studyNotesLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 18));


        practicePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(practicePanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });


        pastQuestionsPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(pastQuestionsPanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        cbtGamePanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(cbtGamePanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        videosPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(videosPanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        audioPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(audioPanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        learningCenterPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(learningCenterPanel);
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });

        studyNotesPanel.setOnMouseClicked(e -> {
            ViewSwitcher.passData(studyNotesPanel);
            ViewSwitcher.showScreen(View.SELECT_NOTE_SCREEN);
        });


    }
}
