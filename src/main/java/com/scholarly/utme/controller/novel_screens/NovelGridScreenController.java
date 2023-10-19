package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.controller.HomeScreenController;
import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.ui.cellFactories.NovelGridCellFactory;
import com.scholarly.utme.ui.utils.Screens;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.novel_screens.NovelGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FxmlPath("/layouts/novel_screens/NovelGridScreen.fxml")
public class NovelGridScreenController implements FxmlView<NovelGridScreenVM>, Initializable {

    @FXML
    private GridView<NovelModel> novelGridView;

    @FXML
    private TextField searchTextField;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Label scholarlyText, pageTitle;

    @FXML
    private Button backButton;


    @InjectViewModel
    private NovelGridScreenVM viewModel;

    private static ObservableList<NovelModel> novelItems = FXCollections.observableArrayList();

    private static String appBarTitle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        viewModel.processInitialData(getInitialData());

        if (novelItems.isEmpty()) {
            novelItems.addAll(viewModel.getNovelModels());
        }

        if (viewModel.getNovelType() != null) {
            appBarTitle = viewModel.getNovelType();
        }
        pageTitle.setText(appBarTitle);

        novelGridView.setCellFactory(new NovelGridCellFactory());
        novelGridView.setItems(novelItems);

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            if (text.isBlank()) {
                novelGridView.setItems(novelItems);
                return change;
            }

            List<NovelModel> searchedNovels = new ArrayList<>();
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    for (NovelModel novel : novelItems) {
                        if (novel.getNovel().getName().contains(text)) {
                            searchedNovels.add(novel);
                        }
                    }

                    Platform.runLater(() -> {
                        novelGridView.setItems(FXCollections.observableArrayList(searchedNovels));
                    });
                    return null;
                }
            };
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();

            return change;
        });

        searchTextField.setTextFormatter(textFormatter);

        searchTextField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            searchIcon.setVisible(!newValue);
        }));


        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.NOVEL_SCREEN);
        });


    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        searchIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/novel_search_icon.png").toString()));
    }

    private void initializeFonts() {

    }


    private InitialData getInitialData() {
        return (InitialData) ViewSwitcher.retrieveData();
    }

    public static class InitialData {
        private String categoryGenreTitle;
        private ObservableList<NovelModel> novelModels;

        public InitialData(String categoryGenreTitle, ObservableList<NovelModel> novelModels) {
            this.categoryGenreTitle = categoryGenreTitle;
            this.novelModels = novelModels;
        }

        public String getCategoryGenreTitle() {
            return categoryGenreTitle;
        }

        public ObservableList<NovelModel> getNovelModels() {
            return novelModels;
        }
    }
}
