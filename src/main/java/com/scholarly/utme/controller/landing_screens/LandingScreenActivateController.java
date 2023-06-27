package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.ActivationInfo;
import com.scholarly.utme.network.model.BaseResponse;
import com.scholarly.utme.network.model.DeviceInfo;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Helper;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenActivateVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;
import static com.scholarly.utme.util.Constants.PREF_KEY_ACTIVATION_STATE;

@FxmlPath("/layouts/landing_screens/landing_screen_activate.fxml")
public class LandingScreenActivateController implements FxmlView<LandingScreenActivateVM>, Initializable {
    private static final String TAG = "LandingScreenActivateController: ";

    private static final String BEARER_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjU0NWUyNDZjNTEwNmExMGQ2MzFiMTA0M2E3MWJiNTllNWJhMGM5NGQiLCJ0eXAiOiJKV1QifQ.eyJ1dWlkIjoiakFiaXBFY1BHZ0VkaTB2VTI1SlYiLCJlbWFpbF9hZGRyZXNzIjoiam9obmRvZUBnbWFpbC5jb20iLCJjb3VudHJ5IjoibmlnZXJpYSIsImlzX2FjdGl2YXRpb25fYWN0aXZlIjp0cnVlLCJkZXZpY2VfaWQiOiJkZXZpY2UtaWQiLCJhcHBfc2x1ZyI6InV0bWUiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vc2Nob2xhcmx5LXV0bWUtc3RhZ2luZyIsImF1ZCI6InNjaG9sYXJseS11dG1lLXN0YWdpbmciLCJhdXRoX3RpbWUiOjE2ODYzNTU0MzgsInVzZXJfaWQiOiJqQWJpcEVjUEdnRWRpMHZVMjVKViIsInN1YiI6ImpBYmlwRWNQR2dFZGkwdlUyNUpWIiwiaWF0IjoxNjg2MzU1NDM4LCJleHAiOjE2ODYzNTkwMzgsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnt9LCJzaWduX2luX3Byb3ZpZGVyIjoiY3VzdG9tIn19.MrIVwiOYFv2vz8-GdoPs8hjhprVWj_9LHOwZmGTlF_9ESiSe8nL5fZLEt1SBXaBMl_XVaJ3waPEL7J7XmDlmpaTynC2nGGxYAVxl2Zqqj9fB_ihRlWDrnsOtCf2Y6WIdI-x1ad4XOGDfQmN1qhGdrBsamK2dEDWtCj1G4slBNc-QmnXBPT_lUUmWG6JxXpeaZLoCuuyccP0YEnjJguEPK0-RNJDX2xTD0aEH_ioIRHu0qNLHK5-4fwf46JdHO78d6e1XGhB9XMjjuU1FOzS7NSwhTrMBiKEhoAZlKlVJKM9emyrouoKUseGIjpwT9Z69_feQ80EIyWPsik0ZzL1h5A";
    private String ACCESS_TOKEN = "";

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
    private TextField activationPinTextField;
    @FXML
    private Label incorrectPinError, activationSuccessfulMessage, activationText;
    @FXML
    private Button activateButton, buyPinButton, loginButton;

    private Preferences preferences = AppPreferences.getPreferences();
    private OkHttpClient httpClient = NetworkService.getHttpClient();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");
//        System.out.println(TAG + "Refresh Token -> " + preferences.get(PREF_KEY_REFRESH_TOKEN, ""));

        initializeViews();
        initializeFonts();

        centerVBox.getChildren().remove(notActivatedPane);
        innerVBox.getChildren().remove(activationText);
        if (preferences.getBoolean(PREF_KEY_ACTIVATION_STATE, false)) {
            centerVBox.getChildren().remove(notActivatedPane);
            innerVBox.getChildren().remove(activationText);
        } else {
            centerVBox.getChildren().add(0, notActivatedPane);
            innerVBox.getChildren().add(innerVBox.getChildren().size(), activationText);
        }

        activateButton.setOnAction(event -> {
//            if (activationPinTextField.getCharacters().length() < 16) {
//                incorrectPinError.setVisible(true);
//            } else {
//                Animations.showDialog(paymentSuccessfulPane, dialogDimmer);
//            }
            DeviceInfo deviceInfo = DeviceInfo.getSystemProperties();

            showProgressBar();
            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                Task<Void> activateTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        activateUser(activationPinTextField.getText(), deviceInfo.getDeviceId());
                        return null;
                    }
                };
                Thread activateThread = new Thread(activateTask);
                activateThread.start();

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                hideProgressBar();
                System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
            }
        });

        buyPinButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.ACTIVATE_PAYMENT_SCREEN);
        });

//        activationSuccessfulCloseIcon.setOnMouseClicked(mouseEvent -> {
//            Animations.hideDialog(activationSuccessfulPane, dialogDimmer);
//        });

        loginButton.setOnAction(event -> {
            ViewSwitcher.passData(false);
            ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
        });
    }

    private void activateUser(String pin, String deviceId) {
        String END_POINT = "activations";

        ActivationInfo activationInfo = new ActivationInfo(pin, deviceId);

        Gson gson = new Gson();
        String json = gson.toJson(activationInfo);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                .put(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(TAG + "Activate: Got Request -> " + request);
                System.out.println(TAG + "Activate: Got Request Body -> " + requestBody);
                System.out.println(TAG + "Activate: Got response code -> " + response.code());
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse activationResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (activationResponse.getStatus().equalsIgnoreCase("success")) {
                        // TODO: Encrypt and Save token with Java Keystore
                        preferences.put(PREF_KEY_ACTIVATE_ACCESS_TOKEN, activationResponse.getData().getAccessToken());
                        preferences.putBoolean(PREF_KEY_ACTIVATION_STATE, true);

                        Platform.runLater(() -> {
                            progressBar.setVisible(false);
//                            ViewSwitcher.passData(new LandingScreenController.InitialData("homeScreen"));
//                            ViewSwitcher.showScreen(View.LANDING_SCREEN);
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
                System.out.println("Request failed with exception -> " + e.getMessage());
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
}
