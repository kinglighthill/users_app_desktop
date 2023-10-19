package com.scholarly.utme.controller;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.*;
import com.scholarly.utme.network.model.request.UserRequest;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.AuthenticationScreenVM;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.*;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.network.model.DeviceInfo.getSystemProperties;
import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/AuthenticationScreen.fxml")
public class AuthenticationController implements FxmlView<AuthenticationScreenVM>, Initializable {
    private static final String TAG = "AuthenticationController: ";

    @FXML
    private StackPane authenticationSection;
    @FXML
    private VBox dimmer, signUpSection, loginSection, recoverPasswordSection, signUpNameSection, signUpEmailSection, signUpPasswordSection, signUpPhoneSection, loginEmailSection, loginPasswordSection;
    @FXML
    private ImageView imageView, appIcon;
    @FXML
    private ProgressIndicator progressBar;
    @FXML
    private Label scholarlyText, beTheBestText, signUpHeaderText, signUpNameText, signUpEmailText, signUpPasswordText, signUpPhoneText, signUpContinueText, signUpHaveAccountText, signUpLoginText, forgotPasswordText, resetPasswordText;
    @FXML
    private Label signUpEmailError, loginHeaderText, loginEmailText, loginPasswordText, loginContinueText, loginHaveAcctText, loginSignUpText, recoverHeaderText, recoverEmailText, recoverEmailPrompt, recoverLoginText;
    @FXML
    private Button signUpProceedButton, signUpGoogleButton, signUpFacebookButton, loginProceedButton, loginGoogleButton, loginFacebookButton, recoverProceedButton;
    @FXML
    private TextField signUpNameField, signUpEmailField, signUpPasswordField, loginEmailField, loginPasswordField, recoverEmailField;
    @FXML
    private CustomNumberField signUpPhoneField;


    private final OkHttpClient httpClient = NetworkService.getHttpClient();

    interface ServerCallback {
        void stopServer();
        void redirect();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            MainApplication.logInfo("Initializing authentication controller");
            boolean showSignUpScreen = getInitialData().showSignUpScreen;
            if (showSignUpScreen) {
                Animations.fadeIn(signUpSection, 300);
            } else {
                Animations.fadeIn(loginSection, 300);
            }

            boolean internetEnabled = checkNetworkConnectivity();
            System.out.println(TAG + "Internet Enabled -> " + internetEnabled);

            initializeViews();
            initializeFonts();

            signUpLoginText.setOnMouseClicked(event -> {
                Animations.fadeOut(signUpSection, 200);
                Animations.fadeIn(loginSection, 300);
            });
            signUpLoginText.setOnMouseEntered(event -> signUpLoginText.setUnderline(true));
            signUpLoginText.setOnMouseExited(event -> signUpLoginText.setUnderline(false));

            loginSignUpText.setOnMouseClicked(event -> {
                Animations.fadeOut(loginSection, 200);
                Animations.fadeIn(signUpSection, 300);
            });
            loginSignUpText.setOnMouseEntered(event -> loginSignUpText.setUnderline(true));
            loginSignUpText.setOnMouseExited(event -> loginSignUpText.setUnderline(false));

            resetPasswordText.setOnMouseClicked(event -> {
                recoverEmailPrompt.setVisible(false);
                recoverEmailPrompt.setText("Please enter a valid email address");
                recoverEmailPrompt.setTextFill(Paint.valueOf("#FF0000"));
                recoverEmailField.setText("");
                recoverProceedButton.setText("Proceed");
                Animations.fadeOut(loginSection, 300);
                Animations.fadeIn(recoverPasswordSection, 300);

            });
            resetPasswordText.setOnMouseEntered(event -> resetPasswordText.setUnderline(true));
            resetPasswordText.setOnMouseExited(event -> resetPasswordText.setUnderline(false));

