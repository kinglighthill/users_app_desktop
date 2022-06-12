package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.VideoItem;
import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import org.controlsfx.control.GridCell;

import java.io.IOException;
import java.util.Objects;

public class VideoGridItemCell extends GridCell<VideoItem> {

    public ImageView videoImage;
    public ImageView playIcon;
    public Label videoTitle;
    public Label timeLabel;
    public Label videoRating;
    public Label videoDescription;

    public ImageView clockIcon;

    public VideoGridItemCell() {
        loadFxml();
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/video_grid_item.fxml"));
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
            videoImage.setImage(new Image((getClass().getResource("/drawable/video_image_dummy.png")).toString()));
            videoTitle.setText(item.getTitle());
            videoTitle.setTextFill(Paint.valueOf("#053500"));
            videoTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 14));

            timeLabel.setText(String.valueOf(item.getTime()));
            timeLabel.setTextFill(Paint.valueOf("#12AF20"));

            clockIcon.setImage(new Image(getClass().getResource("/drawable/clock_icon.png").toString()));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
