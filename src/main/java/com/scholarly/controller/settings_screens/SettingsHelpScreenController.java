package com.scholarly.controller.settings_screens;

import com.scholarly.MainApplication;
import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.models.FAQ;
import com.scholarly.models.FaqItem;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import com.scholarly.util.AppProperties;
import com.scholarly.util.Constants;
import com.scholarly.viewmodels.settings_screens.SettingsHelpScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/settings_screens/SettingsHelpScreen.fxml")
public class SettingsHelpScreenController implements FxmlView<SettingsHelpScreenVM>, Initializable {

    @FXML
    private Panel contactPanel, faqPanel;
//    @FXML
//    private TitledPane whatIsScholarlyPane, getScholarlyPane, firstDifferencePane,  secondDifferencePane;
    @FXML
    private VBox contactVBox, faqVBox, contactDetails, faqDetails;
    @FXML
    private HBox whatsAppPane, gmailPane, websitePane;
    @FXML
    private Button backButton;
    @FXML
    private ToggleButton faqDropdown, contactDropdown;
    @FXML
    private ImageView faqImage, contactImage, phoneImage, whatsAppImage, mailImage, websiteImage, linkedInImage, facebookImage, twitterImage, instagramImage;
//    @FXML
//    private Label firstDifferenceText, secondDifferenceText, whoIsScholarlyForText, makePaymentText;

    private ImageView faqOpenDropdownIcon, contactOpenDropdownIcon;
    private ImageView faqCloseDropdownIcon, contactCloseDropdownIcon;

    MainApplication application = new MainApplication();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFAQ();
        initializeViews();
//        initializeFonts();

        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(faqDropdown, contactDropdown);

        ToggleGroup aboutScholarlyToggle = new ToggleGroup();
//        aboutScholarlyToggle.getToggles().addAll((Toggle) whatIsScholarlyPane, (Toggle) getScholarlyPane, (Toggle) firstDifferencePane, (Toggle) secondDifferencePane);

        contactPanel.setOnMouseClicked(event -> {
            contactDropdown.setSelected(!contactDropdown.isSelected());
        });
        faqPanel.setOnMouseClicked(event -> {
            faqDropdown.setSelected(!faqDropdown.isSelected());
        });

        contactVBox.getChildren().remove(contactDetails);
        faqVBox.getChildren().remove(faqDetails);

        contactDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                contactVBox.getChildren().add(contactDetails);
                contactDropdown.setGraphic(contactCloseDropdownIcon);
            } else {
                contactVBox.getChildren().remove(contactDetails);
                contactDropdown.setGraphic(contactOpenDropdownIcon);
            }
        }));

        faqDropdown.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                faqVBox.getChildren().add(faqDetails);
                faqDropdown.setGraphic(faqCloseDropdownIcon);
            } else {
                faqVBox.getChildren().remove(faqDetails);
                faqDropdown.setGraphic(faqOpenDropdownIcon);
            }
        }));

        whatsAppPane.setOnMouseClicked(event -> {
            application.openBrowser(Constants.WHATSAPP_URL);
        });

        gmailPane.setOnMouseClicked(event -> {
            application.openBrowser(Constants.GMAIL_URL);
        });

        websitePane.setOnMouseClicked(event -> {
            application.openBrowser(AppProperties.getInstance().getWebsite());
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.SETTINGS_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

        linkedInImage.setOnMouseClicked(event -> {
            application.openBrowser(Constants.LINKEDIN_URL);
        });
        facebookImage.setOnMouseClicked(event -> {
            application.openBrowser(Constants.FACEBOOK_URL);
        });
        twitterImage.setOnMouseClicked(event -> {
            application.openBrowser(Constants.TWITTER_URL);
        });
        instagramImage.setOnMouseClicked(event -> {
            application.openBrowser(Constants.INSTAGRAM_URL);
        });

    }

    private void setUpFAQ() {
        Color textColor = Color.valueOf("#053500");
        faqDetails.getChildren().removeAll();

        for (FAQ faq: AppProperties.getInstance().getFaqs()) {
            VBox vBox = new VBox(10);

            Label label = new Label(faq.getTitle());
            label.setFont(Font.font(18));
            label.setTextFill(textColor);
            label.setPadding(new Insets(5, 0, 5, 0));

            vBox.getChildren().add(label);

            for (FaqItem faqItem: faq.getFaqItems()) {
                TitledPane titledPane = new TitledPane();
                titledPane.setText(faqItem.getQuestion());
                titledPane.setTextFill(textColor);
                titledPane.setExpanded(false);

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);

                Label ansLabel = new Label(faqItem.getAnswer());
                ansLabel.setFont(Font.font(16));
                ansLabel.setPadding(new Insets(5, 0, 0, 0));

                hBox.getChildren().add(ansLabel);

                titledPane.setContent(hBox);
                titledPane.setFont(Font.font(16));

                vBox.getChildren().add(titledPane);
            }

            vBox.setPadding(new Insets(0, 20, 0, 20));
            faqDetails.getChildren().add(vBox);
        }
    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        faqImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/faq_icon.png").toString()));
        contactImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/contact_icon.png").toString()));
        phoneImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/phone_icon.png").toString()));
        whatsAppImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/whatsapp_icon.png").toString()));
        mailImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/mail_icon.png").toString()));
        websiteImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/contact_icon.png").toString()));

        linkedInImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/twitter_icon.png").toString()));
        facebookImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/facebook_icon.png").toString()));
        twitterImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/twitter_icon.png").toString()));
        instagramImage.setImage(new Image(getClass().getResource("/drawable/settings_screen_images/instagram_icon.png").toString()));

        faqOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));
        contactOpenDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/open_dropdown_icon.png").toString()));

        faqCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));
        contactCloseDropdownIcon = new ImageView(new Image(getClass().getResource("/drawable/activate_screen_images/close_dropdown_icon.png").toString()));

        faqDropdown.setGraphic(faqOpenDropdownIcon);
        contactDropdown.setGraphic(contactOpenDropdownIcon);

