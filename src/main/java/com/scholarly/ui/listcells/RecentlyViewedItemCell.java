package com.scholarly.ui.listcells;

import com.scholarly.data.model.Course;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class RecentlyViewedItemCell extends ListCell<Course> {


    public ImageView itemImage;


    public Button itemButton;

    public RecentlyViewedItemCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/recently_viewed_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Course item, boolean empty) {
        super.updateItem(item, empty);

        if(empty || item == null) {
            setText(null);
            setStyle("-fx-background-color: #259B24;");
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            itemButton.setText(item.getName());
            itemImage.setImage(new Image(item.getImageURL()));
            setStyle("-fx-background-color: #259B24;");

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