            recoverLoginText.setOnMouseClicked(event -> {
                Animations.fadeOut(recoverPasswordSection, 300);
                Animations.fadeIn(loginSection, 300);
            });
            recoverLoginText.setOnMouseEntered(event -> recoverLoginText.setUnderline(true));
            recoverLoginText.setOnMouseExited(event -> recoverLoginText.setUnderline(false));


            Label signUpNameError = getNameErrorText();
            Label signUpEmailError = getEmailErrorText();
            Label signUpPasswordError = getPasswordErrorText();
            Label signUpPhoneError = getPhoneErrorText();

            signUpProceedButton.setOnAction(event -> {

                String fullName = signUpNameField.getText();
                signUpNameSection.getChildren().remove(signUpNameError);
                if (fullName.split(" ").length == 1) {
                    signUpNameSection.getChildren().add(signUpNameError);
                    return;
                }

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

                signUpProceedButton.setDisable(true);
                showProgressBar();

                signUpEmailSection.getChildren().remove(signUpNameError);
                signUpEmailSection.getChildren().remove(signUpEmailError);
                signUpPasswordSection.getChildren().remove(signUpPasswordError);
                signUpPhoneSection.getChildren().remove(signUpPhoneError);

                ReferrerInfo referrerInfo = new ReferrerInfo();
                UserRequest signupRequest = new UserRequest(fullName, email, phoneNumber, password, "nigeria", "fcm-token", "utme", false, null, getSystemProperties(), referrerInfo);

                System.out.println(TAG + "Internet enabled -> " + internetEnabled);
                if (internetEnabled) {
                    Task<Void> signupTask = new Task<>() {
                        @Override
                        protected Void call() {
                            signupUser(signupRequest);
                            return null;
                        }
                    };
                    Thread signupThread = new Thread(signupTask);
                    signupThread.start();
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

                loginProceedButton.setDisable(true);
                showProgressBar();

                loginEmailSection.getChildren().remove(loginEmailError);
                loginPasswordSection.getChildren().remove(loginPasswordError);

                UserRequest loginRequest = new UserRequest();
                loginRequest.setEmail(email);
                loginRequest.setPassword(password);
                loginRequest.setAppSlug("utme");
                loginRequest.setDeviceInfo(getSystemProperties());

                // Check for internet connectivity
                try {
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    Task<Void> loginTask = new Task<>() {
                        @Override
                        protected Void call() {
                            loginUser(loginRequest);
                            return null;
                        }
                    };
                    Thread loginThread = new Thread(loginTask);
                    loginThread.start();

                } catch (Exception e) {
                    Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                    alertDialog.show();
                    hideProgressBar();
                    System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
                }

            });

            recoverProceedButton.setOnAction(event -> {

                if (!recoverEmailField.getText().contains("@")) {
                    recoverEmailPrompt.setVisible(true);
                } else {

                    recoverProceedButton.setDisable(true);
                    showProgressBar();

                    String email = recoverEmailField.getText();
                    String appSlug = "utme";

                    UserRequest request = new UserRequest();
                    request.setEmail(email);
                    request.setAppSlug(appSlug);

                    // Check for internet connectivity
                    try {
                        URL url = new URL(BASE_URL);
                        URLConnection connection = url.openConnection();
                        connection.connect();

                        Task<Void> recoverTask = new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                recoverPassword(request);
                                return null;
                            }
                        };
                        Thread recoverThread = new Thread(recoverTask);
                        recoverThread.start();

                    } catch (Exception e) {
                        Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                        alertDialog.show();
                        hideProgressBar();
                        System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
                    }

                }
            });

