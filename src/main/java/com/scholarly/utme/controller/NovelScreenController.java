package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.Novel.Genre;
import com.scholarly.utme.data.model.novels.Novel.Type;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.viewmodels.NovelScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
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
    private Label infoText, jambProseLabel, africanProseLabel, nonAfricanProseLabel, africanDramaLabel, nonAfricanDramaLabel, shakespeareanLabel, africanPoetryLabel, nonAfricanPoetryLabel,  novelDescription, authorLabel, chaptersLabel;

    @FXML
    private RadioButton dontShowButton;

    @FXML
    private Button dismissButton, jambProseButton, africanProseButton, nonAfricanProseButton, africanDramaButton, nonAfricanDramaButton, shakespeareanTextButton, africanPoetryButton, nonAfricanPoetryButton;

    @FXML
    private VBox centerBox;

    @FXML
    private ListView<Novel> jambProseList, africanProseList, nonAfricanProseList, africanDramaList, nonAfricanDramaList, shakespeareanTextList, africanPoetryList, nonAfricanPoetryList;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;

    @InjectViewModel
    private NovelScreenVM viewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFont();

        jambProseList.setCellFactory(new NovelListCellFactory());
        jambProseList.setItems(viewModel.getNovels(Type.JAMB, Genre.PROSE));

        Novel firstNovel = jambProseList.getItems().get(0);
        novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + firstNovel.getImagePath()).toString()));
        novelDescription.setText(firstNovel.getAbout());
        chaptersLabel.setText(firstNovel.getChaptersCount() + " chapters");
        authorLabel.setText(viewModel.getAuthor(firstNovel));

        jambProseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        });

        africanProseList.setCellFactory(new NovelListCellFactory());
        africanProseList.setItems(viewModel.getNovels(Type.AFRICAN, Genre.PROSE));
        africanProseList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        nonAfricanProseList.setCellFactory(new NovelListCellFactory());
        nonAfricanProseList.setItems(viewModel.getNovels(Type.NON_AFRICAN, Genre.PROSE));
        nonAfricanProseList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        africanDramaList.setCellFactory(new NovelListCellFactory());
        africanDramaList.setItems(viewModel.getNovels(Type.AFRICAN, Genre.DRAMA));
        africanDramaList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        nonAfricanDramaList.setCellFactory(new NovelListCellFactory());
        nonAfricanDramaList.setItems(viewModel.getNovels(Type.NON_AFRICAN, Genre.DRAMA));
        nonAfricanDramaList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        shakespeareanTextList.setCellFactory(new NovelListCellFactory());
        shakespeareanTextList.setItems(viewModel.getNovels(Type.SHAKESPEAREAN, Genre.TEXT));
        shakespeareanTextList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        africanPoetryList.setCellFactory(new NovelListCellFactory());
        africanPoetryList.setItems(viewModel.getNovels(Type.AFRICAN, Genre.POETRY));
        africanPoetryList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));

        nonAfricanPoetryList.setCellFactory(new NovelListCellFactory());
        nonAfricanPoetryList.setItems(viewModel.getNovels(Type.NON_AFRICAN, Genre.POETRY));
        nonAfricanPoetryList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            chaptersLabel.setText(newValue.getChaptersCount() + " chapters");
            authorLabel.setText(viewModel.getAuthor(newValue));
        }));


        dismissButton.setOnAction(event -> {
            if (dontShowButton.isSelected()) {
                centerBox.getChildren().remove(0);
            }else {
                centerBox.getChildren().get(0).setVisible(false);
            }
        });

    }

    @SuppressWarnings("unchecked")
    private void initializeViews() {
        List<ButtonBase> buttons = FXCollections.observableArrayList(dontShowButton, dismissButton, jambProseButton, africanProseButton, nonAfricanProseButton, africanDramaButton, nonAfricanDramaButton, shakespeareanTextButton, africanPoetryButton, nonAfricanPoetryButton);
        for (ButtonBase button : buttons) {
            button.setBackground(Background.EMPTY);
        }

        List<ListView<Novel>> listViews = FXCollections.observableArrayList(jambProseList, africanProseList, nonAfricanProseList, africanDramaList, nonAfricanDramaList, shakespeareanTextList, africanPoetryList, nonAfricanPoetryList);
        for (ListView<Novel> listView : listViews) {
            listView.setBackground(Background.EMPTY);
        }

        centerScrollPane.setBackground(Background.EMPTY);

        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));
    }

    private void initializeFont() {
        List<ButtonBase> buttons = FXCollections.observableArrayList(dontShowButton, dismissButton, jambProseButton, africanProseButton, nonAfricanProseButton, africanDramaButton, nonAfricanDramaButton, shakespeareanTextButton, africanPoetryButton, nonAfricanPoetryButton);

        for (ButtonBase button : buttons) {
            button.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));
        }

        infoText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
        jambProseLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        africanProseLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        nonAfricanProseLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        africanDramaLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        nonAfricanDramaLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        shakespeareanLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        africanPoetryLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        nonAfricanPoetryLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));

        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }
}
