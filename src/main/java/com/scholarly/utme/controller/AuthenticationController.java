package com.scholarly.utme.controller;

import com.google.gson.Gson;
import com.scholarly.utme.network.NetworkModule;
import com.scholarly.utme.network.model.*;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.AuthenticationScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import okhttp3.*;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.prefs.Preferences;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

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
    private Label signUpEmailError, loginHeaderText, loginEmailText, loginPasswordText, loginContinueText, loginHaveAcctText, loginSignUpText, recoverHeaderText, recoverEmailText, recoverEmailPrompt;

    @FXML
    private Button signUpProceedButton, signUpGoogleButton, signUpFacebookButton, loginProceedButton, loginGoogleButton, loginFacebookButton, recoverProceedButton;

    @FXML
    private TextField signUpEmailField, signUpPasswordField, loginEmailField, loginPasswordField, recoverEmailField;

    @FXML
    private CustomNumberField signUpPhoneField;


    private Preferences userPreferences;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userPreferences = AppPreferences.getPreferences();

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
            recoverEmailPrompt.setVisible(false);
            recoverEmailPrompt.setText("Please enter a valid email address");
            recoverEmailPrompt.setTextFill(Paint.valueOf("#FF0000"));
            recoverEmailField.setText("");
            recoverProceedButton.setText("Proceed");
            Animations.fadeOut(loginSection, 300);
            Animations.fadeIn(recoverPasswordSection, 300);

        });


        Label signUpEmailError = getEmailErrorText();
        Label signUpPasswordError = getPasswordErrorText();
        Label signUpPhoneError = getPhoneErrorText();

        signUpProceedButton.setOnAction(event -> {
            String email = signUpEmailField.getText();
            signUpEmailSection.getChildren().remove(signUpEmailError);
            if (!email.contains("@")) {
                signUpEmailSection.getChildren().add(signUpEmailError);
                return;
            }

            String password = signUpPasswordField.getText();
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


            DeviceInfo deviceInfo = getSystemProperties();
            ReferrerInfo referrerInfo = new ReferrerInfo();
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber(phoneNumber);
            user.setDeviceInfo(deviceInfo);
            user.setReferrerInfo(referrerInfo);

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                signupUser(user, deviceInfo.getPlatform());

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
            }

        });


        Label loginEmailError = getEmailErrorText();
        Label loginPasswordError = getPasswordErrorText();

        loginProceedButton.setOnAction(event -> {
            String email = loginEmailField.getText();
            loginEmailSection.getChildren().remove(loginEmailError);
            if (!loginEmailField.getText().contains("@")) {
                loginEmailSection.getChildren().add(loginEmailError);
                return;
            }

            String password = loginPasswordField.getText();
            loginPasswordSection.getChildren().remove(loginPasswordError);
            if (loginPasswordField.getCharacters().length() < 6) {
                if (!loginPasswordSection.getChildren().contains(loginPasswordError)) {
                    loginPasswordSection.getChildren().add(loginPasswordError);
                }
                return;
            }

            loginEmailSection.getChildren().remove(loginEmailError);
            loginPasswordSection.getChildren().remove(loginPasswordError);


            DeviceInfo deviceInfo = getSystemProperties();
            LoggedInUser user = new LoggedInUser();
            user.setEmail(email);
            user.setPassword(password);
            user.setAppId("");
            user.setDeviceInfo(deviceInfo);

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                loginUser(user);

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
            }

        });

        recoverProceedButton.setOnAction(event -> {

            if (!recoverEmailField.getText().contains("@")) {
                recoverEmailPrompt.setVisible(true);
            } else {
                if (recoverProceedButton.getText().contains("Back")) {
                    Animations.fadeOut(recoverPasswordSection, 300);
                    Animations.fadeIn(loginSection, 300);
                }

                String email = recoverEmailField.getText();
                User user = new User();
                user.setEmail(email);
                user.setAppId(null);

                // Check for internet connectivity
                try {
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    recoverPassword(user);

                } catch (Exception e) {
                    Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                    alertDialog.show();
                    System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
                }

            }
        });

    }

    private void signupUser(User newUser, String platform) {
        String END_POINT = "signup";

        OkHttpClient client = NetworkModule.getHttpClient();

        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("platform", platform)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Got response with code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    AuthResponse signupResponse = gson.fromJson(responseBody.string(), AuthResponse.class);

                    if (signupResponse.getStatus().equalsIgnoreCase("success")) {
                        // TODO: Encrypt and Save token with Java Keystore
                        userPreferences.put(PREF_KEY_ID_TOKEN, signupResponse.getData().getIdToken());
                        userPreferences.put(PREF_KEY_REFRESH_TOKEN, signupResponse.getData().getRefreshToken());
                        System.out.println("Signed up user with ID token -> " + userPreferences.get(PREF_KEY_ID_TOKEN, " ") + "\n AND Refresh Token -> " + userPreferences.get(PREF_KEY_REFRESH_TOKEN, " "));

//                        ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
                        ViewSwitcher.showScreen(View.LANDING_SCREEN);

                    } else if (signupResponse.getStatus().equalsIgnoreCase("error")) {

                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", signupResponse.getMessage(), "");
                            alertDialog.show();
                        });

                    }

                } catch (Exception e) {
                    System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                    alertDialog.show();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void loginUser(LoggedInUser user) {
        String END_POINT = "login";

        OkHttpClient client = NetworkModule.getHttpClient();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Got response with code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    AuthResponse authResponse = gson.fromJson(responseBody.string(), AuthResponse.class);

                    if (authResponse.getStatus().equalsIgnoreCase("success")) {
                        // TODO: Encrypt and Save token with Java Keystore
                        userPreferences.put(PREF_KEY_LOGIN_CUSTOM_TOKEN, authResponse.getData().getToken());
                        System.out.println(TAG + "Logged in user with custom token " + userPreferences.get(PREF_KEY_LOGIN_CUSTOM_TOKEN, ""));

//                        ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
                        ViewSwitcher.showScreen(View.LANDING_SCREEN);

                    } else if (authResponse.getStatus().equalsIgnoreCase("error")) {

                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", authResponse.getMessage(), "");
                            alertDialog.show();
                        });

                    }

                } catch (Exception e) {
                    System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                    alertDialog.show();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void recoverPassword(User user) {
        String END_POINT = "password-reset/send-email";

        OkHttpClient client = NetworkModule.getHttpClient();

        Gson gson = new Gson();
        String json = gson.toJson(user);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Got response with code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    AuthResponse authResponse = gson.fromJson(responseBody.string(), AuthResponse.class);

                    if (authResponse.getStatus().equalsIgnoreCase("success")) {

                        Platform.runLater(() -> {
                            recoverEmailPrompt.setVisible(true);
                            recoverEmailPrompt.setText("A password reset link has been sent to the above registered email");
                            recoverEmailPrompt.setTextFill(Paint.valueOf("#053500"));
                            recoverProceedButton.setText("Back to Login");

                        });
                        System.out.println(TAG + "Got response with message -> " + authResponse.getMessage());

                    } else if (authResponse.getStatus().equalsIgnoreCase("error")) {

                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", authResponse.getMessage(), "");
                            alertDialog.show();
                        });

                    }

                } catch (Exception e) {
                    System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                    alertDialog.show();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private DeviceInfo getSystemProperties() {

        Properties properties = System.getProperties();

        DeviceInfo deviceInfo = new DeviceInfo();

        String deviceName = properties.getProperty("os.name");
        String deviceId = "";
        if (deviceName.contains("Windows")) {
            deviceId = DeviceInfo.getWindowsDeviceUUID();
            System.out.println("Got device ID -> " + deviceId);
        } else if (deviceName.contains("Mac")) {
            deviceId = DeviceInfo.getWindowsDeviceUUID();
            System.out.println("Got device ID -> " + deviceId);
        }

        deviceInfo.setName(deviceName);
        deviceInfo.setPlatform("windows");
        deviceInfo.setFormFactor("desktop");
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setAppVersionName("1.0.0");


//        System.out.println(TAG + "Got device ID with OS name -> " + properties.getProperty("os.name") + " AND arch -> " + properties.getProperty("os.arch") + " AND username -> " + properties.getProperty("user.name"));

        return deviceInfo;
    }

    private Label getEmailErrorText() {
        Label error = new Label("Please enter a valid email address");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
    }

    private Label getPasswordErrorText() {
        Label error = new Label("Your password must be more than 6 characters");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
    }

    private Label getPhoneErrorText() {
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
        recoverEmailPrompt.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
    }
}