            signUpGoogleButton.setOnAction(event -> {

                signUpGoogleButton.setDisable(true);
                showProgressBar();

                // Check for internet connectivity
                try {
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    Task<Void> signInGoogleTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            signInWithGoogle();
                            return null;
                        }
                    };
                    Thread background = new Thread(signInGoogleTask);
                    background.start();

                } catch (Exception e) {
                    Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                    alertDialog.show();
                    hideProgressBar();
                    System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
                }
            });

            loginGoogleButton.setOnAction(event -> {

                loginGoogleButton.setDisable(true);
                showProgressBar();

                // Check for internet connectivity
                try {
                    URL url = new URL(BASE_URL);
                    URLConnection connection = url.openConnection();
                    connection.connect();

                    Task<Void> loginGoogleTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            signInWithGoogle();
                            return null;
                        }
                    };
                    Thread background = new Thread(loginGoogleTask);
                    background.start();

                } catch (Exception e) {
                    Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                    alertDialog.show();
                    hideProgressBar();
                    System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());

                }
            });

            MainApplication.logInfo("Done");
        } catch (Exception e) {
            MainApplication.log(e);
        }
    }

    private void signupUser(UserRequest newUser) {
        String END_POINT = "signup";

        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("platform", newUser.getDeviceInfo().getPlatform())
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse signupResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (signupResponse.getStatus().equalsIgnoreCase("success")) {

                        String userId = signupResponse.getData().getUserData().getId();
                        String userData = gson.toJson(signupResponse.getData().getUserData());

                        PreferencesManager.put(PREF_KEY_USER_ID, userId);
                        PreferencesManager.put(PREF_KEY_USER_DATA+userId, userData);
                        PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, signupResponse.getData().getAccessToken());
                        PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, signupResponse.getData().getRefreshToken());
                        PreferencesManager.putBoolean(PREF_KEY_ACTIVATION_STATE+userId, signupResponse.getData().getActivationState().isActivationActive());
                        PreferencesManager.put(PREF_KEY_ACTIVATE_MESSAGE, signupResponse.getData().getActivationState().getMessage());

                        System.out.println(TAG + "Signed up user with id -> " + userId);
                        System.out.println(TAG + "Signed up user with User Activation State -> " + PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false));

                        Platform.runLater(() -> {
                            hideProgressBar();
                            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                            ViewSwitcher.showScreen(View.LANDING_SCREEN);
                        });

                    } else if (signupResponse.getStatus().equalsIgnoreCase("error")) {
                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", signupResponse.getMessage(), "");
                            alertDialog.show();
                            hideProgressBar();
                        });

                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", "Could not sign up because " + e.getMessage(), "");
                    alertDialog.show();
                    hideProgressBar();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });
    }

    private void signupUser(String authCode, String redirectUri, ServerCallback callback) {
        String END_POINT = "signup/google";

        ReferrerInfo referrerInfo = new ReferrerInfo();
        GoogleUser user = new GoogleUser("nigeria", "fcm-token", "utme", authCode, redirectUri, getSystemProperties(), referrerInfo);

        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("platform", user.getDeviceInfo().getPlatform())
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse signupResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (signupResponse.getStatus().equalsIgnoreCase("success")) {
                        String userId = signupResponse.getData().getUserData().getId();
                        String userData = gson.toJson(signupResponse.getData().getUserData());

                        PreferencesManager.put(PREF_KEY_USER_ID, userId);
                        PreferencesManager.put(PREF_KEY_USER_DATA+userId, userData);
                        PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, signupResponse.getData().getAccessToken());
                        PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, signupResponse.getData().getRefreshToken());
                        PreferencesManager.putBoolean(PREF_KEY_ACTIVATION_STATE+userId, signupResponse.getData().getActivationState().isActivationActive());
                        PreferencesManager.put(PREF_KEY_ACTIVATE_MESSAGE, signupResponse.getData().getActivationState().getMessage());

                        System.out.println(TAG + "Google signup user with id -> " + userId);
                        System.out.println(TAG + "Google signup user with Activation State -> " + PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false));

                        callback.redirect();

                        Platform.runLater(() -> {
                            hideProgressBar();
                            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                            ViewSwitcher.showScreen(View.LANDING_SCREEN);
                        });

                    } else if (signupResponse.getStatus().equalsIgnoreCase("error")) {
                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", signupResponse.getMessage(), "");
                            alertDialog.show();
                            hideProgressBar();
                        });
                        callback.stopServer();
                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                }
                callback.stopServer();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.stopServer();
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                    alertDialog.show();
                    hideProgressBar();
                });
                System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
            }
        });
    }

    private void loginUser(UserRequest user) {
        String END_POINT = "login";

        Gson gson = new Gson();
        String json = gson.toJson(user);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("platform", user.getDeviceInfo().getPlatform())
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);

        try(Response response = call.execute()) {
            try (ResponseBody responseBody = response.body()) {
                assert responseBody != null;
                BaseResponse loginResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                if (loginResponse.getStatus().equalsIgnoreCase("success")) {

                    String userId = loginResponse.getData().getUserData().getId();
                    String userData = gson.toJson(loginResponse.getData().getUserData());

                    PreferencesManager.put(PREF_KEY_USER_ID, userId);
                    PreferencesManager.put(PREF_KEY_USER_DATA+userId, userData);
                    PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, loginResponse.getData().getAccessToken());
                    PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, loginResponse.getData().getRefreshToken());
                    PreferencesManager.putBoolean(PREF_KEY_ACTIVATION_STATE+userId, loginResponse.getData().getActivationState().isActivationActive());
                    PreferencesManager.put(PREF_KEY_ACTIVATE_MESSAGE, loginResponse.getData().getActivationState().getMessage());

                    System.out.println(TAG + "Logged in user with id -> " + PreferencesManager.get(PREF_KEY_USER_ID, ""));
                    System.out.println(TAG + "Logged in user with Activation State -> " + PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false));

                    Platform.runLater(() -> {
                        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
                        ViewSwitcher.showScreen(View.LANDING_SCREEN);
                        hideProgressBar();
                    });

                } else if (loginResponse.getStatus().equalsIgnoreCase("error")) {
                    Platform.runLater(() -> {
                        Alert alertDialog = Alerts.info(getClass(), "Error", loginResponse.getMessage(), "");
                        alertDialog.show();
                        hideProgressBar();
                    });

                }

            } catch (Exception e) {
                System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
            }
        } catch (Exception e) {
            Platform.runLater(() -> {
                Alert alertDialog = Alerts.info(getClass(), "Error", "Could not connect because " + e.getMessage(), "");
                alertDialog.show();
                hideProgressBar();
            });
            System.out.println("Request failed with exception -> " + e.getMessage());
        }

    }

    private void recoverPassword(UserRequest user) {
        String END_POINT = "password-reset/send-email";

        Gson gson = new Gson();
        String json = gson.toJson(user);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("platform", getSystemProperties().getPlatform())
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                        Platform.runLater(() -> {
                            recoverEmailPrompt.setVisible(true);
                            recoverEmailPrompt.setText(baseResponse.getMessage());
                            recoverEmailPrompt.setTextFill(Paint.valueOf("#053500"));

                            hideProgressBar();
                        });
                    } else if (baseResponse.getStatus().equalsIgnoreCase("error")) {

                        Platform.runLater(() -> {
                            recoverEmailPrompt.setVisible(true);
                            recoverEmailPrompt.setText(baseResponse.getMessage());
                            hideProgressBar();
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
                    hideProgressBar();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void signInWithGoogle() {
        final HttpServer server;
        MainApplication application = new MainApplication();
        InetAddress ipaddress = InetAddress.getLoopbackAddress(); // returns 127.0.0.1

        String state = RandomStringUtils.random(6, true, false);
        String scope = "email profile";
        String responseType = "code";
        String clientId = "671999041043-p3grlgbnvrn3ph5fvkf4b52h5vq1oii7.apps.googleusercontent.com";

        try {
            server = HttpServer.create(new InetSocketAddress(ipaddress, 0), 0);

            String redirectUri = "http://" + server.getAddress().getHostName() + ":" + server.getAddress().getPort();

            server.start();

            String authorizationRequest = "https://accounts.google.com/o/oauth2/v2/auth?scope=" + scope + "&response_type=" + responseType + "&state=" + state + "&redirect_uri=" + redirectUri + "&client_id=" + clientId;

            application.openBrowser(authorizationRequest);

            HttpContext responseContext = server.createContext("/");
            responseContext.setHandler(new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String uriResponse = exchange.getRequestURI().getQuery();

                    if (uriResponse.contains("code")) {
                        String code = uriResponse.substring(uriResponse.indexOf("code"), uriResponse.indexOf("scope")-1);
                        String authCode = code.substring(uriResponse.indexOf("="));

                        signupUser(authCode, redirectUri, new ServerCallback() {
                            @Override
                            public void stopServer() {
                                server.stop(60);
                            }

                            @Override
                            public void redirect() {
                                try {
                                    byte[] response = "<html><body>Login successful. Go back to the app</body></html>".getBytes();
                                    exchange.sendResponseHeaders(200, response.length);
                                    OutputStream os = exchange.getResponseBody();
                                    os.write(response);
                                    os.close();
                                } catch (IOException exception) {
                                    System.out.println(exception.getMessage());
                                }
                            }
                        });

                    } else {
                        server.stop(60);
                        Platform.runLater(() -> {
                            Alert alertDialog = Alerts.info(getClass(), "Error", "Could not sign in with Google", "");
                            alertDialog.show();
                            hideProgressBar();
                        });
                    }
                }
            });

        } catch (IOException e) {
            Platform.runLater(() -> {
                Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                alertDialog.show();
                hideProgressBar();
            });
            System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
        }

    }

    private void hideProgressBar() {
        signUpProceedButton.setDisable(false);
        loginProceedButton.setDisable(false);
        signUpGoogleButton.setDisable(false);
        loginGoogleButton.setDisable(false);
        recoverProceedButton.setDisable(false);
        dimmer.setVisible(false);
        progressBar.setVisible(false);
    }

    private void showProgressBar() {
        dimmer.setVisible(true);
        progressBar.setVisible(true);
        AnchorPane.setTopAnchor(progressBar, dimmer.getHeight()/2);
        AnchorPane.setLeftAnchor(progressBar, dimmer.getWidth()/2);
    }

    private Label getNameErrorText() {
        Label error = new Label("Enter your first name and last name separated by a space");
        error.setTextFill(Paint.valueOf("#FF0000"));
        error.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        return error;
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

        /*ImageView facebookImage = new ImageView(new Image(getClass().getResource("/drawable/facebook_icon.png").toString()));
        signUpFacebookButton.setGraphic(facebookImage);
        signUpFacebookButton.setGraphicTextGap(20);
        signUpFacebookButton.setBackground(Background.EMPTY);
        ImageView facebookImage2 = new ImageView(new Image(getClass().getResource("/drawable/facebook_icon.png").toString()));
        loginFacebookButton.setGraphic(facebookImage2);
        loginFacebookButton.setGraphicTextGap(20);
        loginFacebookButton.setBackground(Background.EMPTY);*/
    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 30));
        beTheBestText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 26));

        signUpHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        signUpNameText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpNameField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPhoneText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpPhoneField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        signUpContinueText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpGoogleButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        signUpFacebookButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpHaveAccountText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        signUpLoginText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        recoverLoginText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));


        loginHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        loginEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        loginContinueText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginGoogleButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
//        loginFacebookButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginHaveAcctText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        loginSignUpText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        forgotPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        resetPasswordText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));


        recoverProceedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 24));
        recoverEmailText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverLoginText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverEmailField.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
        recoverEmailPrompt.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
    }

    private boolean checkNetworkConnectivity() {
        try {
            URL url = new URL(BASE_URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private InitialData getInitialData() {
        return (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private boolean showSignUpScreen;

        public InitialData(boolean showSignUpScreen) {
            this.showSignUpScreen = showSignUpScreen;
        }

        public boolean isShowSignUpScreen() {
            return showSignUpScreen;
        }
    }

}