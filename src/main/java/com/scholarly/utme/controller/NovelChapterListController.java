package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.ui.cellFactories.NovelChapterListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.NovelChapterListVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/NovelChapterListScreen.fxml")
public class NovelChapterListController implements FxmlView<NovelChapterListVM>, Initializable {

    @FXML
    private Button backButton, readButton;

    @FXML
    private Label pageTitle, authorLabel, chaptersLabel;

    @FXML
    private ListView<NovelChapter> chaptersList;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;


    @InjectViewModel
    private NovelChapterListVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFont();

        viewModel.processInitialData(getInitialData());

        pageTitle.setText(viewModel.getNovel().getName());
        novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + viewModel.getNovel().getImagePath()).toString()));
        authorLabel.setText(viewModel.getAuthor(viewModel.getNovel()));
        chaptersLabel.setText(viewModel.getNovel().getChapters());

        chaptersList.setCellFactory(new NovelChapterListCellFactory());
        chaptersList.setItems(viewModel.getChapters());
        chaptersList.getSelectionModel().select(0);

        readButton.setOnAction(event -> {
            Object novelData = new NovelChapterListVM.NovelState(viewModel.getNovel(), viewModel.getChapters());

            ViewSwitcher.passData(novelData);
            ViewSwitcher.showScreen(View.NOVEL_CONTENT_SCREEN);
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("novelsButton");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        chaptersList.setBackground(Background.EMPTY);

        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));
    }

    private void initializeFont() {
//        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }

    private Novel getInitialData() {
        return (Novel) ViewSwitcher.retrieveData();
    }
}
