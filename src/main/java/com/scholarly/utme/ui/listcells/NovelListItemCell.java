package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.novelsDb.Novel;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class NovelListItemCell extends ListCell<Novel> {

    public ImageView novelImage;
    public Label name;
    public Label chapters;

    public NovelListItemCell() {
        loadFxml();
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/novel_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(Novel item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            novelImage.setImage(new Image(getClass().getResource("/drawable/novels_images/life_changer2.jpg").toString()));
            name.setText(item.getName());
            name.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
