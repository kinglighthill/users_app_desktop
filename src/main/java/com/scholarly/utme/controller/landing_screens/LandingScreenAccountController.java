package com.scholarly.utme.controller.landing_screens;

import com.scholarly.utme.data.model.newDb.contentType.image.ImageBody;
import com.scholarly.utme.viewmodels.landing_screens.LandingScreenAccountVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/landing_screens/landing_screen_account.fxml")
public class LandingScreenAccountController implements FxmlView<LandingScreenAccountVM>, Initializable {

    @InjectViewModel
    private LandingScreenAccountVM viewModel;

    @FXML
    private Panel profile;

    @FXML
    private ImageView profileImage, triviaImage, referralsImage, activityImage, walletImage, bookmarkImage, notificationImage, rewardsImage, downloadsImage;

    @FXML
    private ImageView accountExpandIcon, triviaExpandIcon, referralsExpandIcon, activityExpandIcon, walletExpandIcon, bookmarkExpandIcon, notificationExpandIcon, rewardsExpandIcon, downloadsExpandIcon;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        profile.setOnMouseClicked(event -> {
            System.out.println("Profile clicked");
        });

    }

    private void initializeViews() {
        Circle clip = new Circle(40, 35, 35);
        profileImage.setClip(clip);
        profileImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image.png").toString()));
        triviaImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/trivia_icon.png").toString()));
        referralsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/referrals_icon.png").toString()));
        activityImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/activity_icon.png").toString()));
        walletImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/wallet_icon.png").toString()));
        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/bookmark_icon.png").toString()));
        notificationImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/notification_icon.png").toString()));
        rewardsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/rewards_icon.png").toString()));
        downloadsImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/download_icon.png").toString()));



        accountExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        triviaExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        referralsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        activityExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        accountExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        walletExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        bookmarkExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        notificationExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        rewardsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));
        downloadsExpandIcon.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/expand_icon.png").toString()));

    }

    private void initializeFonts() {

    }
}
