package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class VideoListItemCell extends ListCell<VideoItem> {

    public MediaView videoMedia;

    public ImageView playIcon;
    public ImageView optionsIcon;
    public ImageView ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5;

    public Label videoTitle;
    public Label timeLabel;
    public Label videoRating;
    public Label videoDescription;

    public ImageView clockIcon;

    public VideoListItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.VIDEO_CONTENT_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/video_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(VideoItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            Media media = new Media(getClass().getResource("/assets/coding.mp4").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            videoMedia.setMediaPlayer(mediaPlayer);

            playIcon.setImage(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
            optionsIcon.setImage(new Image(getClass().getResource("/drawable/video_item_options_icon.png").toString()));
            clockIcon.setImage(new Image(getClass().getResource("/drawable/clock_icon.png").toString()));

            videoTitle.setTextFill(Paint.valueOf("#053500"));
            videoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

            ObservableList<ImageView> imageViews = FXCollections.observableArrayList(ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5);
            for (int i = 0; i < item.getRating(); i++) {
                imageViews.get(i).setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
            }
            videoRating.setText(String.valueOf(item.getRating()));

//            timeLabel.setText(String.valueOf(item.getTime()));
            timeLabel.setTextFill(Paint.valueOf("#12AF20"));

            videoDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
