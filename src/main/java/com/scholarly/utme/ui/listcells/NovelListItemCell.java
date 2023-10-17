package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.novels.Novel;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.Objects;

public class NovelListItemCell extends ListCell<NovelModel> {

    public VBox novelBox;
    public ImageView novelImage;
    public Label name;
    public Label chapters;

    public int selectedIndex = 0;

    public NovelListItemCell() {
        loadFxml();

        selectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                novelBox.setStyle("-fx-border-color: #12AF20; -fx-border-radius: 8;");
            }else {
                novelBox.setStyle(null);
            }
        }));
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/novel_list_item.fxml"));
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
