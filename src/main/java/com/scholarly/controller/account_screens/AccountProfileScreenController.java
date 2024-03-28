package com.scholarly.controller.account_screens;

import com.google.gson.Gson;
import com.scholarly.MainApplication;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.network.NetworkService;
import com.scholarly.network.model.*;
import com.scholarly.network.model.request.UpdateUserRequest;
import com.scholarly.network.model.response.BaseResponse;
import com.scholarly.network.model.response.UploadResponse;
import com.scholarly.ui.utils.*;
import com.scholarly.util.Helper;
import com.scholarly.util.PreferencesManager;
import com.scholarly.viewmodels.account_screens.AccountProfileScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static com.scholarly.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.util.Constants.*;

@FxmlPath("/layouts/account_screens/AccountProfileScreen.fxml")
public class AccountProfileScreenController implements FxmlView<AccountProfileScreenVM>, Initializable {
    private static final String TAG = "AccountProfileScreenController: ";

    @InjectViewModel
    private AccountProfileScreenVM viewModel;

    @FXML
    private Pane dialogDimmer;
    @FXML
    private ProgressIndicator progressBar;
    @FXML
    private HBox toastBar;
    @FXML
    private ImageView profileImage, cameraImage, keyImage, infoImage;
    @FXML
    private Button backButton, saveButton;
    @FXML
    private RadioButton femaleRadioButton, maleRadioButton;
    @FXML
    private TextField profileNameTextField, phoneTextField, emailTextField;
    @FXML
    private Label changeProfileName, changePhoneNum, deviceIdLabel, toastLabel, changeMailHereLabel;


    private final OkHttpClient httpClient = NetworkService.getHttpClient();
    MainApplication application = new MainApplication();

    interface NetworkCallback {
        void refreshToken();

