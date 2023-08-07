package com.scholarly.utme.controller.account_screens;

import com.google.gson.Gson;
import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.network.NetworkService;
import com.scholarly.utme.network.model.*;
import com.scholarly.utme.network.model.response.UploadResponse;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.account_screens.AccountProfileScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
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

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

import static com.scholarly.utme.network.NetworkService.JSON_BODY_TYPE;
import static com.scholarly.utme.util.Constants.*;

@FxmlPath("/layouts/account_screens/AccountProfileScreen.fxml")
public class AccountProfileScreenController implements FxmlView<AccountProfileScreenVM>, Initializable {
    private static final String TAG = "AccountProfileScreenController: ";

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

    private Preferences preferences = AppPreferences.getPreferences();
    private OkHttpClient httpClient = NetworkService.getHttpClient();
    MainApplication application = new MainApplication();

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        String userData = preferences.get(PREF_KEY_USER_DATA, "");
        Gson gson = new Gson();
        User user = gson.fromJson(userData, User.class);

        AtomicReference<String> gender = new AtomicReference<>("");

        initializeViews();
        initializeFonts();

        String profileImageUrl = user.getProfilePicUrl();
        if (profileImageUrl != null && !profileImageUrl.contains("empty")) {
            compressProfileImage(new Image(profileImageUrl));
            System.out.println(TAG + "Set Image successfully for url -> " + profileImageUrl);
        }

        emailTextField.setText(user.getEmail());
        profileNameTextField.setText(user.getFullName());
        phoneTextField.setText(user.getPhoneNumber());
        deviceIdLabel.setText(DeviceInfo.getSystemProperties().getDeviceId());

        changeProfileName.setOnMouseClicked(event -> {
            profileNameTextField.setEditable(true);
        });

        changePhoneNum.setOnMouseClicked(event -> {
            phoneTextField.setEditable(true);
        });

        ToggleGroup genderToggle = new ToggleGroup();
        genderToggle.getToggles().addAll(maleRadioButton, femaleRadioButton);
        genderToggle.getToggles().get(0).setUserData("m");
        genderToggle.getToggles().get(1).setUserData("f");
        if (user.getGender() != null) {
            if (user.getGender().equals("m")) {
                genderToggle.selectToggle(genderToggle.getToggles().get(0));
            } else {
                genderToggle.selectToggle(genderToggle.getToggles().get(1));
            }
        }

