package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.novels.NovelCategoryGenre;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.novel_screens.NovelScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.scholarly.utme.util.Constants.PREF_KEY_DONT_SHOW_NOVEL_SCREEN_PROMPT;

@FxmlPath("/layouts/novel_screens/NovelScreen.fxml")
public class NovelScreenController implements FxmlView<NovelScreenVM>, Initializable {
    private static final String TAG = "NovelScreenController: ";

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private StackPane centerStackPane;

    @FXML
    private Label infoText, novelDescription, authorLabel, chaptersLabel, timeText, pageTitle;

    @FXML
    private RadioButton dontShowButton;

    @FXML
    private Button dismissButton, readButton, backButton;

    @FXML
    private VBox centerVBox, novelsVBox, novelPrompt;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;

    @InjectViewModel
    private NovelScreenVM viewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();
        initializeFont();

        Map<Integer, ListView<NovelModel>> listViews = new HashMap<>();
        AtomicReference<VBox> selectNovelBox = new AtomicReference<>();
        AtomicInteger selectedListViewIndex = new AtomicInteger();

        for (Map.Entry<NovelCategoryGenre, List<NovelModel>> entry : viewModel.getGenresCategories().entrySet()) {
            NovelCategoryGenre novelCategoryGenre = entry.getKey();
            ObservableList<NovelModel> novelModels = FXCollections.observableArrayList(entry.getValue());

            String categoryGenreTitle = novelCategoryGenre.getTitle();

            if (!novelModels.isEmpty()) {
                Panel panel = new Panel();

                Label header = new Label(categoryGenreTitle);
                header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));

                Button viewAllButton = new Button("View all");
                viewAllButton.setTextFill(Paint.valueOf("#12AF20"));
                viewAllButton.setStyle("-fx-border-color: #12AF20; -fx-border-radius: 5; -fx-cursor: hand;");
                viewAllButton.setPadding(new Insets(5, 10, 5, 10));
                viewAllButton.setBackground(Background.EMPTY);
                viewAllButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
                viewAllButton.setOnAction(event -> {
                    ViewSwitcher.passData(new NovelGridScreenController.InitialData(categoryGenreTitle, novelModels));
                    ViewSwitcher.showScreen(View.NOVEL_GRID_SCREEN);
                });

                ListView<NovelModel> listView = new ListView<>(FXCollections.observableArrayList(novelModels.stream().limit(4).collect(Collectors.toList())));
                listView.setOrientation(Orientation.HORIZONTAL);
                listView.setPrefSize(600, 270);
                listView.setBackground(Background.EMPTY);
                listView.setCellFactory(new NovelListCellFactory());
                listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    viewModel.setSelectedNovelModel(newValue);
                });
                listViews.put(novelCategoryGenre.hashCode(), listView);

                TilePane tilePane = new TilePane(Orientation.HORIZONTAL, 15, 10);

                novelModels.forEach(novelModel -> {
                    VBox novelVBox = new VBox(10);
                    novelVBox.setPadding(new Insets(10, 15, 10, 15));

                    ImageView novelImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/drawable/novel_images/" + novelModel.getNovel().getImagePath())).toString()));
                    novelImage.setFitHeight(150);
                    novelImage.setFitWidth(90);

                    StackPane stackPane = new StackPane(novelImage);
                    stackPane.setStyle("-fx-background-color: #D7D7D7; -fx-background-radius: 8;");
                    StackPane.setMargin(novelImage, new Insets(10));

                    Label name = new Label("Name");
                    name.setText(novelModel.getNovel().getName());
                    name.setTextFill(Paint.valueOf("#000000"));
                    name.setPrefWidth(120);
                    name.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));

                    Label chapters = new Label("Chapters");
                    chapters.setText(novelModel.getChapterText());
                    chapters.setTextFill(Paint.valueOf("#12AF20"));

                    VBox bottomBox = new VBox(name, chapters);
                    bottomBox.setPadding(new Insets(5, 0, 0, 0));

                    novelVBox.getChildren().addAll(stackPane, bottomBox);
                    novelVBox.setOnMouseClicked(event -> {
                        ((TilePane) panel.getBottom()).getChildren().forEach(node -> node.setStyle(null));

                        if (selectedListViewIndex.get() != 0 && selectedListViewIndex.get() !=  novelCategoryGenre.hashCode()) {
                            ListView<NovelModel> selectedListView = listViews.get(selectedListViewIndex.get());

                            if (selectedListView != null) {
                                selectedListView.getSelectionModel().clearSelection();
                                selectNovelBox.get().setStyle(null);
                            }
                        }

                        if (viewModel.getSelectedNovelModel() == novelModel) {
                            novelVBox.setStyle(null);
                            viewModel.setSelectedNovelModel(null);
                        } else {
                            novelVBox.setStyle("-fx-border-color: #12AF20; -fx-border-radius: 8;");
//                            System.out.println(TAG + "NovelVbox -> " + novelVBox);
                            viewModel.setSelectedNovelModel(novelModel);
                        }

                        selectedListViewIndex.set(novelCategoryGenre.hashCode());
                        selectNovelBox.set(novelVBox);
                    });

                    novelVBox.setStyle("-fx-cursor: hand;");
                    tilePane.getChildren().add(novelVBox);
                });

                panel.setLeft(header);
                panel.setRight(viewAllButton);
                panel.setBottom(tilePane);

                novelsVBox.getChildren().add(panel);
            }
        }

        viewModel.selectedNovelModelProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                authorIcon.setVisible(true);
                chaptersIcon.setVisible(true);
                timeIcon.setVisible(true);
                readButton.setVisible(true);
                timeText.setVisible(true);

                novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getNovel().getImagePath()).toString()));
                novelDescription.setText(newValue.getNovel().getAbout());
                authorLabel.setText(viewModel.getAuthor(newValue.getNovel()).getName());
                chaptersLabel.setText(newValue.getChapterText());
                timeText.setText(newValue.getTimeText());
            } else {
                authorIcon.setVisible(false);
                chaptersIcon.setVisible(false);
                timeIcon.setVisible(false);
                readButton.setVisible(false);
                timeText.setVisible(false);

                novelImage.setImage(null);
                novelDescription.setText(null);
                authorLabel.setText(null);
                chaptersLabel.setText(null);
            }
        }));

        dismissButton.setOnAction(event -> {
            if (dontShowButton.isSelected()) {
                PreferencesManager.putBoolean(PREF_KEY_DONT_SHOW_NOVEL_SCREEN_PROMPT+viewModel.getUserId(), true);
                centerVBox.getChildren().remove(novelPrompt);
            } else {
                centerVBox.getChildren().remove(novelPrompt);
            }
        });

        readButton.setOnAction(event -> {
            ViewSwitcher.passData(new NovelChapterListController.InitialData(viewModel.getSelectedNovelModel(), Screens.NOVELS_SCREEN));
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });
    }

    private void initializeViews() {
        dismissButton.setBackground(Background.EMPTY);
        centerScrollPane.setBackground(Background.EMPTY);

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString())));

        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));

        boolean dontShowPrompt = PreferencesManager.getBoolean(PREF_KEY_DONT_SHOW_NOVEL_SCREEN_PROMPT+viewModel.getUserId(), false);
        if (dontShowPrompt) {
            centerVBox.getChildren().remove(novelPrompt);
        }
    }

    private void initializeFont() {
        pageTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 24));
        infoText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }

    public void backButtonClicked(MouseEvent mouseEvent) {
        ViewSwitcher.passData(new LandingScreenController.InitialData(Screens.HOME_SCREEN));
        ViewSwitcher.showScreen(View.LANDING_SCREEN);
    }
}
