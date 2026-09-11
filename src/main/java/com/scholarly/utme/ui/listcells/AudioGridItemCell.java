package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.AudioItem;
import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import org.controlsfx.control.GridCell;

import java.io.IOException;

public class AudioGridItemCell extends GridCell<AudioItem> {

    public ImageView audioImage;
    public ImageView playIcon;
    public ImageView optionsIcon;
    public Label videoTitle;
    public Label timeLabel;
    public Label videoRating;
    public Label videoDescription;

    public ImageView clockIcon;

    public AudioGridItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.AUDIO_CONTENT_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/audio_grid_item.fxml"));
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
            audioImage.setImage(new Image((getClass().getResource("/drawable/audio_image_dummy.png")).toString()));
            optionsIcon.setImage(new Image(getClass().getResource("/drawable/video_item_options_icon.png").toString()));

//            videoImage.setStyle("-fx-background-radius: 8; -fx-background-color: rgba(18, 18, 18, 0.5);");

            videoTitle.setText(item.getTitle());
            videoTitle.setTextFill(Paint.valueOf("#053500"));
            videoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

//            timeLabel.setText(String.valueOf(item.getTime()));
            timeLabel.setTextFill(Paint.valueOf("#12AF20"));

            clockIcon.setImage(new Image(getClass().getResource("/drawable/clock_icon.png").toString()));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
