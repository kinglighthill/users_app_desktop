package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.AuthenticationVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/authentication_screen.fxml")
public class AuthenticationController implements FxmlView<AuthenticationVM>, Initializable {

    public HBox root;

    public VBox imageSliderSection;
    public Pane pane;

    public Separator separator;

    public VBox authenticationSection;



    // Global ImageView array variable;
    ImageView[] imgView = new ImageView[3];
    int imgIndex = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HBox.setMargin(separator, new Insets(0, 30, 0, 30));
        initializeImageSliderSection();
        showDefaultAuthenticationSection();
    }

    private void initializeImageSliderSection() {
        VBox.setMargin(pane, new Insets(40, 40, 40, 40));
        for (int i = 1; i <= 3; i++) {
            imgView[i-1] = new ImageView(new Image(getClass().getResource("/drawable/image" + i + ".jpg").toString()));
            imgView[i-1].setFitWidth(800);
            imgView[i-1].setFitHeight(800);
        }

        pane.getChildren().add(imgView[imgIndex]);

        EventHandler<ActionEvent> eventHandler = e -> {
            if (imgIndex < 2) {
                // Adding Children
                pane.getChildren().remove(imgView[imgIndex]);
                imgIndex++;
                pane.getChildren().add(imgView[imgIndex]);
                FadeTransition ft = new FadeTransition(Duration.millis(1000), imgView[imgIndex]);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
            else if (imgIndex == 2) {
                imgIndex = 0;
                pane.getChildren().remove(imgView[2]);
                pane.getChildren().add(imgView[imgIndex]);
                FadeTransition ft = new FadeTransition(Duration.millis(1000), imgView[imgIndex]);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            }
        };

        // Timeline Animation
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(5000), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    private void showDefaultAuthenticationSection() {

        Label headerLabel = new Label("Welcome to Scholarly Jamb E-learning app");
        VBox.setMargin(headerLabel, new Insets(40, 0, 0, 20));

        VBox buttonBox = new VBox();
        buttonBox.setSpacing(30);
        Button loginButton = new Button("Login");
        HBox buttonSeparator = new HBox();
        VBox.setMargin(buttonSeparator, new Insets(0, 20, 0, 20));
        buttonSeparator.setSpacing(10);
        buttonSeparator.setAlignment(Pos.CENTER);
        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        Label orLabel = new Label("OR");

        HBox.setHgrow(separator1, Priority.ALWAYS);
        HBox.setHgrow(separator2, Priority.ALWAYS);

        buttonSeparator.getChildren().addAll(separator1, orLabel, separator2);

        Button signUpButton = new Button("Sign Up");

        buttonBox.getChildren().addAll(loginButton, buttonSeparator, signUpButton);
        buttonBox.setAlignment(Pos.CENTER);


        VBox.setVgrow(buttonBox, Priority.ALWAYS);

        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(headerLabel, buttonBox);

        loginButton.setOnAction(e -> {
            showLoginUI();
        });

        signUpButton.setOnAction(e -> {
            showSignUpUI();
        });
    }


    /**
     * Shows login UI for authentication
     */
    private void showLoginUI() {
        Label headerLabel = new Label("Login");
        Button backButton = new Button("back");

        HBox topBar = new HBox();
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(backButton, headerLabel);

        VBox.setMargin(topBar, new Insets(40, 0, 0, 20));

        Label credential = new Label("Email or Username");
        VBox.setMargin(credential, new Insets(60, 0, 0, 20));
        TextField credentialTextField = new TextField();
        VBox.setMargin(credentialTextField, new Insets(10, 0, 0, 20));

        Label passwordLabel = new Label("Password");
        VBox.setMargin(passwordLabel, new Insets(20, 0, 0, 20));
        PasswordField passwordField = new PasswordField();
        VBox.setMargin(passwordField, new Insets(10, 0, 0, 20));


        Button loginButton = new Button("Login");
        VBox.setMargin(loginButton, new Insets(40, 0, 0, 20));
        Button forgotPasswordButton = new Button("Forgot Password?");
        VBox.setMargin(forgotPasswordButton, new Insets(15, 0, 0, 20));



        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, credential, credentialTextField, passwordLabel, passwordField, loginButton, forgotPasswordButton);


        loginButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
        backButton.setOnAction(e -> {
            showDefaultAuthenticationSection();
        });
        forgotPasswordButton.setOnAction(e -> {
            showForgotPasswordUI();
        });
    }

    /**
     * Shows sign up UI for authentication
     */
    private void showSignUpUI() {
        Label headerLabel = new Label("Sign Up");
        Button backButton = new Button("back");

        HBox topBar = new HBox();
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(backButton, headerLabel);

        VBox.setMargin(topBar, new Insets(40, 0, 0, 20));

        Label firstName = new Label("First Name");
        VBox.setMargin(firstName, new Insets(60, 0, 0, 20));
        TextField firstNameTextField = new TextField();
        VBox.setMargin(firstNameTextField, new Insets(10, 0, 0, 20));

        Label lastName = new Label("Last Name");
        VBox.setMargin(lastName, new Insets(20, 0, 0, 20));
        TextField lastNameTextField = new TextField();
        VBox.setMargin(lastNameTextField, new Insets(10, 0, 0, 20));

        Label email = new Label("Email");
        VBox.setMargin(email, new Insets(20, 0, 0, 20));
        TextField emailTextField = new TextField();
        VBox.setMargin(emailTextField, new Insets(10, 0, 0, 20));

        Label passwordLabel = new Label("Password");
        VBox.setMargin(passwordLabel, new Insets(20, 0, 0, 20));
        PasswordField passwordField = new PasswordField();
        VBox.setMargin(passwordField, new Insets(10, 0, 0, 20));


        Button signUpButton = new Button("Sign Up");
        VBox.setMargin(signUpButton, new Insets(40, 0, 0, 20));



        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, firstName, firstNameTextField, lastName, lastNameTextField, email, emailTextField, passwordLabel, passwordField, signUpButton);


        backButton.setOnAction(e -> {
            showDefaultAuthenticationSection();
        });
        signUpButton.setOnAction(e -> {
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void showForgotPasswordUI() {
        Label headerLabel = new Label("Forgot Password");
        Button backButton = new Button("back");

        HBox topBar = new HBox();
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(backButton, headerLabel);

        VBox.setMargin(topBar, new Insets(40, 0, 0, 20));

        Label description = new Label("Enter your email address below. An email with a reset link will be sent shortly afterward.");
        VBox.setMargin(description, new Insets(20, 0, 0, 20));

        Label email = new Label("Email");
        VBox.setMargin(email, new Insets(60, 0, 0, 20));
        TextField emailTextField = new TextField();
        VBox.setMargin(emailTextField, new Insets(10, 0, 0, 20));


        Button proceedButton = new Button("Proceed");
        VBox.setMargin(proceedButton, new Insets(40, 0, 0, 20));



        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, description, email, emailTextField, proceedButton);

        backButton.setOnAction(e -> {
            showLoginUI();
        });
        proceedButton.setOnAction(e -> {
            showLoginUI();
        });
    }
}
