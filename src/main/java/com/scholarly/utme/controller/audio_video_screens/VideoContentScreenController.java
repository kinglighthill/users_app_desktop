package com.scholarly.utme.controller.audio_video_screens;

import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.cellFactories.VideoListCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.audio_video_screens.VideoContentScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/audio_video_screens/VideoContentScreen.fxml")
public class VideoContentScreenController implements FxmlView<VideoContentScreenVM>, Initializable {

    @FXML
    private VBox centerPane;

    @FXML
    private Button backButton, playButton;

    @FXML
    private ImageView bookmarkIcon, shareIcon, rateIcon;

    @FXML
    private MediaView mediaView;

    @FXML
    private ListView<VideoItem> videosList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        Media media = new Media(getClass().getResource("/assets/coding.mp4").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        VideoItem videoItem = new VideoItem("dummyurl", "Central Nervous System", 45, 5, "dummyDescription");
        VideoItem videoItem1 = new VideoItem("dummyurl", "Central Nervous System", 45, 3, "dummyDescription");
        VideoItem videoItem2 = new VideoItem("dummyurl", "Central Nervous System", 45, 5, "dummyDescription");
        VideoItem videoItem3 = new VideoItem("dummyurl", "Central Nervous System", 45, 2, "dummyDescription");
        VideoItem videoItem4 = new VideoItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        VideoItem videoItem5 = new VideoItem("dummyurl", "Central Nervous System", 45, 1, "dummyDescription");

        ObservableList<VideoItem> items = FXCollections.observableArrayList();
        items.addAll(videoItem, videoItem1, videoItem2, videoItem3, videoItem4, videoItem5);

        videosList.setCellFactory(new VideoListCellFactory());
        videosList.setItems(items);

        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.VIDEOS_GRID_SCREEN);
        });

    }

    private void initializeViews() {
        bookmarkIcon.setImage(new Image(getClass().getResource("/drawable/video_bookmark_icon.png").toString()));
        shareIcon.setImage(new Image(getClass().getResource("/drawable/video_share_icon.png").toString()));
        rateIcon.setImage(new Image(getClass().getResource("/drawable/video_star_icon.png").toString()));

        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
        playButton.setGraphic(playIcon);
        playButton.setBackground(Background.EMPTY);

        videosList.setBackground(Background.EMPTY);
    }
}
