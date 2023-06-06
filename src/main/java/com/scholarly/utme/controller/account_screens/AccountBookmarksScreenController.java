package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.BookmarkItem;
import com.scholarly.utme.ui.cellFactories.BookmarkGridCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.AccountBookmarksScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountBookmarksScreen.fxml")
public class AccountBookmarksScreenController implements FxmlView<AccountBookmarksScreenVM>, Initializable {

    @FXML
    private GridView<BookmarkItem> bookmarksGrid;

    @FXML
    private ImageView searchImage, appImage;

    @FXML
    private Button backButton;

    @FXML
    private ToggleButton practiceButton, novelsButton, audiosButton, videosButton, notesButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Label scholarlyText;

    private final ToggleGroup toggleGroup = new ToggleGroup();


    private static final String PRESSED_BUTTON_STYLE = "-fx-background-color: rgba(255, 255, 255, 0.1); -fx-border-color: #FFFFFF #FFFFFF #FFFFFF #FF9900; -fx-border-width: 0 0 0 5;";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        toggleGroup.getToggles().addAll(practiceButton, novelsButton, audiosButton, videosButton, notesButton);

        practiceButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                changeButtonStyle(practiceButton);
            }
        }));

        novelsButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                changeButtonStyle(novelsButton);
            }
        }));

        audiosButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                changeButtonStyle(audiosButton);
            }
        }));

        videosButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                changeButtonStyle(videosButton);
            }
        }));

        notesButton.selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                changeButtonStyle(notesButton);
            }
        }));

        BookmarkItem item1 = new BookmarkItem("bookmark_practice_image", "CBT Practice Test", "Question 5", "CBT Practice");
        BookmarkItem item2 = new BookmarkItem("bookmark_novel_image", "Sweet Sixteen", "Chapter 7: Beauty", "Novel");
        BookmarkItem item3 = new BookmarkItem("bookmark_video_image", "Biology", "The Central Nervous System", "Video");
        BookmarkItem item4 = new BookmarkItem("bookmark_practice_image", "CBT Practice Test", "Question 5", "CBT Practice");
        BookmarkItem item5 = new BookmarkItem("bookmark_novel_image", "Sweet Sixteen", "Chapter 7: Beauty", "Novel");
        BookmarkItem item6 = new BookmarkItem("bookmark_video_image", "Biology", "The Central Nervous System", "Video");

        ObservableList<BookmarkItem> bookmarks = FXCollections.observableArrayList(item1, item2, item3, item4, item5, item6);
        bookmarksGrid.setCellFactory(new BookmarkGridCellFactory());
        bookmarksGrid.setItems(bookmarks);

        searchTextField.setOnMouseClicked(mouseEvent -> {
            searchImage.setVisible(false);
        });

        searchTextField.setOnMouseExited(mouseEvent -> {
            searchImage.setVisible(true);
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData("accountScreen"));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        searchImage.setImage(new Image(getClass().getResource("/drawable/search_icon.png").toString()));
        appImage.setImage(new Image(getClass().getResource("/drawable/app_logo.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
        searchTextField.setBackground(Background.EMPTY);
        practiceButton.setBackground(Background.EMPTY);
        novelsButton.setBackground(Background.EMPTY);
        audiosButton.setBackground(Background.EMPTY);
        videosButton.setBackground(Background.EMPTY);
        notesButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
        scholarlyText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 20));
        practiceButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        novelsButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        audiosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        videosButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        notesButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }

    private void changeButtonStyle(ToggleButton pressedButton) {
        practiceButton.setStyle(null);
        novelsButton.setStyle(null);
        audiosButton.setStyle(null);
        videosButton.setStyle(null);
        notesButton.setStyle(null);

        pressedButton.setStyle(PRESSED_BUTTON_STYLE);
    }
}
