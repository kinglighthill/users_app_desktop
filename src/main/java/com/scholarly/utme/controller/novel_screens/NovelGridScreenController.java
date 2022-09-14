package com.scholarly.utme.controller.novel_screens;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.ui.cellFactories.NovelGridCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.novel_screens.NovelGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/novel_screens/NovelGridScreen.fxml")
public class NovelGridScreenController implements FxmlView<NovelGridScreenVM>, Initializable {

    @FXML
    private GridView<Novel> novelGridView;

    @FXML
    private ImageView searchIcon;

    @FXML
    private Label scholarlyText, pageTitle;

    @FXML
    private Button backButton;


    @InjectViewModel
    private NovelGridScreenVM viewModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();
        initializeFonts();

        viewModel.processInitialData(getInitialData());

        pageTitle.setText(viewModel.getNovelType());

        novelGridView.setCellFactory(new NovelGridCellFactory());
        novelGridView.setItems(viewModel.getNovels());


        backButton.setOnAction(event -> {
            ViewSwitcher.passData("novelsButton");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
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
        private ObservableList<Novel> novels;

        public InitialData(String categoryGenreTitle, ObservableList<Novel> novels) {
            this.categoryGenreTitle = categoryGenreTitle;
            this.novels = novels;
        }

        public String getCategoryGenreTitle() {
            return categoryGenreTitle;
        }

        public ObservableList<Novel> getNovels() {
            return novels;
        }
    }
}
