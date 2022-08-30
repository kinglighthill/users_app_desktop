package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.ui.cellFactories.NovelChapterListCellFactory;
import com.scholarly.utme.ui.utils.Animations;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.novel_screens.NovelChapterListVM.NovelState;
import com.scholarly.utme.viewmodels.novel_screens.NovelContentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/novel_screens/NovelContentScreen.fxml")
public class NovelContentScreenController implements FxmlView<NovelContentScreenVM>, Initializable {

    @FXML
    private ScrollPane contentPane;

    @FXML
    private ListView<NovelChapter> chaptersList;

    @FXML
    private Panel questionFooter;

    @FXML
    private VBox dimmer;

    @FXML
    private Button backButton, prevButton, nextButton, takeQuizButton, quitQuizButton, fiftyFiftyButton;

    @FXML
    private Button optionAButton, optionBButton, optionCButton, optionDButton;

    @FXML
    private Label pageTitle, chapterIndex, chapterTitle, chapterContent, chapterCount, fiftyFiftyCount, questionLabel;

    @FXML
    private HBox chapterHeader;

    @FXML
    private VBox chaptersListPane, chapterQuizPane;

    @FXML
    private ImageView bookmarkImage, reportImage, speakerImage;

    @InjectViewModel
    private NovelContentScreenVM viewModel;

    List<Button> options;

    String idleButtonStyle =
            "-fx-background-color: #FF8D19;" +
                    "-fx-background-radius: 5";

    String hoveredButtonStyle =
            "-fx-background-color: #FFA347;" +
                    "-fx-background-radius: 5";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        options = new ArrayList<>();
        options.add(optionAButton);
        options.add(optionBButton);
        options.add(optionCButton);
        options.add(optionDButton);

        initializeViews();
        initializeFont();
        initializeGestures();
        initializeQuiz();

        viewModel.processInitialData(getInitialData());

        chaptersList.setCellFactory(new NovelChapterListCellFactory());
        chaptersList.setItems(viewModel.getChapters());
        chaptersList.getSelectionModel().select(viewModel.getSelectedChapter());

        chaptersList.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            viewModel.setSelectedChapter(newValue);
        }));

        viewModel.selectedChapterProperty().addListener(((observableValue, oldValue, newValue) -> {
            chapterContent.setText(newValue.getDetails().replaceAll("<br>", System.lineSeparator()));
            chapterTitle.setText(newValue.getTitle());
            if (!chapterHeader.getChildren().contains(chapterIndex)) {
                chapterHeader.getChildren().add(0, chapterIndex);
            }
            if (newValue.getPosition() != -1) {
                chapterCount.setText(newValue.getPosition() + " of " + chaptersList.getItems().size());
                chapterIndex.setText("Chapter " + newValue.getPosition() + ":");
            }else {
                chapterCount.setText(chaptersList.getSelectionModel().getSelectedIndex() + 1 + " of " + chaptersList.getItems().size());
                chapterHeader.getChildren().remove(chapterIndex);
            }
        }));


        pageTitle.setText(viewModel.getNovel().getName());
        if (viewModel.getSelectedChapter().getPosition() == -1) {
            chapterHeader.getChildren().remove(chapterIndex);
            chapterCount.setText(chaptersList.getSelectionModel().getSelectedIndex() + 1 + " of " + chaptersList.getItems().size());
        }else {
            chapterCount.setText(viewModel.getSelectedChapter().getPosition() + " of " + chaptersList.getItems().size());
        }
        chapterIndex.setText("Chapter " + viewModel.getSelectedChapter().getPosition() + ":");
        chapterTitle.setText(viewModel.getSelectedChapter().getTitle());
        chapterContent.setText(viewModel.getSelectedChapter().getDetails().replaceAll("<br>", System.lineSeparator()));


        prevButton.disableProperty().bind(Bindings.equal(0, chaptersList.getSelectionModel().selectedIndexProperty()));
        nextButton.disableProperty().bind(Bindings.equal(chaptersList.getSelectionModel().selectedIndexProperty(), chaptersList.getItems().size()-1));

        nextButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            chaptersList.getSelectionModel().select(selectedIndex + 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
        });

        prevButton.setOnAction(event -> {
            int selectedIndex = chaptersList.getSelectionModel().getSelectedIndex();
            chaptersList.getSelectionModel().select(selectedIndex - 1);
            viewModel.setSelectedChapter(chaptersList.getSelectionModel().getSelectedItem());
        });;

        takeQuizButton.setOnAction(event -> {
            Animations.slideIn(chapterQuizPane, 500f, 0f, 500);
            Animations.translateOut(chaptersListPane, 400);
            Animations.fadeIn(dimmer, 500);
        });

        quitQuizButton.setOnAction(event -> {
            Animations.slideOut(chapterQuizPane, 0f, 500f, 500);
            Animations.translateIn(chaptersListPane, 400);
            Animations.fadeOut(dimmer, 500);

        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(viewModel.getNovel());
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });

    }

    private void initializeViews() {
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString())));
        prevButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/novel_images/prev_icon.png").toString())));
        nextButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/novel_images/next_icon.png").toString())));

        bookmarkImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_bookmark.png").toString()));
        reportImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_report.png").toString()));
        speakerImage.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_quiz_speaker.png").toString()));

        chaptersList.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        chapterIndex.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        chapterTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, FontUtil.FontSize.EIGHTEEN.size));
        chapterContent.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
        chapterCount.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.SIXTEEN.size));
    }

    private void initializeGestures() {
        String idleFiftyFiftyStyle = fiftyFiftyButton.getStyle();
        String hoveredFiftyFiftyStyle =
                "-fx-background-color: #73D25E;" +
                        "-fx-background-radius: 300;" +
                        "-fx-border-color: #1B9D01;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 300";

        fiftyFiftyButton.setOnMouseEntered(e -> {
            fiftyFiftyButton.setStyle(hoveredFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.WHITE);
        });
        fiftyFiftyButton.setOnMouseExited(e -> {
            fiftyFiftyButton.setStyle(idleFiftyFiftyStyle);
            fiftyFiftyButton.setTextFill(Color.web("#1B9D01"));
        });

        options.forEach(button -> {
            button.setOnMouseEntered(e -> button.setStyle(hoveredButtonStyle));
            button.setOnMouseExited(e -> button.setStyle(idleButtonStyle));
        });

    }

    private void initializeQuiz() {

    }

    private NovelState getInitialData() {
        NovelState novelState = (NovelState) ViewSwitcher.retrieveData();
        return novelState;
    }


}
