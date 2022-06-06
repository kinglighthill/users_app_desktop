package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelChapter;
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

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/NovelChapterListScreen.fxml")
public class NovelChapterListController implements FxmlView<NovelChapterListVM>, Initializable {

    @FXML
    private Button backButton;

    @FXML
    private Label pageTitle, authorLabel, chaptersLabel;

    @FXML
    private ListView<NovelChapter> chapterListView;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;


    @InjectViewModel
    private NovelChapterListVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        viewModel.processInitialData(getInitialData());

        pageTitle.setText(viewModel.getNovel().getName());
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);


    }

    private Novel getInitialData() {
        return (Novel) ViewSwitcher.retrieveData();
    }
}