//        whatIsScholarlyPane.setContentDisplay(ContentDisplay.RIGHT);

        backButton.setBackground(Background.EMPTY);
        faqDropdown.setBackground(Background.EMPTY);
        contactDropdown.setBackground(Background.EMPTY);
//        whatIsScholarlyPane.setBackground(Background.EMPTY);
    }

    /*private void initializeFonts() {
        firstDifferenceText.setText("1. CBT: With this app you can time yourself and take exams just like JAMB CBT." + System.lineSeparator() +
                "2. All in one: Rather than having different booklets for different subjects, all your JAMB subjects are in this app." + System.lineSeparator() +
                "3. Customer Support: With this app, you have access to people you can ask questions or make complaints." + System.lineSeparator() +
                "4. Audio Question and Answers: Questions, Answers and Explanation can be read to you, meaning you can learn by listening rather than reading." + System.lineSeparator() +
                "5. Save Questions for later: You can save questions that matter to you so that you can easily find them later." + System.lineSeparator() +
                "6. Detailed Result Breakdown: See how you performed at various subjects with visual representations like bar chart and tables." + System.lineSeparator() +
                "7. Built-in Scientific Calculator: Us the app's JAMB-like calculator without having to use an external device or calculator." + System.lineSeparator() +
                "8. Practice one or more subjects at once: Take a test or exam with more than one subjects at a time" + System.lineSeparator() +
                "9. Shuffle Questions/Options: Test your retention ability by rearranging the questions and options order." + System.lineSeparator() +
                "10. Profile: Personalize your app experience using our feature rich profile screen, update your picture, full name, phone number etc."
        );

        secondDifferenceText.setText("1. Explanation to every question: This app provides concise explanation to every question be it mathematical or non-mathematical." + System.lineSeparator() +
                "2. Interactive Learning: With features like CBT Game and Study Past Questions, you learn in a fun-filled and interactive manner." + System.lineSeparator() +
                "3. Customizable: Customize the app features to suit your needs with its preference setting." + System.lineSeparator() +
                "4. Easy to use: This app has been designed with you in mind. It affords users a wonderful user experience with its intuitive user interface." + System.lineSeparator() +
                "5. Notifications/SMS: Get notified and alerted with the latest information on admissions, universities." + System.lineSeparator() +
                "6. Share questions and scores: With this app, you can share difficult questions you encountered to your friends via different social media platform. Also share your results with friends"
        );

        whoIsScholarlyForText.setText("Any student who wants to learn, pass exams and gain admission into higher institutions.");

        makePaymentText.setText("You can pay with 3 options;" + System.lineSeparator() +
                "1. Bank Transfer or Deposit: Pay with any banking app or go to the bank to make payment. After payment you will need to send an evidence of payment to our email, scholarlyapp.ng@gmail.com." + System.lineSeparator() +
                "2. Cards: This is done online within the app with your ATM and your app gets activated instantly." + System.lineSeparator() +
                "3. Online Bank Transfer: This is performed within the app with only your account number and does not require the use of ATM card."
        );

    }*/
}
