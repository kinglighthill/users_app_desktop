package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.Course;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class RecentlyViewedItemCell extends ListCell<Course> {


    public ImageView itemImage;

    public Button itemButton;

    private Course currentCourse;

    public RecentlyViewedItemCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/recently_viewed_item.fxml"));
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

        currentCourse = item;

        if(empty || item == null) {
            setText(null);
            setStyle("-fx-background-color: #259B24;");
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            itemButton.setText(item.getName());
            itemButton.setOnMouseClicked(event -> {
                System.out.println("Selected Course: " + currentCourse.getName());
                ViewSwitcher.passData(currentCourse.getName());
                ViewSwitcher.showScreen(View.HOME_SCREEN);
            });
            itemImage.setImage(new Image(item.getImageURL()));
            setStyle("-fx-background-color: #259B24;");

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
