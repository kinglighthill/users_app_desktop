package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.OrderedListItem;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.io.IOException;

public class NovelChapterListItemCell extends ListCell<NovelChapter> {

    public Panel panel;
    public Label chapterIndex;
    public Label chapterTitle;
    public ImageView padlockIcon;

    public NovelChapterListItemCell() {
        loadFxml();

        selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                panel.setStyle("-fx-background-color: #12AF20; -fx-background-radius: 5;");
                chapterIndex.setTextFill(Paint.valueOf("#FFFFFF"));
                chapterTitle.setTextFill(Paint.valueOf("#FFFFFF"));
            } else {
                panel.setStyle("-fx-background-color: #F1F1F1; -fx-background-radius: 5;");
                chapterIndex.setTextFill(Paint.valueOf("#000000"));
                chapterTitle.setTextFill(Paint.valueOf("#000000"));
            }
        }));
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/novel_chapter_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(NovelChapter item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            if (item.getPosition() >= 1) {
                chapterIndex.setText("Chapter " + item.getPosition() + ":  ");
            } else {
                chapterIndex.setText(null);
            }

            chapterTitle.setText(item.getTitle());

            padlockIcon.setImage(new Image(getClass().getResource("/drawable/novel_images/padlock_icon.png").toString()));
            chapterIndex.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
            chapterTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
