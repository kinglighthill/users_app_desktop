package com.scholarly.controller;

import com.scholarly.ui.utils.Animations;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.viewmodels.PreAuthenticationVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/PreAuthenticationScreen.fxml")
public class PreAuthenticationController implements FxmlView<PreAuthenticationVM>, Initializable {

    @FXML
    private HBox root;

    @FXML
    private VBox imageSliderSection, authenticationSection, footerVBox;

    @FXML
    private Label scholarlyText, appText, scholarlyFooterText, termsOfServiceText, privacyPolicyText, faqText;

    @FXML
    private ImageView scholarlyLogo, sliderImageView1, sliderImageView2, sliderImageView3;

    @FXML
    private Button signupButton, loginButton;

    @FXML
    private StackPane sliderImagePane;

    @FXML
    private Pane pane;

    Label loginInfoLabel, resetInfoLabel, signupInfoLabel;

    // Global ImageView array variable;
    ImageView[] imgView = new ImageView[3];
    int imgIndex = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

        initializeFonts();

        startImageSlider();

        signupButton.setOnAction(event -> {
            boolean showSignUpScreen = true;
            ViewSwitcher.passData(new AuthenticationController.InitialData(showSignUpScreen));
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        });

        loginButton.setOnAction(event -> {
            boolean showSignUpScreen = false;
            ViewSwitcher.passData(new AuthenticationController.InitialData(showSignUpScreen));
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        });
    }


    private void startImageSlider() {
        EventHandler<ActionEvent> eventHandler = event -> {
            if (imgIndex == 0) {
                Animations.fadeOut(sliderImageView3, 300);
                if (!sliderImageView1.isVisible()) {
                    Animations.fadeIn(sliderImageView1, 300);
                }
                imgIndex++;
            }
            else if (imgIndex == 1) {
                Animations.fadeOut(sliderImageView1, 300);
                Animations.fadeIn(sliderImageView2, 300);

                imgIndex++;
            }
            else if (imgIndex == 2) {
                Animations.fadeOut(sliderImageView2, 300);
                Animations.fadeIn(sliderImageView3, 300);

                imgIndex = 0;
            }
        };

        // Timeline Animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(3000), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void initializeViews() {
        scholarlyLogo.setImage(new Image(getClass().getResource("/drawable/app_icon.png").toString()));
        sliderImageView1.setImage(new Image(getClass().getResource("/drawable/auth_screen_image1.jpg").toString()));
        sliderImageView2.setImage(new Image(getClass().getResource("/drawable/auth_screen_image2.jpg").toString()));
        sliderImageView3.setImage(new Image(getClass().getResource("/drawable/auth_screen_image3.jpg").toString()));


        loginButton.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWENTY.size));
        appText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.THIRTY_FOUR.size));
        signupButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        loginButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        scholarlyFooterText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        termsOfServiceText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        privacyPolicyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        faqText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
    }

}
