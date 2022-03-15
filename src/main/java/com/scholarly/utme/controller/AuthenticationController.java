package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.User;
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
import javafx.fxml.FXML;
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

    @FXML
    public HBox root;

    @FXML
    public VBox imageSliderSection;

    @FXML
    public Pane pane;

    @FXML
    public Separator separator;

    @FXML
    public VBox authenticationSection;

    Label loginInfoLabel, resetInfoLabel, signupInfoLabel;

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


        HBox topBar = new HBox();
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(headerLabel);

        VBox.setMargin(topBar, new Insets(40, 0, 0, 20));

        Label credential = new Label("Email or Username");
        VBox.setMargin(credential, new Insets(60, 0, 0, 20));
        TextField credentialTextField = new TextField();
        VBox.setMargin(credentialTextField, new Insets(10, 0, 0, 20));

        Label passwordLabel = new Label("Password");
        VBox.setMargin(passwordLabel, new Insets(20, 0, 0, 20));
        PasswordField passwordField = new PasswordField();
        VBox.setMargin(passwordField, new Insets(10, 0, 0, 20));

        loginInfoLabel = new Label();
        loginInfoLabel.setVisible(false);
        VBox.setMargin(loginInfoLabel, new Insets(15, 0, 0, 20));

        Button loginButton = new Button("Login");
        VBox.setMargin(loginButton, new Insets(40, 0, 0, 20));

        HBox backAndForgotPassButton = new HBox();
        VBox.setMargin(backAndForgotPassButton, new Insets(20, 0, 0, 20));


        Button backButton = new Button("Back");
        HBox.setMargin(backButton, new Insets(0, 10, 0, 0));

        Button forgotPasswordButton = new Button("Forgot Password?");
        HBox.setMargin(forgotPasswordButton, new Insets(0, 0, 0, 10));

        backAndForgotPassButton.getChildren().clear();
        backAndForgotPassButton.getChildren().addAll(backButton, forgotPasswordButton);



        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, credential, credentialTextField, passwordLabel, passwordField, loginInfoLabel, loginButton, backAndForgotPassButton);


        loginButton.setOnAction(e -> {
            validateLoginInput(credentialTextField, passwordField);

        });
        backButton.setOnAction(e -> {
            showDefaultAuthenticationSection();
        });
        forgotPasswordButton.setOnAction(e -> {
            showForgotPasswordUI();
        });
    }

    /**
     * Validates user authentication details before login
     * @param credentialField the email address or username entered
     * @param passwordField the user's password
     */
    private void validateLoginInput(TextField credentialField, PasswordField passwordField) {
        String credential = credentialField.getText().trim();
        String password = passwordField.getText().trim();

        if (credential.contains("@") && !password.isEmpty()){
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        }else {
            loginInfoLabel.setText("Please enter a valid email or password");
            loginInfoLabel.setVisible(true);
        }
    }

    /**
     * Shows sign up UI for authentication
     */
    private void showSignUpUI() {
        Label headerLabel = new Label("Sign Up");
        Button backButton = new Button("Back");

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

        Label phone = new Label("Phone number");
        VBox.setMargin(phone, new Insets(20, 0, 0, 20));
        TextField phoneTextField = new TextField();
        VBox.setMargin(phoneTextField, new Insets(10, 0, 0, 20));

        Label email = new Label("Email");
        VBox.setMargin(email, new Insets(20, 0, 0, 20));
        TextField emailTextField = new TextField();
        VBox.setMargin(emailTextField, new Insets(10, 0, 0, 20));

        Label passwordLabel = new Label("Password");
        VBox.setMargin(passwordLabel, new Insets(20, 0, 0, 20));
        PasswordField passwordField = new PasswordField();
        VBox.setMargin(passwordField, new Insets(10, 0, 0, 20));

        signupInfoLabel = new Label();
        VBox.setMargin(signupInfoLabel, new Insets(20, 0, 0, 20));

        Button signUpButton = new Button("Sign Up");
        VBox.setMargin(signUpButton, new Insets(40, 0, 0, 20));



        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, firstName, firstNameTextField, lastName, lastNameTextField, phone, phoneTextField, email, emailTextField, passwordLabel, passwordField, signupInfoLabel, signUpButton);


        backButton.setOnAction(e -> {
            showDefaultAuthenticationSection();
        });
        signUpButton.setOnAction(e -> {
            validateSignupInput(firstNameTextField, lastNameTextField, phoneTextField, emailTextField, passwordField);

        });
    }

    private void validateSignupInput(TextField firstNameText, TextField lastNameText, TextField phoneText, TextField emailText, PasswordField passwordText){
        String firstName = firstNameText.getText().trim();
        String lastName = lastNameText.getText().trim();
        String phone = phoneText.getText().trim();
        String email = emailText.getText().trim();
        String password = passwordText.getText().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            User newUser = new User(firstName, lastName, phone, email, password);
            authenticateUser(newUser);
            signupInfoLabel.setText("Account created successfully!");
            showLoginUI();
        }else {
            signupInfoLabel.setText("Kindly fill out all fields");
            signupInfoLabel.setVisible(true);
        }


    }

    private void showForgotPasswordUI() {
        Label headerLabel = new Label("Forgot Password");


        HBox topBar = new HBox();
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        topBar.getChildren().addAll(headerLabel);

        VBox.setMargin(topBar, new Insets(40, 0, 0, 20));

        Label description = new Label("Enter your email address below. An email with a reset link will be sent shortly.");
        VBox.setMargin(description, new Insets(20, 0, 0, 20));

        Label email = new Label("Email");
        VBox.setMargin(email, new Insets(60, 0, 0, 20));
        TextField emailTextField = new TextField();
        VBox.setMargin(emailTextField, new Insets(10, 0, 0, 20));

        resetInfoLabel = new Label();
        VBox.setMargin(resetInfoLabel, new Insets(10, 0, 0, 20));
        resetInfoLabel.setVisible(false);

        HBox hbox = new HBox();
        VBox.setMargin(hbox, new Insets(40, 0, 0, 20));

        Button backButton = new Button("Back");
        HBox.setMargin(backButton, new Insets(0, 10, 0, 0));

        Button proceedButton = new Button("Proceed");
        VBox.setMargin(proceedButton, new Insets(0, 10, 0, 20));

        hbox.getChildren().clear();
        hbox.getChildren().addAll(backButton, proceedButton);

        authenticationSection.getChildren().clear();
        authenticationSection.getChildren().addAll(topBar, description, email, emailTextField, resetInfoLabel, hbox);

        backButton.setOnAction(e -> {
            showLoginUI();
        });
        proceedButton.setOnAction(e -> {
            showLoginUI();
           // validateEmailInput(emailTextField);
        });
    }

    /*private void validateEmailInput(TextField emailText){
        String email = emailText.getText().trim();

        if (email.contains("@")) {
            resetPassword(email);
            resetInfoLabel.setText("A password reset link has been sent to the above email address");
            resetInfoLabel.setVisible(true);

        } else {
            resetInfoLabel.setText("Please enter a valid email address");
            resetInfoLabel.setVisible(true);

        }
    }
*/
    private void authenticateUser(User user){
        //TODO
    }

    private void resetPassword(String email){
        // TODO
    }
}
