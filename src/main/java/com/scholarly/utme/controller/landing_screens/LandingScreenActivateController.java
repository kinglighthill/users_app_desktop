package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.ActivationInfo;
import com.scholarly.utme.network.model.RefreshRequest;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.network.model.DeviceInfo;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenActivateVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import okhttp3.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;
import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;

@FxmlPath("/layouts/landing_screens/landing_screen_activate.fxml")
public class LandingScreenActivateController implements FxmlView<LandingScreenActivateVM>, Initializable {
    private static final String TAG = "LandingScreenActivateController: ";

    @InjectViewModel
    private LandingScreenActivateVM viewModel;

    @FXML
    private Pane dialogDimmer;
    @FXML
    private StackPane notActivatedPane;
    @FXML
    private ProgressIndicator progressBar;
    @FXML
    private ImageView padlockIcon, atmCardImage, activationSuccessfulImage;
    @FXML
    private VBox activationSuccessfulPane, centerVBox, innerVBox;
    @FXML
    private CustomNumberField activationPinTextField;
    @FXML
    private Label incorrectPinError, activationSuccessfulMessage, activationText, headerLabel, whatsAppNumber;
    @FXML
    private Button activateButton, buyPinButton, continueButton;

    private final OkHttpClient httpClient = NetworkService.getHttpClient();


    private static int MAX_PIN_LENGTH = 16;
    private static int SPACE_INTERVAL = 4;

    MainApplication application = new MainApplication();

    interface NetworkCallback {
        void resendRequest();
        void refreshToken();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        centerVBox.getChildren().remove(notActivatedPane);
        innerVBox.getChildren().remove(activationText);
        if (viewModel.isActivated()) {
            centerVBox.getChildren().remove(notActivatedPane);
            innerVBox.getChildren().remove(activationText);
            activationPinTextField.setDisable(true);
            activateButton.setDisable(true);
            headerLabel.setText("Your app has been activated!");
        } else {
            activationPinTextField.setDisable(false);
            activateButton.setDisable(false);
            centerVBox.getChildren().add(0, notActivatedPane);
            innerVBox.getChildren().add(innerVBox.getChildren().size(), activationText);
            headerLabel.setText("Enter your 16 digits activation pin to get unlimited access to the app's content");
        }

        activateButton.setDisable(
                isActivationPinValid(activationPinTextField.getText(), false)
        );

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (isActivationPinValid(text, true)) {
                return null;
            }

