package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.FontUtil;
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

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AuthenticationScreen.fxml")
public class AuthenticationController implements FxmlView<AuthenticationScreenVM>, Initializable {

    @FXML
    private ImageView imageView, appIcon;

    @FXML
    private Label scholarlyText, beTheBestText, signupText, emailText, passwordText, phoneText, continueText, haveAccountText, loginText;

    @FXML
    private Button proceedButton, googleButton, facebookButton;

    @FXML
    private TextField emailField, passwordField, phoneField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

        initializeFonts();

        proceedButton.setOnAction(event -> {
            System.out.println("Proceed button clicked");
        });

    }

    private void initializeViews() {
        imageView.setImage(new Image(getClass().getResource("/drawable/signup_screen_image.jpg").toString()));
        appIcon.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));

        ImageView googleImage = new ImageView(new Image(getClass().getResource("/drawable/google_icon.png").toString()));
        googleButton.setGraphic(googleImage);
        googleButton.setGraphicTextGap(20);
        googleButton.setBackground(Background.EMPTY);
        ImageView facebookImage = new ImageView(new Image(getClass().getResource("/drawable/facebook_icon.png").toString()));
        facebookButton.setGraphic(facebookImage);
        facebookButton.setGraphicTextGap(20);
        facebookButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY_SIX.size));
        beTheBestText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.TWENTY_SIX.size));
        signupText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.TWENTY.size));
        emailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        emailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        passwordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        //passwordField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        phoneText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        phoneField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        proceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        continueText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        googleButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        facebookButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        haveAccountText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        loginText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
    }
}
