package com.scholarly.utme.controller.account_screens;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.*;
import com.scholarly.utme.network.model.request.UpdateUserRequest;
import com.scholarly.utme.network.model.response.BaseResponse;
import com.scholarly.utme.network.model.response.UploadResponse;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.account_screens.AccountProfileScreenVM;
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
import org.unbrokendome.base62.Base62;

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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

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
    private Label changeProfileName, changePhoneNum, deviceIdLabel, toastLabel;

    private Preferences preferences;
    private OkHttpClient httpClient;
    MainApplication application = new MainApplication();

    interface NetworkCallback {
        void refreshToken();

        void resendRequest();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        preferences = AppPreferences.getPreferences();
        httpClient = NetworkService.getHttpClient();

        boolean internetEnabled = checkNetworkConnectivity();

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

        String encodedDeviceId = Base62.encodeUUID(UUID.fromString(DeviceInfo.getSystemProperties().getDeviceId()));
        deviceIdLabel.setText(encodedDeviceId.toUpperCase());

        changeProfileName.setOnMouseClicked(event -> {
            profileNameTextField.setEditable(true);
        });
        changeProfileName.setOnMouseEntered(event -> changeProfileName.setUnderline(true));
        changeProfileName.setOnMouseExited(event -> changeProfileName.setUnderline(false));

        changePhoneNum.setOnMouseClicked(event -> {
            phoneTextField.setEditable(true);
        });
        changePhoneNum.setOnMouseEntered(event -> changePhoneNum.setUnderline(true));
        changePhoneNum.setOnMouseExited(event -> changePhoneNum.setUnderline(false));

        ToggleGroup genderToggle = new ToggleGroup();
        genderToggle.getToggles().addAll(maleRadioButton, femaleRadioButton);
        genderToggle.getToggles().get(0).setUserData("m");
        genderToggle.getToggles().get(1).setUserData("f");
        if (viewModel.getUser().getGender() != null) {
            if (viewModel.getUser().getGender().equals("m")) {
                genderToggle.selectToggle(genderToggle.getToggles().get(0));
            } else {
                genderToggle.selectToggle(genderToggle.getToggles().get(1));
            }
        }

        AtomicReference<String> gender = new AtomicReference<>("");

        gender.set((String) genderToggle.getSelectedToggle().getUserData());

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

            String REFRESH_TOKEN = preferences.get(PREF_KEY_REFRESH_TOKEN+userId, "");

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
                                        System.out.println(TAG + "Got OkHttp refreshToken response -> " + response);
                                        try(ResponseBody responseBody = response.body()) {
                                            assert responseBody != null;
                                            BaseResponse refreshResponse = gson.fromJson(responseBody.string(), BaseResponse.class);
                                            if (refreshResponse.getStatus().equalsIgnoreCase("success")) {
                                                System.out.println(TAG + "Refreshed Token Response user id -> " + refreshResponse.getData().getUserId());

                                                preferences.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                                preferences.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                            } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
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

                                String NEW_ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");

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

                                                String userData = preferences.get(PREF_KEY_USER_DATA+userId, "");
                                                UserData user = gson.fromJson(userData, UserData.class);
                                                user.setProfilePicUrl(imageUrl);

                                                String updatedUser = gson.toJson(user);
                                                preferences.put(PREF_KEY_USER_DATA+userId, updatedUser);

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

            String UPDATE_END_POINT = "user-profile/update-profile";

            String userId = viewModel.getUserId();

            String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");
            String REFRESH_TOKEN = preferences.get(PREF_KEY_REFRESH_TOKEN+userId, "");

            UpdateUserRequest updateUserRequest = new UpdateUserRequest(profileNameTextField.getText(), phoneTextField.getText(), gender.get());

            showProgressBar();

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

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

                                                preferences.put(PREF_KEY_ACCESS_TOKEN+userId, refreshResponse.getData().getAccessToken());
                                                preferences.put(PREF_KEY_REFRESH_TOKEN+userId, refreshResponse.getData().getRefreshToken());

                                            } else if (refreshResponse.getStatus().equalsIgnoreCase("error")) {
                                                Platform.runLater(() -> {
                                                    Alert alertDialog = Alerts.info(getClass(), "Error", refreshResponse.getMessage(), "");
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

                                String NEW_ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");

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

                                                String oldUserData = preferences.get(PREF_KEY_USER_DATA+userId, "");
                                                UserData oldUser = gson.fromJson(oldUserData, UserData.class);

                                                UserData newUser = new UserData(oldUser.getId(), updateResponse.getData().getFullName(), oldUser.getEmail(), updateResponse.getData().getPhoneNumber(),
                                                        oldUser.getCountry(), oldUser.isEmailVerified(), oldUser.getProfilePicUrl(), oldUser.getReferralCode(), updateResponse.getData().getGender());

                                                String updatedUserData = gson.toJson(newUser);
                                                preferences.put(PREF_KEY_USER_DATA+userId, updatedUserData);

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
            }

        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACCOUNT_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private File rescaleAndCompressImage(File imageFile) {
        try {
            System.out.println(TAG + "File size before compression -> " + imageFile.length());
            File compressedImageFile = new File(imageFile.getName() + "compressed");

            InputStream is = new FileInputStream(imageFile);
            OutputStream os = new FileOutputStream(compressedImageFile);

            float quality = 0.5f;

            long divisor = imageFile.length() / 1000;

            if (divisor > 10 && divisor < 50) {
                divisor = imageFile.length() / 10000;
            }
            if (divisor > 50 && divisor < 100) {
                divisor = imageFile.length() / 50000;
            }
            if (divisor > 100 && divisor < 150) {
                divisor = imageFile.length() / 100000;
            }
            if (divisor > 150 && divisor < 200) {
                divisor = imageFile.length() / 150000;
            }
            System.out.println(TAG + "Divisor -> " + divisor);
            if (divisor > 1) {
                quality = 1.0f / divisor;
            }

            System.out.println(TAG + "New Quality size -> " + quality);


            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage image = ImageIO.read(is);

            // get all image writers for JPG format
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            ImageWriter writer = (ImageWriter) writers.next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            // compress to a given quality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.05f);

            // appends a complete image stream containing a single image and
            //associated stream and image metadata and thumbnails to the output
            writer.write(null, new IIOImage(image, null, null), param);

            // close all streams
            is.close();
            os.close();
            ios.close();
            writer.dispose();

            System.out.println(TAG + "File size after compression -> " + compressedImageFile.length());

        } catch (Exception e) {
            System.out.println(TAG + "Error compressing image " + e.getMessage());
        }
        return null;
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

                            String oldUserData = preferences.get(PREF_KEY_USER_DATA+userId, "");
                            UserData oldUser = gson.fromJson(oldUserData, UserData.class);

                            System.out.println(TAG + "OldUserData -> " + oldUserData);

                            UserData newUser = new UserData(oldUser.getId(), updateResponse.getData().getFullName(), oldUser.getEmail(), updateResponse.getData().getPhoneNumber(),
                                    oldUser.getCountry(), oldUser.isEmailVerified(), oldUser.getProfilePicUrl(), oldUser.getReferralCode(), updateResponse.getData().getGender());

                            String updatedUserData = gson.toJson(newUser);
                            preferences.put(PREF_KEY_USER_DATA+userId, updatedUserData);

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

        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN+userId, "");
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

                            String userData = preferences.get(PREF_KEY_USER_DATA+userId, "");
                            UserData user = gson.fromJson(userData, UserData.class);
                            user.setProfilePicUrl(imageUrl);

                            String updatedUser = gson.toJson(user);
                            preferences.put(PREF_KEY_USER_DATA+userId, updatedUser);

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

    private BaseResponse refreshAccessToken() {
        System.out.println(TAG + "Inside Refresh Access Token!");
        String END_POINT = "/login/refresh";

        System.out.println(TAG + "Got Refresh Token -> " + preferences.get(PREF_KEY_REFRESH_TOKEN, ""));
        String refreshToken = preferences.get(PREF_KEY_REFRESH_TOKEN, "");
        RefreshRequest refreshRequest = new RefreshRequest(refreshToken);

        Gson gson = new Gson();
        String json = gson.toJson(refreshRequest);
        System.out.println(TAG + "JSON Request Body -> " + json);

        BaseResponse responseObject = null;

        JsonNode body = new JsonNode(json);
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + END_POINT)
                    .body(body)
                    .asString();
            if (response.getCode() == 200) {
                System.out.println(TAG + "Got Access Token successfully!");

                responseObject = gson.fromJson(response.getBody(), BaseResponse.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(TAG + "Cannot execute Unirest because " + e.getMessage());
        }

        return responseObject;
    }

}
