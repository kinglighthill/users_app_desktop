package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.ui.cellFactories.NovelChapterListCellFactory;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.viewmodels.novel_screens.NovelChapterListVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/novel_screens/NovelChapterListScreen.fxml")
public class NovelChapterListController implements FxmlView<NovelChapterListVM>, Initializable {
    private static final String TAG = "NovelChapterListController: ";

    @InjectViewModel
    private NovelChapterListVM viewModel;

    @FXML
    private Button backButton, readButton;
    @FXML
    private Label pageTitle, authorLabel, chaptersLabel, activateHeaderText;
    @FXML
    private ListView<NovelChapter> chaptersList;
    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, /*timeIcon,*/ activateNowCloseIcon, activateNowPadlockIcon, greenTickIcon1, greenTickIcon2, greenTickIcon3, greenTickIcon4, greenTickIcon5;
    @FXML
    private VBox activateNowDialog, dimmer;
    @FXML
    private Button activateNowButton;

    private SimpleObjectProperty<NovelChapter> previouslySelectedChapter = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFont();

        viewModel.processInitialData(getInitialData());

        pageTitle.setText(viewModel.getNovelModel().getNovel().getName());
        novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + viewModel.getNovelModel().getNovel().getImagePath()).toString()));
        authorLabel.setText(viewModel.getAuthor().getName());
        chaptersLabel.setText(viewModel.getNovelModel().getChapterText());

        chaptersList.setCellFactory(new NovelChapterListCellFactory());
        chaptersList.setItems(viewModel.getChapters());
        chaptersList.getSelectionModel().select(0);
        viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());


        chaptersList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            previouslySelectedChapter.set(oldValue);
            if (newValue.isFree()) {
                readButton.setDisable(false);
                viewModel.setSelectedChapter(newValue);
            } else {
                dimmer.setVisible(true);
                Animations.translateIn(activateNowDialog, 300);
                readButton.setDisable(true);
            }

        }));

        readButton.setOnAction(event -> {
            ViewSwitcher.passData(new NovelContentScreenController.InitialData(viewModel.getNovelModel(), viewModel.getChapters(), viewModel.getSelectedChapter()));
            ViewSwitcher.showScreen(View.NOVEL_CONTENT_SCREEN);
        });

        backButton.setOnAction(event -> {;
            if (getInitialData().previousScreen == Screens.NOVELS_SCREEN) {
                ViewSwitcher.showScreen(View.NOVEL_SCREEN);
            } else {
                ViewSwitcher.passData(new NovelGridScreenController.InitialData(null, FXCollections.emptyObservableList()));
                ViewSwitcher.showScreen(View.NOVEL_GRID_SCREEN);
            }
        });

        activateNowCloseIcon.setOnMouseClicked(event -> {
            chaptersList.getSelectionModel().select(previouslySelectedChapter.get());
            dimmer.setVisible(false);
            Animations.translateOut(activateNowDialog, 300);
        });

        activateNowButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.ACTIVATE_SCREEN));
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        chaptersList.setBackground(Background.EMPTY);

        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
//        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));

        activateNowCloseIcon.setImage(new Image(getClass().getResource("/drawable/close_icon.png").toString()));
        activateNowPadlockIcon.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/padlock_icon.png").toString()));
        greenTickIcon1.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon2.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon3.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon4.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
        greenTickIcon5.setImage(new Image(getClass().getResource("/drawable/activate_screen_images/green_tick_icon.png").toString()));
    }

    private void initializeFont() {
//        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

        activateHeaderText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
    }

    private InitialData getInitialData() {
        return (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private NovelModel novelModel;
        private Screens previousScreen;

        public InitialData(NovelModel novelModel, Screens previousScreen) {
            this.novelModel = novelModel;
            this.previousScreen = previousScreen;
        }

        public NovelModel getNovelModel() {
            return novelModel;
        }

        public Screens getPreviousScreen() {
            return previousScreen;
        }
    }
}
