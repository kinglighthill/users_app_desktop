package com.scholarly.utme.controller.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.DeviceInfo;
import com.scholarly.utme.network.model.User;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAccountVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;

@FxmlPath("/layouts/landing_screens/landing_screen_account.fxml")
public class LandingScreenAccountController implements FxmlView<LandingScreenAccountVM>, Initializable {
    private static final String TAG = "LandingScreenAccountController: ";

    @InjectViewModel
    private LandingScreenAccountVM viewModel;

    @FXML
    private Pane dialogDimmer;
    @FXML
    private Panel profilePanel, referralPanel, bookmarksPanel, triviaPanel, notificationsPanel;
    @FXML
    private ImageView profileImage, triviaImage, referralsImage, activityImage, walletImage, bookmarkImage, notificationImage, rewardsImage, downloadsImage;
    @FXML
    private ImageView accountExpandIcon, triviaExpandIcon, referralsExpandIcon, activityExpandIcon, walletExpandIcon, bookmarkExpandIcon, notificationExpandIcon, rewardsExpandIcon, downloadsExpandIcon;
    @FXML
    private Button logoutButton;
    @FXML
    private Label emailText, deviceIdLabel;


    Preferences preferences = AppPreferences.getPreferences();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String userData = preferences.get(PREF_KEY_USER_DATA, "");
        Gson gson = new Gson();
        User user = gson.fromJson(userData, User.class);

        initializeViews();
        initializeFonts();

        emailText.setText(user.getEmail());
        deviceIdLabel.setText(DeviceInfo.getSystemProperties().getDeviceId());

        if (user.getProfilePicUrl() != null){
            compressProfileImage((new Image(user.getProfilePicUrl())));
            System.out.println(TAG + "Set Image successfully for url -> " + user.getProfilePicUrl());
        }

        profilePanel.setOnMouseClicked(mouseEvent -> {
            ViewSwitcher.showScreen(View.ACCOUNT_PROFILE_SCREEN);
//            compressProfileImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image2.jpg").toString()));
        });

//        referralPanel.setOnMouseClicked(mouseEvent -> {
//            ViewSwitcher.showScreen(View.ACCOUNT_REFERRAL_SCREEN);
//        });

        bookmarksPanel.setOnMouseClicked(mouseEvent -> {
            ViewSwitcher.showScreen(View.ACCOUNT_BOOKMARKS_SCREEN);
        });

        /*triviaPanel.setOnMouseClicked(mouseEvent -> {
            ViewSwitcher.showScreen(View.ACCOUNT_TRIVIA_SCREEN);
        });*/

        notificationsPanel.setOnMouseClicked(mouseEvent -> {
            ViewSwitcher.showScreen(View.ACCOUNT_NOTIFICATIONS_SCREEN);
        });

        logoutButton.setOnAction(event -> {
            Dialog<ButtonType> dialog = Alerts.dialog(getClass(), "Logout", null, "Are you sure you want to logout?");

            dialogDimmer.setVisible(true);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    preferences.putBoolean(Constants.PREF_KEY_ACTIVATION_STATE, false);
                    preferences.putBoolean(Constants.PREF_KEY_HOME_SCREEN_ACTIVATE_PROMPT_REMOVED, false);
                    dialogDimmer.setVisible(true);
                    ViewSwitcher.passData(false);
                    ViewSwitcher.showScreen(View.AUTHENTICATION_SCREEN);
                } else {
                    dialogDimmer.setVisible(false);
                }
                return buttonType;
            });

            dialog.show();

        });

    }

    private void initializeViews() {
        profileImage.setImage(null);
//        compressProfileImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image2.jpg").toString()));
//        triviaImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/trivia_icon.png").toString()));
//        referralsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/referrals_icon.png").toString()));
//        activityImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/activity_icon.png").toString()));
//        walletImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/wallet_icon.png").toString()));
        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/bookmark_icon.png").toString()));
        notificationImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/notification_icon.png").toString()));
//        rewardsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/rewards_icon.png").toString()));
//        downloadsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/download_icon.png").toString()));


        accountExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        triviaExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        referralsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        activityExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        accountExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        walletExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        bookmarkExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        notificationExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        rewardsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
//        downloadsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));

        ImageView logoutIcon = new ImageView(new Image(getClass().getResource("/drawable/account_screen_images/logout_icon.png").toString()));
        logoutButton.setGraphic(logoutIcon);
        logoutButton.setGraphicTextGap(40);
    }

    private void compressProfileImage(Image image) {
        Circle clip = new Circle(40, 40, 40);
        profileImage.setClip(clip);
        Rectangle2D imageBounds = new Rectangle2D(0, 0, image.getWidth(), image.getHeight());
        profileImage.setFitWidth(80);
        profileImage.setFitHeight(80);
        profileImage.setViewport(imageBounds);
        profileImage.setSmooth(true);
        profileImage.setImage(image);
    }

    private void initializeFonts() {

    }
}
