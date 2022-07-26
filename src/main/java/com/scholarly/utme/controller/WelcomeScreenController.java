package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.WelcomeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.ViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/WelcomeScreen.fxml")
public class WelcomeScreenController implements FxmlView<WelcomeScreenVM>, Initializable {

    @FXML
    private ImageView welcomeScreenCenterImageView, welcomeScreenCenterBackgroundImageView, screenOneRectangleImageView, screenOnePipeImageView, screenOneCubeImageView;

    @FXML
    private ImageView screenTwoCubeImageView, screenTwoRectangleImageView, screenTwoDoughnutImageView, screenThreeSemicolonImageView, screenThreeDoughnutImageView, screenThreeDoughnutImageView2;

    @FXML
    private ImageView screenFourPipeImageView, screenFourRectangleImageView, screenFourCubeImageView, screenFiveDoughnutImageView, screenFiveSemicolonImageView, screenFiveRectangleImageView;

    @FXML
    private VBox centerVBox;

    @FXML
    private Label advertHeader, advertExplanation;

    @FXML
    private Button nextButton, skipButton;

    @FXML
    private Separator divider1, divider2, divider3, divider4, divider5;

    @FXML
    private StackPane welcomeScreen, presentationImagePane;

    @InjectViewModel
    private ViewModel viewModel;

    private int currentScreen = 1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

        initializeFonts();

        nextButton.setOnAction(event -> {
            currentScreen++;
            System.out.println("Current screen -> " + currentScreen);
            changeScreen(currentScreen);
        });

        skipButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
        });

    }

    private void changeScreen(int currentScreen) {
        if (currentScreen == 2) {
            showScreenTwo();
        }else if (currentScreen == 3) {
            showScreenThree();
        } else if (currentScreen == 4) {
            showScreenFour();
        } else if (currentScreen == 5) {
            showScreenFive();
        } else if (currentScreen > 5) {
            ViewSwitcher.showScreen(View.PRE_AUTHENTICATION_SCREEN);
        }

    }

    private void showScreenTwo() {
        Image screenTwoCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_two_center_background.png").toString());
        Image screenTwoRocketImage = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_two_rocket_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenTwoCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenTwoRocketImage);

        Animations.fadeOut(screenOneRectangleImageView, 300);
        Animations.fadeOut(screenOnePipeImageView, 300);
        Animations.fadeOut(screenOneCubeImageView, 300);

        divider1.setOpacity(1.0);

        Animations.fadeIn(screenTwoCubeImageView, 300);
        Animations.fadeIn(screenTwoRectangleImageView, 300);
        Animations.fadeIn(screenTwoDoughnutImageView, 300);

        divider2.setOpacity(0.5);

        advertHeader.setText("Use app without internet");

    }

    private void showScreenThree() {
        Image screenThreeCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_three_center_background.png").toString());
        Image screenThreeTrophyImage = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_three_trophy_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenThreeCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenThreeTrophyImage);

        Animations.fadeOut(screenTwoCubeImageView, 300);
        Animations.fadeOut(screenTwoRectangleImageView, 300);
        Animations.fadeOut(screenTwoDoughnutImageView, 300);

        divider2.setOpacity(1.0);

        Animations.fadeIn(screenThreeDoughnutImageView, 300);
        Animations.fadeIn(screenThreeSemicolonImageView, 300);
        Animations.fadeIn(screenThreeDoughnutImageView2, 300);

        divider3.setOpacity(0.5);

        advertHeader.setText("Test yourself with JAMB CBT Practice");

    }

    private void showScreenFour() {
        Image screenFourCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_four_center_background.png").toString());
        Image screenFourMegaphoneImage = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_four_megaphone_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenFourCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenFourMegaphoneImage);

        Animations.fadeOut(screenThreeDoughnutImageView, 300);
        Animations.fadeOut(screenThreeSemicolonImageView, 300);
        Animations.fadeOut(screenThreeDoughnutImageView2, 300);

        divider3.setOpacity(1.0);

        Animations.fadeIn(screenFourPipeImageView, 300);
        Animations.fadeIn(screenFourRectangleImageView, 300);
        Animations.fadeIn(screenFourCubeImageView, 300);

        divider4.setOpacity(0.5);

        advertHeader.setText("Get Latest Jamb Updates");
    }

    private void showScreenFive() {
        Image screenFiveCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_five_center_background.png").toString());
        Image screenFiveCenterImage = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_five_connect_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenFiveCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenFiveCenterImage);

        Animations.fadeOut(screenFourPipeImageView, 300);
        Animations.fadeOut(screenFourRectangleImageView, 300);
        Animations.fadeOut(screenFourCubeImageView, 300);

        divider4.setOpacity(1.0);

        Animations.fadeIn(screenFiveDoughnutImageView, 300);
        Animations.fadeIn(screenFiveSemicolonImageView, 300);
        Animations.fadeIn(screenFiveRectangleImageView, 300);

        divider5.setOpacity(0.5);

        advertHeader.setText("Study notes and syllabus");
    }

    private void initializeViews() {
        Image screenOneCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_one_center_background.png").toString());
        Image screenOneCenterImage = new Image(getClass().getResource("/drawable/welcome_screen_images/screen_one_presentation_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenOneCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenOneCenterImage);

        screenOneRectangleImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_one_rectangle.png").toString()));
        screenOnePipeImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_one_pipe.png").toString()));
        screenOneCubeImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_one_cube.png").toString()));

        screenTwoCubeImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_two_cube.png").toString()));
        screenTwoRectangleImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_two_rectangle.png").toString()));
        screenTwoDoughnutImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_two_doughnut.png").toString()));

        screenThreeDoughnutImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_three_doughnut.png").toString()));
        screenThreeSemicolonImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_three_semicircle.png").toString()));
        screenThreeDoughnutImageView2.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_three_doughnut2.png").toString()));

        screenFourPipeImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_four_pipe.png").toString()));
        screenFourRectangleImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_four_rectangle.png").toString()));
        screenFourCubeImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_four_cube.png").toString()));

        screenFiveDoughnutImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_five_doughnut.png").toString()));
        screenFiveSemicolonImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_five_semicircle.png").toString()));
        screenFiveRectangleImageView.setImage(new Image(getClass().getResource("/drawable/welcome_screen_images/screen_five_rectangle.png").toString()));

        VBox.setMargin(presentationImagePane, new Insets(30, 0, 0, 0));
        StackPane.setMargin(welcomeScreenCenterImageView, new Insets(30, 0, 0, 0));
        StackPane.setMargin(screenOnePipeImageView, new Insets(40, 0, 0, 0));
        StackPane.setMargin(screenOneCubeImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenTwoCubeImageView, new Insets(50, 0, 0, 0));
        StackPane.setMargin(screenTwoRectangleImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenThreeDoughnutImageView, new Insets(80, 0, 0, 0));
        StackPane.setMargin(screenThreeSemicolonImageView, new Insets(0, 0, 30, 0));
        StackPane.setMargin(screenFourRectangleImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenFiveDoughnutImageView, new Insets(50, 0, 0, 0));
        StackPane.setMargin(screenFiveSemicolonImageView, new Insets(0, 0, 30, 0));

    }

    private void initializeFonts() {
        advertHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 22));
        advertExplanation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        nextButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));
        skipButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

    }
}
