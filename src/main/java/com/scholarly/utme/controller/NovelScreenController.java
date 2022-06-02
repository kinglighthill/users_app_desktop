package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelAuthor;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.viewmodels.NovelScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/NovelScreen.fxml")
public class NovelScreenController implements FxmlView<NovelScreenVM>, Initializable {

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private StackPane centerStackPane;

    @FXML
    private Label infoText, proseText, novelDescription, authorLabel, chaptersLabel;

    @FXML
    private RadioButton dontShowButton;

    @FXML
    private Button dismissButton, viewAllButton;

    @FXML
    private VBox centerBox;

    @FXML
    private ListView<Novel> jambNovelsList;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;

    @InjectViewModel
    private NovelScreenVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFont();

        jambNovelsList.setItems(viewModel.getNovels());
        jambNovelsList.setCellFactory(new NovelListCellFactory());

        Novel firstNovel = jambNovelsList.getItems().get(0);
        novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + firstNovel.getImagePath()).toString()));
        novelDescription.setText(firstNovel.getAbout());
        chaptersLabel.setText(firstNovel.getChaptersCount() + " chapters");
        authorLabel.setText(viewModel.getAuthor(firstNovel));

        jambNovelsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        });


        dismissButton.setOnAction(event -> {
            centerBox.getChildren().get(0).setVisible(false);
        });

    }

    private void initializeViews() {
        centerScrollPane.setBackground(Background.EMPTY);
        jambNovelsList.setBackground(Background.EMPTY);
        viewAllButton.setBackground(Background.EMPTY);

        novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/life_changer.jpg").toString()));
        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));
    }

    private void initializeFont() {
        infoText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        proseText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.SIXTEEN.size));

        dontShowButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        dismissButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        viewAllButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }
}