        genderToggle.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                gender.set((String) newValue.getToggleGroup().getSelectedToggle().getUserData());
            }
        }));

        profileImage.imageProperty().addListener(((observableValue, oldImage, newImage) -> {
            compressProfileImage(newImage);
        }));

        cameraImage.setOnMouseClicked(event -> {
            File imageFile = application.openFileChooser(ViewSwitcher.getStage());
//            File imageFile = new File("");
            System.out.println(TAG + "Got image file of size -> " + imageFile.length());

//            File compressedImage = compressImage(imageFile);

//            System.out.println(TAG + "Image file size after compression -> " + compressedImage.length());

//            application.openWebcam();

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                Task<Void> uploadTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        uploadImage(imageFile);
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

            String imageUrl = preferences.get(PREF_KEY_PROFILE_IMAGE_URL, "");
            Image image = new Image(imageUrl);
            compressProfileImage(image);
        });

        saveButton.setOnAction(event -> {
            profileNameTextField.setEditable(false);
            phoneTextField.setEditable(false);

            UpdateUserRequest request = new UpdateUserRequest(profileNameTextField.getText(), phoneTextField.getText(), gender.get());

            showProgressBar();

            // Check for internet connectivity
            try {
                URL url = new URL(BASE_URL);
                URLConnection connection = url.openConnection();
                connection.connect();

                Task<Void> updateTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        updateUser(request);
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
            ViewSwitcher.passData(new LandingScreenController.InitialData("accountScreen"));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private File rescaleAndCompressImage(File imageFile) {
        try {
            System.out.println(TAG + "File size before compression -> " + imageFile.length());
            File compressedImageFile = new File(imageFile.getName()+"compressed");

            InputStream is = new FileInputStream(imageFile);
            OutputStream os = new FileOutputStream(compressedImageFile);

            float quality = 0.5f;

            long divisor = imageFile.length()/1000;

            if (divisor > 10 && divisor < 50) {
                divisor = imageFile.length()/10000;
            }
            if (divisor > 50 && divisor < 100) {
                divisor = imageFile.length()/50000;
            }
            if (divisor > 100 && divisor < 150) {
                divisor = imageFile.length()/100000;
            }
            if (divisor > 150 && divisor < 200) {
                divisor = imageFile.length()/150000;
            }
            System.out.println(TAG + "Divisor -> " + divisor);
            if (divisor > 1) {
                quality = 1.0f/divisor;
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

    private File compressImageFile(File inputImage) {
        try {
//            System.out.println(TAG + "File size before compression -> " + inputImage.length());
            File compressedImageFile = new File(inputImage.getName()+"compressed");

            InputStream is = new FileInputStream(inputImage);
            OutputStream os = new FileOutputStream(compressedImageFile);

            float quality = 0.5f;

            long divisor = inputImage.length()/1000;

            System.out.println(TAG + "Divisor -> " + divisor);
            if (divisor > 1) {
                quality = 1.0f/divisor;
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

    private void updateUser(UpdateUserRequest updateRequest) {
        String END_POINT = "user-profile/update-profile";
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");
        System.out.println(TAG + "Update User Request with Access Token -> " + ACCESS_TOKEN);

        Gson gson = new Gson();
        String json = gson.toJson(updateRequest);

        RequestBody requestBody = RequestBody.create(JSON_BODY_TYPE, json);

        Request request = new Request.Builder()
                .url(BASE_URL + END_POINT)
                .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    BaseResponse updateResponse = gson.fromJson(responseBody.string(), BaseResponse.class);

                    if (updateResponse.getStatus().equalsIgnoreCase("success")) {
                        String userData = preferences.get(PREF_KEY_USER_DATA, "");
                        User user = gson.fromJson(userData, User.class);
                        user.setFullName(updateResponse.getData().getFullName());
                        user.setPhoneNumber(updateResponse.getData().getPhoneNumber());
                        user.setGender(updateResponse.getData().getGender());

                        String updatedUser = gson.toJson(user);
                        preferences.put(PREF_KEY_USER_DATA, updatedUser);

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

                } catch (Exception e) {
                    System.out.println("Cannot parse response body to data class because -> " + e.getMessage());
                }
                response.close();
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

    private void uploadImage(File imageFile) {
        String END_POINT = "user-profile/upload-profile-pic";
        String ACCESS_TOKEN = preferences.get(PREF_KEY_ACCESS_TOKEN, "");
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
                .post(requestBody)
                .build();

        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    assert responseBody != null;
                    Gson gson = new Gson();
                    UploadResponse uploadResponse = gson.fromJson(responseBody.string(), UploadResponse.class);
                    if (uploadResponse.getStatus().equalsIgnoreCase("success")) {
                        String imageUrl = uploadResponse.getData();
                        preferences.put(PREF_KEY_PROFILE_IMAGE_URL, imageUrl);
                        System.out.println(TAG + "Uploaded image successfully with url -> " + imageUrl);
//                        compressProfileImage(new Image(imageUrl));

                        String userData = preferences.get(PREF_KEY_USER_DATA, "");
                        User user = gson.fromJson(userData, User.class);
                        user.setProfilePicUrl(imageUrl);
                        compressProfileImage(new Image(user.getProfilePicUrl()));

                        String updatedUser = gson.toJson(user);
                        preferences.put(PREF_KEY_USER_DATA, updatedUser);

                        Platform.runLater(() -> {
                            hideProgressBar();
                            compressProfileImage(new Image(user.getProfilePicUrl()));
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

    private void compressProfileImage(Image image) {
        Rectangle2D imageBounds = new Rectangle2D(0, 0, image.getWidth(), image.getHeight());
        profileImage.setFitWidth(120);
        profileImage.setFitHeight(120);
        profileImage.setImage(image);
        profileImage.setViewport(imageBounds);
        profileImage.setSmooth(true);
        Circle clip = new Circle(60, 60, 60);
        profileImage.setClip(clip);
    }
}
