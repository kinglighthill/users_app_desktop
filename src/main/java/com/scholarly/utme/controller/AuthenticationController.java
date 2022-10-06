package com.scholarly.utme.controller;

import com.google.gson.Gson;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.NetworkModule;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.DeviceInfo;
import com.scholarly.utme.network.model.ReferrerInfo;
import com.scholarly.utme.network.model.SignupResponse;
import com.scholarly.utme.network.model.User;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.AuthenticationScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import jidefx.scene.control.field.NumberField;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.security.Signature;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AuthenticationScreen.fxml")
public class AuthenticationController implements FxmlView<AuthenticationScreenVM>, Initializable {
    private static final String TAG = "AuthenticationController: ";

    @FXML
    private StackPane authenticationSection;

    @FXML
    private VBox signUpSection, loginSection, recoverPasswordSection, signUpEmailSection, signUpPasswordSection, signUpPhoneSection, loginEmailSection, loginPasswordSection;

    @FXML
    private ImageView imageView, appIcon;

    @FXML
    private Label scholarlyText, beTheBestText, signUpHeaderText, signUpEmailText, signUpPasswordText, signUpPhoneText, signUpContinueText, signUpHaveAccountText, signUpLoginText, forgotPasswordText, resetText;

    @FXML
    private Label signUpEmailError, loginHeaderText, loginEmailText, loginPasswordText, loginContinueText, loginHaveAcctText, loginSignUpText, recoverHeaderText, recoverEmailText, recoverEmailError;

    @FXML
    private Button signUpProceedButton, signUpGoogleButton, signUpFacebookButton, loginProceedButton, loginGoogleButton, loginFacebookButton, recoverProceedButton;

    @FXML
    private TextField signUpEmailField, signUpPasswordField, loginEmailField, loginPasswordField, recoverEmailField;

    @FXML
    private CustomNumberField signUpPhoneField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        boolean showSignUpScreen = (boolean) ViewSwitcher.retrieveData();
        if (showSignUpScreen) {
            Animations.fadeIn(signUpSection, 300);
        } else {
            Animations.fadeIn(loginSection, 300);
        }

        initializeViews();
        initializeFonts();

        signUpLoginText.setOnMouseClicked(event -> {
            Animations.fadeOut(signUpSection, 200);
            Animations.fadeIn(loginSection, 300);
        });

        loginSignUpText.setOnMouseClicked(event -> {
            Animations.fadeOut(loginSection, 200);
            Animations.fadeIn(signUpSection, 300);
        });

        resetText.setOnMouseClicked(event -> {
            recoverEmailError.setVisible(false);
            recoverEmailError.setText("Please enter a valid email address");
            recoverEmailError.setTextFill(Paint.valueOf("#FF0000"));
            recoverEmailField.setText("");
            recoverProceedButton.setText("Proceed");
            Animations.fadeOut(loginSection, 300);
            Animations.fadeIn(recoverPasswordSection, 300);

        });


        Label signUpEmailError = getEmailError();
        Label signUpPasswordError = getPasswordError();
        Label signUpPhoneError = getPhoneError();

        signUpProceedButton.setOnAction(event -> {
            String email = signUpEmailField.getText();
            signUpEmailSection.getChildren().remove(signUpEmailError);
            if (!email.contains("@")) {
                signUpEmailSection.getChildren().add(signUpEmailError);
                return;
            }

            String password = signUpPasswordField.getCharacters().toString();
            signUpPasswordSection.getChildren().remove(signUpPasswordError);
            if (password.length() < 6) {
                if (!signUpPasswordSection.getChildren().contains(signUpPasswordError)) {
                    signUpPasswordSection.getChildren().add(signUpPasswordError);
                }
                return;
            }

            String phoneNumber = signUpPhoneField.getCharacters().toString();
            signUpPhoneSection.getChildren().remove(signUpPhoneError);
            if (phoneNumber.length() < 11) {
                if (!signUpPhoneSection.getChildren().contains(signUpPhoneError)) {
                    signUpPhoneSection.getChildren().add(signUpPhoneError);
                }
                return;
            }

            signUpEmailSection.getChildren().remove(signUpEmailError);
            signUpPasswordSection.getChildren().remove(signUpPasswordError);
            signUpPhoneSection.getChildren().remove(signUpPhoneError);

            DeviceInfo deviceInfo = new DeviceInfo();
            ReferrerInfo referrerInfo = new ReferrerInfo();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            user.setDeviceInfo(deviceInfo);
            user.setReferrerInfo(referrerInfo);

            SignupResponse response = NetworkService.createNewUser(user);
            if (response != null) {
                if (response.getMessage().equalsIgnoreCase("success")) {
                    ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
                    ViewSwitcher.showScreen(View.LANDING_SCREEN);
                } else {
                    System.out.println(TAG + "Cannot sign up because -> " + response.getMessage());

                }
            }

        });

