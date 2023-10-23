package com.scholarly.utme.controller;

import com.google.gson.Gson;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.DeviceInfo;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.ui.cellFactories.AppGridCellFactory;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.AppsGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import okhttp3.*;
import org.controlsfx.control.GridView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.scholarly.utme.util.Constants.BASE_URL;
import static com.scholarly.utme.util.Constants.PREF_KEY_ACCESS_TOKEN;

@FxmlPath("/layouts/AppsGridScreen.fxml")
public class AppsGridScreenController implements FxmlView<AppsGridScreenVM>, Initializable {
    private static final String TAG = "AppsGridScreenController: ";

    @InjectViewModel
    private AppsGridScreenVM viewModel;

    @FXML
    private GridView<AppItem> appsGrid;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView searchIcon;
    @FXML
    private Button backButton;
    @FXML
    private Label appsTitle;

    private static final List<AppItem> appItems = FXCollections.observableArrayList();

    private final OkHttpClient httpClient = NetworkService.getHttpClient();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (getInitialData().type.equalsIgnoreCase("Mobile")) {
            if (appItems.isEmpty()) {
                System.out.println(TAG + "App Items is empty");
                loadMobileApps();
            } else {
                System.out.println(TAG + "App Items is not empty");
                appsGrid.setItems(FXCollections.observableArrayList(appItems));
            }
        } else {
            loadDesktopApps();
        }

        initializeViews();
        initializeFonts();
        appsGrid.setCellFactory(new AppGridCellFactory());

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isBlank()) {
                appsGrid.setItems(FXCollections.observableArrayList(appItems));
                return change;
            }

            List<AppItem> searchedApps = new ArrayList<>();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    for (AppItem appItem : appItems) {
                        if (appItem.keywords.contains(text)) {
                            searchedApps.add(appItem);
                        }
                    }

                    Platform.runLater(() -> {
                        appsGrid.setItems(FXCollections.observableArrayList(searchedApps));
                    });
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.start();

            return change;
        });

        searchTextField.setTextFormatter(textFormatter);

        searchTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            searchIcon.setVisible(!newValue);
        }));

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.APPS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));
        searchIcon.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/search_icon.png").toString()));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private void loadMobileApps() {
        appsTitle.setText("Mobile Apps");

        String MOBILE_APPS_END_POINT = "exam-apps";
        String userId = viewModel.getUserId();

        String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + MOBILE_APPS_END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .addHeader("Platform", DeviceInfo.getSystemProperties().getPlatform())
                .get().build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                        appItems.addAll(baseResponse.getData().getApps());
                        Platform.runLater(() -> appsGrid.setItems(FXCollections.observableArrayList(appItems)));
                    } else {
//                        Platform.runLater(this::showEmptyAppsScreen);
                    }

                } catch (Exception e) {
                    System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
//                    Platform.runLater(this::showEmptyAppsScreen);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
//                Platform.runLater(this::showEmptyAppsScreen);
            }
        });

    }

    private void loadDesktopApps() {
        appsTitle.setText("Desktop Apps");

        String DESKTOP_APPS_END_POINT = "exam-apps/desktop";

        String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+viewModel.getUserId(), "");

        Gson gson = new Gson();

        Request request = new Request.Builder()
                .url(BASE_URL + DESKTOP_APPS_END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .get().build();

        Call call = httpClient.newCall(request);

        try (Response response = call.execute()) {
            try (ResponseBody responseBody = response.body()) {
                assert responseBody != null;
                BaseResponse baseResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                if (baseResponse.getStatus().equalsIgnoreCase("success")) {
                    appItems.addAll(baseResponse.getData().getApps());
//                    Platform.runLater(() -> displayDesktopApps(desktopApps));
                } else {
//                    Platform.runLater(this::showEmptyAppsScreen);
                }

            } catch (Exception e) {
                System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
//                Platform.runLater(this::showEmptyAppsScreen);
            }
        } catch (Exception e) {
            System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
//            Platform.runLater(this::showEmptyAppsScreen);
        }
    }

    private InitialData getInitialData() {
        InitialData data = (InitialData) ViewSwitcher.retrieveData();
        return data;
    }


    public static class InitialData {
        private String type;

        public InitialData(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }

}
