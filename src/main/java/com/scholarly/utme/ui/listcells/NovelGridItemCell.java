package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.controlsfx.control.GridCell;

import java.io.IOException;
import java.util.Objects;

public class NovelGridItemCell extends GridCell<Novel> {

    private Novel novel;

    public VBox novelBox;
    public ImageView novelImage;
    public Label name;
    public Label chapters;

    public NovelGridItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.passData(novel);
            ViewSwitcher.showScreen(View.NOVEL_CHAPTER_LIST_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/novel_grid_item.fxml"));
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
        this.novel = item;

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            novelImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/drawable/novel_images/" + item.getImagePath())).toString()));
            name.setText(item.getName());
            name.setTextFill(Paint.valueOf("#000000"));
            name.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

            chapters.setText(item.getChaptersCount() + " chapters");
            chapters.setTextFill(Paint.valueOf("#12AF20"));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