        void resendRequest();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {

        boolean internetEnabled = Helper.checkNetworkConnectivity();

        initializeViews();
        initializeFonts();

        String imageUrl = viewModel.getUser().getProfilePicUrl();
        String imageUrlWithQueryString = imageUrl + "?" + RandomStringUtils.random(6, true, true);

        Task<Void> imageTask = new Task<>() {
            @Override
            protected Void call() {
                if (imageUrl != null && !imageUrl.contains("empty")) {
                    Image image = new Image(imageUrlWithQueryString, false);
                    if (image.isError() || !internetEnabled) {
                        try {
                            InputStream inputStream = new FileInputStream("scholarly_profile_image.jpg");
                            renderProfileImage(new Image(inputStream));
                            System.out.println(TAG + "Loaded Image from File");
                        } catch (Exception e) {
                            System.out.println(TAG + "Error loading image from File system");
                        }
                    } else {
                        renderProfileImage(image);
                        System.out.println(TAG + "Loaded Image from url -> " + imageUrlWithQueryString);
                    }
                } else {
                    renderProfileImage(new Image(getClass().getResource("/drawable/account_screen_images/default_profile_image.png").toString()));
                }
                return null;
            }
        };
        Thread imageThread = new Thread(imageTask);
        imageThread.start();

        emailTextField.setText(viewModel.getUser().getEmail());
        profileNameTextField.setText(viewModel.getUser().getFullName());
        phoneTextField.setText(viewModel.getUser().getPhoneNumber());

//        String encodedDeviceId = Base62.encodeUUID(UUID.fromString(DeviceInfo.getSystemProperties().getDeviceId()));
        String deviceId = DeviceInfo.getSystemProperties().getDeviceId();
        deviceIdLabel.setText(deviceId.toUpperCase());

        changeMailHereLabel.setOnMouseClicked(event -> {
            application.openBrowser(GMAIL_URL);
        });
        changeMailHereLabel.setOnMouseEntered(event -> changeMailHereLabel.setUnderline(true));
        changeMailHereLabel.setOnMouseExited(event -> changeMailHereLabel.setUnderline(false));

        ToggleGroup genderToggle = new ToggleGroup();
        genderToggle.getToggles().addAll(maleRadioButton, femaleRadioButton);
        genderToggle.getToggles().get(0).setUserData("m");
        genderToggle.getToggles().get(1).setUserData("f");
        if (viewModel.getUser().getGender() != null) {
            if (viewModel.getUser().getGender().equals("m")) {
                genderToggle.selectToggle(genderToggle.getToggles().get(0));
            } else if (viewModel.getUser().getGender().equals("f")){
                genderToggle.selectToggle(genderToggle.getToggles().get(1));
            }
        }

        AtomicReference<String> gender = new AtomicReference<>("");

        if (genderToggle.getSelectedToggle() != null) {
            gender.set((String) genderToggle.getSelectedToggle().getUserData());
        }

        genderToggle.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                gender.set((String) newValue.getToggleGroup().getSelectedToggle().getUserData());
            }
        }));

        cameraImage.setOnMouseClicked(event -> {
            File imageFile = application.openFileChooser(ViewSwitcher.getStage());
            System.out.println(TAG + "Got image file of size -> " + imageFile.length());
            String END_POINT = "user-profile/upload-profile-pic";

            Gson gson = new Gson();

            String userId = viewModel.getUserId();

            String REFRESH_TOKEN = PreferencesManager.get(PREF_KEY_REFRESH_TOKEN+userId, "");

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                Task<Void> uploadTask = new Task<>() {
                    @Override
                    protected Void call() {
                        uploadImage(imageFile, new NetworkCallback() {
                            @Override
                            public void refreshToken() {
                                System.out.println(TAG + "Refreshing token...");
                                RefreshRequest refreshRequest = new RefreshRequest(REFRESH_TOKEN);

                                String json = gson.toJson(refreshRequest);

                                RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                                Request request = new Request.Builder()
                                        .url(REFRESH_URL)
                                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                                        .post(requestBody)
                                        .build();

                                Call call = httpClient.newCall(request);

                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {

                                        try(ResponseBody responseBody = response.body()) {
                                            assert responseBody != null;
                                            BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                            if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                                                System.out.println(TAG + "Refreshed Token Response user id -> " + refreshResponse.getData().getUserId());

                                                PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                                PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                            } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
                                                    alertDialog.show();
                                                    hideProgressBar();
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

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                                            alertDialog.show();
                                            hideProgressBar();
                                        });
                                        System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void resendRequest() {
                                System.out.println(TAG + "Resending request...");

                                String NEW_ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                                RequestBody requestBody = new MultipartBody.Builder()
                                        .setType(MultipartBody.FORM)
                                        .addFormDataPart("image", imageFile.getName(),
                                                RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                                        .build();

                                // Create the request
                                Request request = new Request.Builder()
                                        .url(BASE_URL + END_POINT)
                                        .header("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                                        .post(requestBody)
                                        .build();

                                Call call = httpClient.newCall(request);

                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        try (ResponseBody responseBody = response.body()) {
                                            assert responseBody != null;
                                            Gson gson = new Gson();
                                            UploadResponse uploadResponse = gson.fromJson(responseBody.string(), UploadResponse.class);
                                            if (uploadResponse.getStatus().equalsIgnoreCase("success")) {
                                                String imageUrl = uploadResponse.getData();
                                                System.out.println(TAG + "Uploaded image successfully with url -> " + imageUrl);

                                                String userData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                                                UserData user = gson.fromJson(userData, UserData.class);
                                                user.setProfilePicUrl(imageUrl);

                                                String updatedUser = gson.toJson(user);
                                                PreferencesManager.put(PREF_KEY_USER_DATA+userId, updatedUser);

                                                Platform.runLater(() -> {
                                                    hideProgressBar();
                                                    renderProfileImage(new Image(user.getProfilePicUrl() + "?" + RandomStringUtils.random(6, true, true)));
                                                    toastLabel.setText("Image uploaded successfully!");
                                                    Animations.showToast(toastBar);
                                                });

                                            } else if (uploadResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", uploadResponse.getMessage(), "");
                                                    alertDialog.show();
                                                    hideProgressBar();
                                                });
                                            }

                                        } catch (Exception e) {
                                            Platform.runLater(() -> {
                                                hideProgressBar();
                                                Alert alertDialog = Alerts.info(getClass(), "Error", "Something went wrong. Try again later!", "");
                                                alertDialog.show();
                                            });
                                            System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", "Image upload failed " + e.getMessage(), "");
                                            alertDialog.show();
                                            hideProgressBar();
                                        });
                                        System.out.println("Request failed with exception -> " + e.getMessage());
                                    }
                                });
                            }
                        });
                        return null;
                    }
                };
                Thread uploadThread = new Thread(uploadTask);
                uploadThread.start();

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                hideProgressBar();
                System.out.println(TAG + "Cannot create connection because -> " + e.getMessage());
            }
        });

        saveButton.setOnAction(event -> {
            profileNameTextField.setEditable(false);
            phoneTextField.setEditable(false);

            showProgressBar();

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                String UPDATE_END_POINT = "user-profile/update-profile";

                String userId = viewModel.getUserId();

                String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");
                String REFRESH_TOKEN = PreferencesManager.get(PREF_KEY_REFRESH_TOKEN+userId, "");

                UpdateUserRequest updateUserRequest = new UpdateUserRequest(profileNameTextField.getText(), phoneTextField.getText(), gender.get());

                Gson gson = new Gson();

                String json = gson.toJson(updateUserRequest);

                RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                Request updateRequest = new Request.Builder()
                        .url(BASE_URL + UPDATE_END_POINT)
                        .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                        .post(requestBody)
                        .build();

                Task<Void> updateTask = new Task<>() {
                    @Override
                    protected Void call() {
                        updateUser(updateRequest, new NetworkCallback() {
                            @Override
                            public void refreshToken() {
                                System.out.println(TAG + "Refreshing token...");
                                RefreshRequest refreshRequest = new RefreshRequest(REFRESH_TOKEN);

                                String json = gson.toJson(refreshRequest);

                                RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

                                Request request = new Request.Builder()
                                        .url(REFRESH_URL)
                                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                                        .post(requestBody)
                                        .build();

                                Call call = httpClient.newCall(request);

                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {
                                        System.out.println(TAG + "Got OkHttp refreshToken response -> " + response);
                                        try(ResponseBody responseBody = response.body()) {
                                            assert responseBody != null;
                                            BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                            if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                                                System.out.println(TAG + "Refreshed Token with user id -> " + refreshResponse.getData().getUserId());
                                                System.out.println(TAG + "Put Refresh Token User Id -> " + userId);

                                                PreferencesManager.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                                PreferencesManager.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                            } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
                                                    alertDialog.show();
                                                    hideProgressBar();
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

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                                            alertDialog.show();
                                            hideProgressBar();
                                        });
                                        System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
                                    }
                                });

                            }

                            @Override
                            public void resendRequest() {
                                System.out.println(TAG + "Resending request...");

                                String NEW_ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");

                                Request updateRequest = new Request.Builder()
                                        .url(BASE_URL + UPDATE_END_POINT)
                                        .addHeader("Authorization", "Bearer " + NEW_ACCESS_TOKEN)
                                        .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                                        .post(requestBody)
                                        .build();

                                Call call = httpClient.newCall(updateRequest);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) {

                                        try(ResponseBody responseBody = response.body()) {
                                            assert responseBody != null;
                                            BaseResponse updateResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                            if (updateResponse.getStatus().equalsIgnoreCase("success")) {

                                                String oldUserData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                                                UserData oldUser = gson.fromJson(oldUserData, UserData.class);

                                                UserData newUser = new UserData(oldUser.getId(), updateResponse.getData().getFullName(), oldUser.getEmail(), updateResponse.getData().getPhoneNumber(),
                                                        oldUser.getCountry(), oldUser.isEmailVerified(), oldUser.getProfilePicUrl(), oldUser.getReferralCode(), updateResponse.getData().getGender());

                                                String updatedUserData = gson.toJson(newUser);
                                                PreferencesManager.put(PREF_KEY_USER_DATA+userId, updatedUserData);

                                                Platform.runLater(() -> {
                                                    hideProgressBar();
                                                    toastLabel.setText("Account updated successfully!");
                                                    Animations.showToast(toastBar);
                                                });

                                            } else if (updateResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", updateResponse.getMessage(), "");
                                                    alertDialog.show();
                                                    hideProgressBar();
                                                });
                                            }
                                            response.close();
                                        } catch (Exception e) {
                                            Platform.runLater(() -> {
                                                hideProgressBar();
                                                Alert alertDialog = Alerts.info(getClass(), "Error", "Something went wrong. Try again later!", "");
                                                alertDialog.show();
                                            });
                                            System.out.println(TAG + "Cannot parse response body to data class because -> " + e.getMessage());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Platform.runLater(() -> {
                                            Alert alertDialog = Alerts.info(getClass(), "Error", e.getMessage(), "");
                                            alertDialog.show();
                                            hideProgressBar();
                                        });
                                        System.out.println(TAG + "Request failed with exception -> " + e.getMessage());
                                    }
                                });
                            }
                        });
                        return null;
                    }
                };
                Thread updateThread = new Thread(updateTask);
                updateThread.start();

            } catch (Exception e) {
                Alert alertDialog = Alerts.info(getClass(), "No Internet", "Check your internet connection and try again", "");
                alertDialog.show();
                hideProgressBar();
                System.out.println(TAG + "Cannot create connection to -> " + e.getMessage());
                MainApplication.log(e);
            }

        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACCOUNT_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private File compressImage(File imageFile) {
        if (imageFile != null) {
            File newFile = compressImageFile(imageFile);
            do {
                newFile = compressImageFile(newFile);
            } while (Objects.requireNonNull(newFile).length() > 4000);
            return newFile;
        }
        return new File("");
    }

    private void saveImageToFile2(File image) {
        try {
            BufferedImage bufferedImage = ImageIO.read(image);
            File outputFile = new File("test_profile_image.jpg");
            ImageIO.write(bufferedImage, "jpg", outputFile);
        } catch (Exception e) {
            System.out.println(TAG + "Error saving image to File -> " + e.getMessage());
        }
    }

    private void saveImageToFile(File image) {
        File localImage = new File("scholarly_profile_image.jpg");

        try {
            InputStream inputStream = new FileInputStream(image);
            OutputStream outputStream = new FileOutputStream(localImage);

            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage bufferedImage = ImageIO.read(inputStream);

            // get all image writers for JPG format
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            ImageWriter writer = writers.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            // compress to a given quality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);

            // appends a complete image stream containing a single image and
            //associated stream and image metadata and thumbnails to the output
            writer.write(null, new IIOImage(bufferedImage, null, null), param);

            System.out.println(TAG + "Saved image successfully with name -> " + localImage.getName() + " and size -> " + localImage.length());
            System.out.println(TAG + "Saved image successfully with image path -> " + localImage.getPath());

            // close all streams
            inputStream.close();
            outputStream.close();
            ios.close();
            writer.dispose();

        } catch (Exception e) {
            System.out.println(TAG + "Error saving image -> " + e.getMessage());
        }
    }

    private File compressImageFile(File inputImage) {
        try {
//            System.out.println(TAG + "File size before compression -> " + inputImage.length());
            File compressedImageFile = new File(inputImage.getName() + "compressed");

            InputStream is = new FileInputStream(inputImage);
            OutputStream os = new FileOutputStream(compressedImageFile);

            float quality = 0.5f;

            long divisor = inputImage.length() / 1000;

            System.out.println(TAG + "Divisor -> " + divisor);
            if (divisor > 1) {
                quality = 1.0f / divisor;
            }
            System.out.println(TAG + "New Quality size -> " + quality);

            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage image = ImageIO.read(is);

            // get all image writers for JPG format
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            ImageWriter writer = writers.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            // compress to a given quality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // appends a complete image stream containing a single image and
            //associated stream and image metadata and thumbnails to the output
            writer.write(null, new IIOImage(image, null, null), param);

            // close all streams
            is.close();
            os.close();
            ios.close();
            writer.dispose();

            System.out.println(TAG + "Compressed image to size -> " + compressedImageFile.length());
            return compressedImageFile;

        } catch (Exception e) {
            System.out.println(TAG + "Error compressing image -> " + e.getMessage());
        }
        return null;
    }

    private void initializeViews() {
        profileImage.setImage(null);
//        compressProfileImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image2.png").toString()));
        cameraImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/camera_icon.png").toString()));
        keyImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/key_icon.png").toString()));
        infoImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/info_icon.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }

    private void showProgressBar() {
        Animations.showDialog(progressBar, dialogDimmer);
    }

    private void hideProgressBar() {
        Animations.hideDialog(progressBar, dialogDimmer);
    }

    private void updateUser(Request updateRequest, NetworkCallback callback) {
        Gson gson = new Gson();

        Call call = httpClient.newCall(updateRequest);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(TAG + "UpdateUser: Got response with code -> " + response.code());
                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    callback.refreshToken();
                    callback.resendRequest();

                } else {

                    try(ResponseBody responseBody = response.body()) {
                        assert responseBody != null;
                        BaseResponse updateResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                        if (updateResponse.getStatus().equalsIgnoreCase("success")) {
                            String userId = viewModel.getUserId();

                            String oldUserData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                            UserData oldUser = gson.fromJson(oldUserData, UserData.class);

                            System.out.println(TAG + "OldUserData -> " + oldUserData);

                            UserData newUser = new UserData(oldUser.getId(), updateResponse.getData().getFullName(), oldUser.getEmail(), updateResponse.getData().getPhoneNumber(),
                                    oldUser.getCountry(), oldUser.isEmailVerified(), oldUser.getProfilePicUrl(), oldUser.getReferralCode(), updateResponse.getData().getGender());

                            String updatedUserData = gson.toJson(newUser);
                            PreferencesManager.put(PREF_KEY_USER_DATA+userId, updatedUserData);

                            System.out.println(TAG + "New UserData -> " + updatedUserData);

                            Platform.runLater(() -> {
                                hideProgressBar();
                                toastLabel.setText("Account updated successfully!");
                                Animations.showToast(toastBar);
                            });

                        } else if (updateResponse.getStatus().equalsIgnoreCase("error")) {
                            Platform.runLater(() -> {
                                Alert alertDialog = Alerts.info(getClass(), "Error", updateResponse.getMessage(), "");
                                alertDialog.show();
                                hideProgressBar();
                            });
                        }
                        response.close();
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
                    Alert alertDialog = Alerts.info(getClass(), "Error", "Could not connect because " + e.getMessage(), "");
                    alertDialog.show();
                    hideProgressBar();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void uploadImage(File imageFile, NetworkCallback callback) {
        String END_POINT = "user-profile/upload-profile-pic";

        String userId = viewModel.getUserId();

        String ACCESS_TOKEN = PreferencesManager.get(PREF_KEY_ACCESS_TOKEN+userId, "");
//        System.out.println(TAG + "Upload Profile Image Request with Access Token -> " + ACCESS_TOKEN);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", imageFile.getName(),
                        RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .addHeader("platform", DeviceInfo.getSystemProperties().getPlatform())
                .post(requestBody)
                .build();

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
                        Gson gson = new Gson();
                        UploadResponse uploadResponse = gson.fromJson(responseBody.string(), UploadResponse.class);
                        if (uploadResponse.getStatus().equalsIgnoreCase("success")) {
                            String imageUrl = uploadResponse.getData();
                            System.out.println(TAG + "Uploaded image successfully with url -> " + imageUrl);

                            String userData = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                            UserData user = gson.fromJson(userData, UserData.class);
                            user.setProfilePicUrl(imageUrl);

                            String updatedUser = gson.toJson(user);
                            PreferencesManager.put(PREF_KEY_USER_DATA+userId, updatedUser);

                            Platform.runLater(() -> {
                                hideProgressBar();
                                renderProfileImage(new Image(user.getProfilePicUrl() + "?" + RandomStringUtils.random(6, true, true)));
                                toastLabel.setText("Image uploaded successfully!");
                                Animations.showToast(toastBar);
                                saveImageToFile(imageFile);
                            });

                        } else if (uploadResponse.getStatus().equalsIgnoreCase("error")) {
                            Platform.runLater(() -> {
                                Alert alertDialog = Alerts.info(getClass(), "Error", uploadResponse.getMessage(), "");
                                alertDialog.show();
                                hideProgressBar();
                            });
                        }

                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            hideProgressBar();
                            Alert alertDialog = Alerts.info(getClass(), "Error", "Something went wrong. Try again later!", "");
                            alertDialog.show();
                        });
                        System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                    }
                }
                response.close();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Platform.runLater(() -> {
                    Alert alertDialog = Alerts.info(getClass(), "Error", "Image upload failed " + e.getMessage(), "");
                    alertDialog.show();
                    hideProgressBar();
                });
                System.out.println("Request failed with exception -> " + e.getMessage());
            }
        });

    }

    private void renderProfileImage(Image image) {
        Circle clip = new Circle(60, 60, 60);
        profileImage.setClip(clip);
        Rectangle2D imageBounds = new Rectangle2D(0, 0, image.getWidth(), image.getHeight());
        profileImage.setFitWidth(120);
        profileImage.setFitHeight(120);
        profileImage.setViewport(imageBounds);
        profileImage.setSmooth(true);
        profileImage.setCache(true);
        profileImage.setImage(image);
    }
}
