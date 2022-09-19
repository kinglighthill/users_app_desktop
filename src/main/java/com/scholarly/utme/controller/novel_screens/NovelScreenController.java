package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelCategory;
import com.scholarly.utme.ui.cellFactories.NovelListCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/novel_screens/NovelScreen.fxml")
public class NovelScreenController implements FxmlView<NovelScreenVM>, Initializable {
    private static final String TAG = "NovelScreenController: ";

    @FXML
    private ScrollPane centerScrollPane;

    @FXML
    private StackPane centerStackPane;

    @FXML
    private Label infoText, novelDescription, authorLabel, chaptersLabel, timeText;

    @FXML
    private RadioButton dontShowButton;

    @FXML
    private Button dismissButton, readButton;

    @FXML
    private VBox centerVBox, novelsVBox;

    @FXML
    private ImageView novelImage, authorIcon, chaptersIcon, timeIcon;

    @InjectViewModel
    private NovelScreenVM viewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFont();

        viewModel.getGenres().forEach(genre -> {
            List<NovelCategory> categories = viewModel.getGenreCategoryMap().get(genre);

            categories.forEach(novelCategory -> {

                String categoryGenreTitle = novelCategory.getCategory() + " " + genre.getGenre();
//                System.out.println(TAG + categoryGenreTitle);

                ObservableList<Novel> novels = viewModel.getNovels(novelCategory, genre);

                if (!novels.isEmpty()) {
                    Panel panel = new Panel();

                    Label header = new Label(categoryGenreTitle);
                    header.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, 18));

                    Button viewAllButton = new Button("View all");
                    viewAllButton.setTextFill(Paint.valueOf("#12AF20"));
                    viewAllButton.setStyle("-fx-border-color: #12AF20; -fx-border-radius: 5;");
                    viewAllButton.setPadding(new Insets(5, 10, 5, 10));
                    viewAllButton.setBackground(Background.EMPTY);
                    viewAllButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
                    viewAllButton.setOnAction(event -> {
                        ViewSwitcher.passData(new NovelGridScreenController.InitialData(categoryGenreTitle, novels));
                        ViewSwitcher.showScreen(View.NOVEL_GRID_SCREEN);
                    });

                    ListView<Novel> listView = new ListView<>(FXCollections.observableArrayList(novels.stream().limit(4).collect(Collectors.toList())));
                    listView.setOrientation(Orientation.HORIZONTAL);
                    listView.setPrefSize(600, 270);
                    listView.setBackground(Background.EMPTY);
                    listView.setCellFactory(new NovelListCellFactory());
                    listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                        viewModel.setSelectedNovel(newValue);
                    });

                    panel.setLeft(header);
                    panel.setRight(viewAllButton);
                    panel.setBottom(listView);

                    novelsVBox.getChildren().add(panel);

                }
//                System.out.println(TAG + "Novels with category: " + novelCategory.getCategory() + " AND genre: " + genre.getGenre() + " ARE -> " + novels);
            });
        });


        viewModel.selectedNovelProperty().addListener(((observable, oldValue, newValue) -> {
            authorIcon.setVisible(true);
            chaptersIcon.setVisible(true);
            timeIcon.setVisible(true);
            readButton.setVisible(true);
            timeText.setVisible(true);

            novelImage.setImage(new Image(getClass().getResource("/drawable/novel_images/" + newValue.getImagePath()).toString()));
            novelDescription.setText(newValue.getAbout());
            authorLabel.setText(viewModel.getAuthor(newValue).getName());
            chaptersLabel.setText(newValue.getChapters());

        }));


        dismissButton.setOnAction(event -> {
            if (dontShowButton.isSelected()) {
                centerVBox.getChildren().remove(0);
            } else {
                centerVBox.getChildren().get(0).setVisible(false);
            }
        });

        readButton.setOnAction(event -> {
            ViewSwitcher.passData(new NovelChapterListController.InitialData(viewModel.getSelectedNovel(), viewModel.getAuthor(viewModel.getSelectedNovel())));
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });

    }

    private void initializeViews() {
        dismissButton.setBackground(Background.EMPTY);
        centerScrollPane.setBackground(Background.EMPTY);

        authorIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/author_icon.png").toString()));
        chaptersIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/chapter_icon.png").toString()));
        timeIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/time_icon.png").toString()));
    }

    private void initializeFont() {
        infoText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        novelDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }
}
