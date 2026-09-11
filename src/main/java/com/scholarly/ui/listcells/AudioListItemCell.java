package com.scholarly.ui.listcells;

import com.scholarly.data.model.listItems.AudioItem;
import com.scholarly.ui.utils.FontUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class AudioListItemCell extends ListCell<AudioItem> {
    public ImageView audioImage;

    public ImageView playIcon;
    public ImageView optionsIcon;
    public ImageView ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5;

    public Label videoTitle;
    public Label timeLabel;
    public Label audioRating;
    public Label videoDescription;

    public ImageView clockIcon;

    public AudioListItemCell() {
        loadFxml();

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/audio_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(AudioItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            audioImage.setImage(new Image(getClass().getResource("/drawable/audio_image_dummy.png").toString()));

            playIcon.setImage(new Image(getClass().getResource("/drawable/video_type_play_icon.png").toString()));
            optionsIcon.setImage(new Image(getClass().getResource("/drawable/video_item_options_icon.png").toString()));
            clockIcon.setImage(new Image(getClass().getResource("/drawable/clock_icon.png").toString()));

            videoTitle.setTextFill(Paint.valueOf("#053500"));
            videoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

            ObservableList<ImageView> starImages = FXCollections.observableArrayList(ratingStar1, ratingStar2, ratingStar3, ratingStar4, ratingStar5);
            for (int i = 0; i < item.getRating(); i++) {
                starImages.get(i).setImage(new Image(getClass().getResource("/drawable/landing_screen_images/rating_star.png").toString()));
            }
            audioRating.setText(String.valueOf(item.getRating()));

//            timeLabel.setText(String.valueOf(item.getTime()));
            timeLabel.setTextFill(Paint.valueOf("#12AF20"));

            videoDescription.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