            return change;
        });

        activationPinTextField.setTextFormatter(textFormatter);

        activationPinTextField.textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    String formattedText = Helper.addRemoveSpaces(newValue, MAX_PIN_LENGTH, SPACE_INTERVAL);
                    activationPinTextField.setText(formattedText);
                    activationPinTextField.positionCaret(formattedText.length());
                    activateButton.setDisable(isActivationPinValid(newValue, false));
                });

        activateButton.setOnAction(event -> {
            showProgressBar();
            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                String END_POINT = "activations";
                String userId = viewModel.getUserId();

                String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                String encodedDeviceId = DeviceInfo.getSystemProperties().getDeviceId();

                ActivationInfo activationInfo = new ActivationInfo(
                        activationPinTextField.getText().replace(" ", ""),
                        encodedDeviceId
                );

                Gson gson = new Gson();
                String json = gson.toJson(activationInfo);

                RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                Request request = new Request.Builder()
                        .url(BASE_URL + END_POINT)
                        .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                        .put(requestBody)
                        .build();

                activateUser(request, new NetworkCallback() {
                    @Override
                    public void refreshToken() {
                        System.out.println(TAG + "Refreshing token...");
                        String REFRESH_TOKEN = PreferencesManager.get(PREF_KEY_REFRESH_TOKEN+userId, "");
                        RefreshRequest refreshTokenRequest = new RefreshRequest(REFRESH_TOKEN);

                        String refreshJson = gson.toJson(refreshTokenRequest);

                        RequestBody refreshRequestBody = RequestBody.create(JSON_BODY_TYPE, refreshJson);

                        Request refreshRequest = new Request.Builder()
                                .url(REFRESH_URL)
                                .post(refreshRequestBody)
                                .build();

                        Call call = httpClient.newCall(refreshRequest);

                        try(Response response = call.execute()) {
                            if (response.code() == 200) {
                                try(ResponseBody responseBody = response.body()) {
                                    assert responseBody != null;
                                    BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                    if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                                        System.out.println(TAG + "Refreshed Token for user with id -> " + refreshResponse.getData().getUserId());

                                        PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                        PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                        System.out.println(TAG + "Refreshed Token New Access Token -> " + PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, ""));

                                    } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
                                            alertDialog.show();

                                        });
                                    }
                                } catch (Exception e) {
                                    Platform.runLater(() -> {
                                        hideProgressBar();
                                        Alert alertDialog = Alerts.info(getClass(), "Error", "Something went wrong. Try again later!", "");
                                        alertDialog.show();
                                    });
                                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                                }

                                response.close();
                            }
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                                alertDialog.show();
                            });
                        }
                    }
                    @Override
                    public void resendRequest() {
                        System.out.println(TAG + "Resending request...");

                        String NEW_ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                        Request request = new Request.Builder()
                                .url(BASE_URL + END_POINT)
                                .addHeader("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                                .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                                .put(requestBody)
                                .build();

                        Call call = httpClient.newCall(request);

                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                try (ResponseBody responseBody = response.body()) {
                                    assert responseBody != null;
                                    BaseResponse activationResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                                    if (activationResponse.getStatus().equalsIgnoreCase("success")) {

                                        PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, activationResponse.getData().getAccessToken());
                                        PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, activationResponse.getData().getRefreshToken());
                                        PreferencesManager.putBoolean(PREF_KEY_ACTIVATION_STATE+userId, true);

                                        Platform.runLater(() -> {
                                            progressBar.setVisible(false);
                                            Animations.showDialog(activationSuccessfulPane, dialogDimmer);
                                        });

                                    } else if (activationResponse.getStatus().equalsIgnoreCase("error")) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", activationResponse.getMessage(), "");
                                            alertDialog.show();
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
                                    Alert alertDialog = Alerts.info(getClass(), "Error", "Could not connect because " + e.getMessage(), "");
                                    alertDialog.show();
                                    hideProgressBar();
                                });
                                System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
                            }
                        });
                    }
                });

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                hideProgressBar();
                System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
            }
        });

        whatsAppNumber.setOnMouseClicked(event -> {
            application.openBrowser(WHATSAPP_URL);
        });
        whatsAppNumber.setOnMouseEntered(event -> whatsAppNumber.setUnderline(true));
        whatsAppNumber.setOnMouseExited(event -> whatsAppNumber.setUnderline(false));

        buyPinButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.ACTIVATE_PAYMENT_SCREEN);
        });

        continueButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void activateUser(Request request, NetworkCallback callback) {
        Gson gson = new Gson();

        String userId = viewModel.getUserId();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    callback.refreshToken();
                    callback.resendRequest();
                } else {
                    try (ResponseBody responseBody = response.body()) {
                        assert responseBody != null;
                        BaseResponse activationResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                        if (activationResponse.getStatus().equalsIgnoreCase("success")) {

                            PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, activationResponse.getData().getAccessToken());
                            PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, activationResponse.getData().getRefreshToken());
                            PreferencesManager.putBoolean(PREF_KEY_ACTIVATION_STATE+userId, true);

                            Platform.runLater(() -> {
                                progressBar.setVisible(false);
                                Animations.showDialog(activationSuccessfulPane, dialogDimmer);
                            });

                        } else if (activationResponse.getStatus().equalsIgnoreCase("error")) {
                            Platform.runLater(() -> {
                                hideProgressBar();
                                Alert alertDialog = Alerts.info(getClass(), "Error", activationResponse.getMessage(), "");
                                alertDialog.show();
                            });
                        }
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            hideProgressBar();
                            Alert alertDialog = Alerts.info(getClass(), "Error", "Something went wrong. Try again later!", "");
                            alertDialog.show();
                        });
                        System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    hideProgressBar();
                    Alert alertDialog = Alerts.info(getClass(), "Error", "Could not connect because " + e.getMessage(), "");
                    alertDialog.show();
                });
                System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void showProgressBar() {
        Animations.showDialog(progressBar, dialogDimmer);
    }

    private void hideProgressBar() {
        Animations.hideDialog(progressBar, dialogDimmer);
    }

    private void initializeViews() {
        padlockIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/padlock_icon.png").toString()));
        atmCardImage.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/credit_card_image.png").toString()));
//        activationSuccessfulCloseIcon.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/new_challenge_close_icon.png").toString()));
        activationSuccessfulImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/successful_image.png").toString()));

    }

    private void initializeFonts() {

    }

    private boolean isActivationPinValid(String pin, boolean equals) {
        if (equals) {
            return pin.replace(" ", "").length() - 1 == MAX_PIN_LENGTH;
        } else {
            return pin.replace(" ", "").length() < MAX_PIN_LENGTH;
        }
    }
}
