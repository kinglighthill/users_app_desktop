package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.Novel.Genre;
import com.scholarly.utme.data.model.novels.Novel.Type;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.NovelScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
    private Button dismissButton, jambProseButton, africanProseButton, nonAfricanProseButton, africanDramaButton, nonAfricanDramaButton, shakespeareanTextButton, africanPoetryButton, nonAfricanPoetryButton, readButton;

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

        novelImage.imageProperty().bind(viewModel.novelImageProperty());
        novelDescription.textProperty().bind(viewModel.novelDescriptionProperty());
        chaptersLabel.textProperty().bind(viewModel.novelChaptersProperty());
        authorLabel.textProperty().bind(viewModel.novelAuthorProperty());

        Pair<String, ObservableList<Novel>> jambProse = new Pair<>(jambProseButton.getId(), viewModel.getNovels(Type.JAMB, Genre.PROSE));
        jambProseButton.setUserData(jambProse);
        jambProseList.setItems(viewModel.getFirstFourNovels(Type.JAMB, Genre.PROSE));
        jambProseList.getSelectionModel().select(0);

        viewModel.setSelectedNovel(jambProseList.getItems().get(0));

        jambProseList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        });


        Pair<String, ObservableList<Novel>> africanProse = new Pair<>(africanProseButton.getId(), viewModel.getNovels(Type.AFRICAN, Genre.PROSE));
        africanProseButton.setUserData(africanProse);
        africanProseList.setItems(viewModel.getFirstFourNovels(Type.AFRICAN, Genre.PROSE));

        africanProseList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> nonAfricanProse = new Pair<>(nonAfricanProseButton.getId(), viewModel.getNovels(Type.NON_AFRICAN, Genre.PROSE));
        nonAfricanProseButton.setUserData(nonAfricanProse);
        nonAfricanProseList.setItems(viewModel.getFirstFourNovels(Type.NON_AFRICAN, Genre.PROSE));

        nonAfricanProseList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> africanDrama = new Pair<>(africanDramaButton.getId(), viewModel.getNovels(Type.AFRICAN, Genre.DRAMA));
        africanDramaButton.setUserData(africanDrama);
        africanDramaList.setItems(viewModel.getFirstFourNovels(Type.AFRICAN, Genre.DRAMA));

        africanDramaList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> nonAfricanDrama = new Pair<>(nonAfricanDramaButton.getId(), viewModel.getNovels(Type.NON_AFRICAN, Genre.DRAMA));
        nonAfricanDramaButton.setUserData(nonAfricanDrama);
        nonAfricanDramaList.setItems(viewModel.getFirstFourNovels(Type.NON_AFRICAN, Genre.DRAMA));

        nonAfricanDramaList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> shakespearean = new Pair<>(shakespeareanTextButton.getId(), viewModel.getNovels(Type.SHAKESPEAREAN, Genre.TEXT));
        shakespeareanTextButton.setUserData(shakespearean);
        shakespeareanTextList.setItems(viewModel.getFirstFourNovels(Type.SHAKESPEAREAN, Genre.TEXT));

        shakespeareanTextList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> africanPoetry = new Pair<>(africanPoetryButton.getId(), viewModel.getNovels(Type.AFRICAN, Genre.POETRY));
        africanPoetryButton.setUserData(africanPoetry);
        africanPoetryList.setItems(viewModel.getFirstFourNovels(Type.AFRICAN, Genre.POETRY));

        africanPoetryList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        Pair<String, ObservableList<Novel>> nonAfricanPoetry = new Pair<>(nonAfricanPoetryButton.getId(), viewModel.getNovels(Type.NON_AFRICAN, Genre.POETRY));
        nonAfricanPoetryButton.setUserData(nonAfricanPoetry);
        nonAfricanPoetryList.setItems(viewModel.getFirstFourNovels(Type.NON_AFRICAN, Genre.POETRY));

        nonAfricanPoetryList.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            viewModel.setSelectedNovel(newValue);
        }));


        dismissButton.setOnAction(event -> {
            if (dontShowButton.isSelected()) {
                centerBox.getChildren().remove(0);
            }else {
                centerBox.getChildren().get(0).setVisible(false);
            }
        });

        readButton.setOnAction(event -> {
            ViewSwitcher.passData(viewModel.getSelectedNovel());
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });

    }

    @SuppressWarnings("unchecked")
    private void initializeViews() {
        List<ButtonBase> buttons = FXCollections.observableArrayList(jambProseButton, africanProseButton, nonAfricanProseButton, africanDramaButton, nonAfricanDramaButton, shakespeareanTextButton, africanPoetryButton, nonAfricanPoetryButton);
        for (ButtonBase button : buttons) {
            button.setBackground(Background.EMPTY);

            button.setOnAction(event -> {
                ViewSwitcher.passData(button.getUserData());
                ViewSwitcher.showScreen(View.NOVEL_GRID_SCREEN);
            });
        }

        List<ListView<Novel>> listViews = FXCollections.observableArrayList(jambProseList, africanProseList, nonAfricanProseList, africanDramaList, nonAfricanDramaList, shakespeareanTextList, africanPoetryList, nonAfricanPoetryList);
        for (ListView<Novel> listView : listViews) {
            listView.setBackground(Background.EMPTY);
            listView.setCellFactory(new NovelListCellFactory());
        }

        dismissButton.setBackground(Background.EMPTY);
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