        Label loginEmailError = getEmailError();
        Label loginPasswordError = getPasswordError();

        loginProceedButton.setOnAction(event -> {
            loginEmailSection.getChildren().remove(loginEmailError);
            if (!loginEmailField.getText().contains("@")) {
                loginEmailSection.getChildren().add(loginEmailError);
                return;
            }

            loginPasswordSection.getChildren().remove(loginPasswordError);
            if (loginPasswordField.getCharacters().length() < 6) {
                if (!loginPasswordSection.getChildren().contains(loginPasswordError)) {
                    loginPasswordSection.getChildren().add(loginPasswordError);
                }
                return;
            }

            loginEmailSection.getChildren().remove(loginEmailError);
            loginPasswordSection.getChildren().remove(loginPasswordError);

            ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);

        });

        recoverProceedButton.setOnAction(event -> {
            if (!recoverEmailField.getText().contains("@")) {
                recoverEmailError.setVisible(true);
            } else {
                if (recoverProceedButton.getText().contains("Back")) {
                    Animations.fadeOut(recoverPasswordSection, 300);
                    Animations.fadeIn(loginSection, 300);
                }
                recoverEmailError.setVisible(true);
                recoverEmailError.setText("A password reset link has been sent to the above registered email");
                recoverEmailError.setTextFill(Paint.valueOf("#053500"));
                recoverProceedButton.setText("Back to Login");
            }
        });

    }

    private Label getEmailError() {
        Label error = new Label("Please enter a valid email address");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
    }

    private Label getPasswordError() {
        Label error = new Label("Your password must be more than 6 characters");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
    }

    private Label getPhoneError() {
        Label error = new Label("Your phone number must be more than 11 characters");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
    }

    private void initializeViews() {
        imageView.setImage(new Image(getClass().getResource("/drawable/signup_screen_image.jpg").toString()));
        appIcon.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        ImageView googleImage = new ImageView(new Image(getClass().getResource("/drawable/google_icon.png").toString()));
        signUpGoogleButton.setGraphic(googleImage);
        signUpGoogleButton.setGraphicTextGap(20);
        signUpGoogleButton.setBackground(Background.EMPTY);
        ImageView googleImage2 = new ImageView(new Image(getClass().getResource("/drawable/google_icon.png").toString()));
        loginGoogleButton.setGraphic(googleImage2);
        loginGoogleButton.setGraphicTextGap(20);
        loginGoogleButton.setBackground(Background.EMPTY);

        ImageView facebookImage = new ImageView(new Image(getClass().getResource("/drawable/facebook_icon.png").toString()));
        signUpFacebookButton.setGraphic(facebookImage);
        signUpFacebookButton.setGraphicTextGap(20);
        signUpFacebookButton.setBackground(Background.EMPTY);
        ImageView facebookImage2 = new ImageView(new Image(getClass().getResource("/drawable/facebook_icon.png").toString()));
        loginFacebookButton.setGraphic(facebookImage2);
        loginFacebookButton.setGraphicTextGap(20);
        loginFacebookButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 30));
        beTheBestText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 26));

        signUpHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        signUpEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPhoneText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPhoneField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpContinueText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpGoogleButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpFacebookButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpHaveAccountText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpLoginText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));


        loginHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        loginEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginContinueText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginGoogleButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginFacebookButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginHaveAcctText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginSignUpText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        forgotPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        resetText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));


        recoverHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        recoverEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverEmailError.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        recoverProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
    }


}
