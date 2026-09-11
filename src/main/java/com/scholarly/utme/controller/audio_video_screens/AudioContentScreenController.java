package com.scholarly.utme.controller.audio_video_screens;

import com.scholarly.utme.data.model.listItems.AudioItem;
import com.scholarly.utme.ui.cellFactories.AudioListCellFactory;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.audio_video_screens.AudioContentScreenVM;
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

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/audio_video_screens/AudioContentScreen.fxml")
public class AudioContentScreenController implements FxmlView<AudioContentScreenVM>, Initializable {

    @FXML
    private VBox centerPane;

    @FXML
    private Button backButton, playButton;

    @FXML
    private ImageView bookmarkIcon, shareIcon, rateIcon;

    @FXML
    private ImageView equalizer, speaker;

    @FXML
    private ListView<AudioItem> audioList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        AudioItem audioItem = new AudioItem("dummyurl", "Central Nervous System", 45, 5, "dummyDescription");
        AudioItem audioItem1 = new AudioItem("dummyurl", "Central Nervous System", 45, 2, "dummyDescription");
        AudioItem audioItem2 = new AudioItem("dummyurl", "Central Nervous System", 45, 4, "dummyDescription");
        AudioItem audioItem3 = new AudioItem("dummyurl", "Central Nervous System", 45, 3, "dummyDescription");
        AudioItem audioItem4 = new AudioItem("dummyurl", "Central Nervous System", 45, 5, "dummyDescription");
        AudioItem audioItem5 = new AudioItem("dummyurl", "Central Nervous System", 45, 1, "dummyDescription");

        ObservableList<AudioItem> items = FXCollections.observableArrayList();
        items.addAll(audioItem, audioItem1, audioItem2, audioItem3, audioItem4, audioItem5);

        audioList.setCellFactory(new AudioListCellFactory());
        audioList.setItems(items);

        backButton.setOnAction(event -> {
            ViewSwitcher.showScreen(View.AUDIOS_GRID_SCREEN);
        });
    }

    private void initializeViews() {
        bookmarkIcon.setImage(new Image(getClass().getResource("/drawable/video_bookmark_icon.png").toString()));
        shareIcon.setImage(new Image(getClass().getResource("/drawable/video_share_icon.png").toString()));
        rateIcon.setImage(new Image(getClass().getResource("/drawable/video_star_icon.png").toString()));
        equalizer.setImage(new Image(getClass().getResource("/drawable/audio_type_equalizer_view_1x.png").toString()));
        speaker.setImage(new Image(getClass().getResource("/drawable/audio_speaker_icon.png").toString()));

        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/practice_back_button_icon.png").toString()));
        backButton.setGraphic(backIcon);

        ImageView playIcon = new ImageView(new Image(getClass().getResource("/drawable/audio_play_icon.png").toString()));
        playButton.setGraphic(playIcon);
        playButton.setBackground(Background.EMPTY);

        audioList.setBackground(Background.EMPTY);
    }
}
