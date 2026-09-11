package com.scholarly.ui.listcells;

import com.scholarly.controller.novel_screens.NovelChapterListController;
import com.scholarly.data.model.novels.NovelModel;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.Screens;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
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

public class NovelGridItemCell extends GridCell<NovelModel> {

    private NovelModel novelModel;

    public VBox novelBox;
    public ImageView novelImage;
    public Label name;
    public Label chapters;

    public NovelGridItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.passData(new NovelChapterListController.InitialData(novelModel, Screens.NOVELS_GRID_SCREEN));
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
    protected void updateItem(NovelModel item, boolean empty) {
        super.updateItem(item, empty);
        this.novelModel = item;

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            novelImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/drawable/novel_images/" + item.getNovel().getImagePath())).toString()));
            name.setText(item.getNovel().getName());
            name.setTextFill(Paint.valueOf("#000000"));
            name.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.TWELVE.size));

            chapters.setText(item.getChapterText());
            chapters.setTextFill(Paint.valueOf("#12AF20"));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
