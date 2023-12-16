package com.scholarly.utme.controller.audio_video_screens;

import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.cellFactories.VideoGridCellFactory;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.audio_video_screens.VideosGridScreenVM;
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

@FxmlPath("/layouts/audio_video_screens/VideosGridScreen.fxml")
public class VideosGridScreenController implements FxmlView<VideosGridScreenVM>, Initializable {

    @FXML
    private StackPane searchPane;

    @FXML
    private Button backButton, mostPopularButton, newButton, highlyRatedButton;

    @FXML
    private ImageView searchIcon;

    @FXML
    private GridView<VideoItem> videosGrid;

    @InjectViewModel
    private VideosGridScreenVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFont();


        VideoItem videoItem = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        VideoItem videoItem1 = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        VideoItem videoItem2 = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        VideoItem videoItem3 = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        VideoItem videoItem4 = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");

        ObservableList<VideoItem> items = FXCollections.observableArrayList();
        items.addAll(videoItem, videoItem1, videoItem2, videoItem3, videoItem4);

        videosGrid.setCellFactory(new VideoGridCellFactory());
        videosGrid.setItems(items);

        backButton.setOnAction(event -> {
            ViewSwitcher.passData("videosButton");
            ViewSwitcher.showScreen(View.PQ_SCREEN);
        });
    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        searchIcon.setImage(new Image(getClass().getResource("/drawable/search_icon_green.png").toString()));

//        searchPane.setBackground(Background.EMPTY);
    }

    private void initializeFont() {
        mostPopularButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        newButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
        highlyRatedButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

    }

}
