package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.listItems.AudioItem;
import com.scholarly.utme.ui.cellFactories.AudioGridCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.AudiosGridScreenVM;
import com.scholarly.utme.viewmodels.VideosGridScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/AudiosGridScreen.fxml")
public class AudiosGridScreenController implements FxmlView<AudiosGridScreenVM>, Initializable {

    @FXML
    private StackPane searchPane;

    @FXML
    private Button backButton, mostPopularButton, newButton, highlyRatedButton;

    @FXML
    private ImageView searchIcon;

    @FXML
    private GridView<AudioItem> audiosGrid;

    @InjectViewModel
    private AudiosGridScreenVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        AudioItem audioItem = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        AudioItem audioItem1 = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        AudioItem audioItem2 = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        AudioItem audioItem3 = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        AudioItem audioItem4 = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");

        ObservableList<AudioItem> audios = FXCollections.observableArrayList();
        audios.addAll(audioItem, audioItem1, audioItem2, audioItem3, audioItem4);

        audiosGrid.setCellFactory(new AudioGridCellFactory());
        audiosGrid.setItems(audios);


        backButton.setOnAction(event -> {
            ViewSwitcher.passData("audiosButton");
            ViewSwitcher.showScreen(View.HOME_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        searchIcon.setImage(new Image(getClass().getResource("/drawable/search_icon2.png").toString()));
    }

    private void initializeFonts() {
        mostPopularButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        newButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        highlyRatedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }
}
