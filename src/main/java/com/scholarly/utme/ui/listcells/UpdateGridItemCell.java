package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.data.model.listItems.UpdateItem;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.GridCell;

import java.io.IOException;

public class UpdateGridItemCell extends GridCell<UpdateItem> {

    public ImageView updateImage;
    public Label updateTitle;
    public Label updateDate;
    public Label updateDetails;

    public UpdateGridItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.UPDATE_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/update_grid_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(UpdateItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            updateImage.setImage(new Image(getClass().getResource("/drawable/update_item_image.png").toString()));
            Rectangle clip = new Rectangle(updateImage.getFitWidth(), updateImage.getFitHeight());
            clip.setArcHeight(60);
            clip.setArcHeight(60);
            updateImage.setClip(clip);

            updateTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
            updateDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
            updateDetails.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

}
