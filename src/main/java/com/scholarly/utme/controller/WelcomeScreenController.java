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
    private ImageView welcomeScreenCenterImageView, welcomeScreenCenterBackgroundImageView, screenOneRectangleImageView, screenOnePipeImageView, screenTwoRectangleImageView, screenTwoDoughnutImageView, screenThreeSemicolonImageView, screenThreeDoughnutImageView, screenThreeDoughnutImageView2, screenFourPipeImageView, screenFourRectangleImageView, screenFiveDoughnutImageView, screenFiveSemicolonImageView, screenFiveRectangleImageView;

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
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
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
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        }

    }

    private void showScreenTwo() {
        Image screenTwoCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_two_center_background.png").toString());
        System.out.println("ScreenTwoImageBackground height -> " + screenTwoCenterImageBackground.getHeight());
        Image screenTwoRocketImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_two_rocket_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenTwoCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenTwoRocketImage);

        Animations.fadeOut(screenOneRectangleImageView, 300);
        Animations.fadeOut(screenOnePipeImageView, 300);

        divider1.setOpacity(1.0);

        Animations.fadeIn(screenTwoRectangleImageView, 300);
        Animations.fadeIn(screenTwoDoughnutImageView, 300);

        divider2.setOpacity(0.5);

        advertHeader.setText("Use app without internet");

    }

    private void showScreenThree() {
        Image screenThreeCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_three_center_background.png").toString());
        System.out.println("ScreenThreeImageBackground height -> " + screenThreeCenterImageBackground.getHeight());
        Image screenThreeTrophyImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_three_trophy_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenThreeCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenThreeTrophyImage);

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
        Image screenFourCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_four_center_background.png").toString());
        System.out.println("ScreenFourImageBackground height -> " + screenFourCenterImageBackground.getHeight());
        Image screenFourMegaphoneImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_four_megaphone_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenFourCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenFourMegaphoneImage);

        Animations.fadeOut(screenThreeDoughnutImageView, 300);
        Animations.fadeOut(screenThreeSemicolonImageView, 300);
        Animations.fadeOut(screenThreeDoughnutImageView2, 300);

        divider3.setOpacity(1.0);

        Animations.fadeIn(screenFourPipeImageView, 300);
        Animations.fadeIn(screenFourRectangleImageView, 300);

        divider4.setOpacity(0.5);

        advertHeader.setText("Get Latest Jamb Updates");
    }

    private void showScreenFive() {
        Image screenFiveCenterImageBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_five_center_background.png").toString());
        System.out.println("ScreenFiveCenterImageBackground height -> " + screenFiveCenterImageBackground.getHeight());
        Image screenFourMegaphoneImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_four_megaphone_image.png").toString());

        welcomeScreenCenterBackgroundImageView.setImage(screenFiveCenterImageBackground);
        welcomeScreenCenterImageView.setImage(screenFourMegaphoneImage);

        Animations.fadeOut(screenFourPipeImageView, 300);
        Animations.fadeOut(screenFourRectangleImageView, 300);

        divider4.setOpacity(1.0);

        Animations.fadeIn(screenFiveDoughnutImageView, 300);
        Animations.fadeIn(screenFiveSemicolonImageView, 300);
        Animations.fadeIn(screenFiveRectangleImageView, 300);

        divider5.setOpacity(0.5);

        advertHeader.setText("Study notes and syllabus");
    }

    private void initializeViews() {
        Image welcomeScreenCenterBackground = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_one_presentation_background.png").toString());
        welcomeScreenCenterBackgroundImageView.setImage(welcomeScreenCenterBackground);
        Image screenOnePresentation = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_one_presentation_image.png").toString());
        welcomeScreenCenterImageView.setImage(screenOnePresentation);
        Image screenOneRectangle = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_one_rectangle_image.png").toString());
        screenOneRectangleImageView.setImage(screenOneRectangle);
        Image screenOnePipe = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_one_pipe_image.png").toString());
        screenOnePipeImageView.setImage(screenOnePipe);

        Image screenTwoRectangleImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_two_rectangle_image.png").toString());
        screenTwoRectangleImageView.setImage(screenTwoRectangleImage);
        Image screenTwoDoughnutImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_two_doughnut_image.png").toString());
        screenTwoDoughnutImageView.setImage(screenTwoDoughnutImage);

        Image screenThreeDoughnutImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_three_doughnut_image.png").toString());
        screenThreeDoughnutImageView.setImage(screenThreeDoughnutImage);
        Image screenThreeSemicolonImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_three_semicolon_image.png").toString());
        screenThreeSemicolonImageView.setImage(screenThreeSemicolonImage);
        Image screenThreeDoughnutImage2 = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_three_doughnut_image2.png").toString());
        screenThreeDoughnutImageView2.setImage(screenThreeDoughnutImage2);

        Image screenFourPipeImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_four_pipe_image.png").toString());
        screenFourPipeImageView.setImage(screenFourPipeImage);
        Image screenFourRectangleImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_four_rectangle_image.png").toString());
        screenFourRectangleImageView.setImage(screenFourRectangleImage);

        Image screenFiveDoughnutImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_five_doughnut_image.png").toString());
        screenFiveDoughnutImageView.setImage(screenFiveDoughnutImage);
        Image screenFiveSemicolonImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_five_semicolon_image.png").toString());
        screenFiveSemicolonImageView.setImage(screenFiveSemicolonImage);
        Image screenFiveRectangleImage = new Image(getClass().getResource("/drawable/welcome_screen_images/welcome_screen_five_rectangle_image.png").toString());
        screenFiveRectangleImageView.setImage(screenFiveRectangleImage);

        VBox.setMargin(presentationImagePane, new Insets(30, 0, 0, 0));
        StackPane.setMargin(welcomeScreenCenterImageView, new Insets(0, 0, 30, 0));
        StackPane.setMargin(screenOnePipeImageView, new Insets(40, 0, 0, 0));
        StackPane.setMargin(screenTwoRectangleImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenThreeDoughnutImageView, new Insets(50, 0, 0, 0));
        StackPane.setMargin(screenThreeSemicolonImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenFourRectangleImageView, new Insets(0, 0, 50, 0));
        StackPane.setMargin(screenFiveDoughnutImageView, new Insets(50, 0, 0, 0));
        StackPane.setMargin(screenFiveSemicolonImageView, new Insets(0, 0, 50, 0));

    }

    private void initializeFonts() {
        advertHeader.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWENTY.size));
        advertExplanation.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        nextButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        skipButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

    }
}
