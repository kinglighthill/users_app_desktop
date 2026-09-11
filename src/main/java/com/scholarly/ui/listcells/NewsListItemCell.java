package com.scholarly.ui.listcells;

import com.scholarly.data.model.listItems.NewsItem;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.ui.utils.View;
import com.scholarly.ui.utils.ViewSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class NewsListItemCell extends ListCell<NewsItem> {
    public ImageView newsImage;
    public Label newsTitle;
    public Label newsDate;
    public Label newsDetails;

    public NewsListItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.UPDATE_SCREEN);
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/news_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(NewsItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            newsImage.setImage(new Image(getClass().getResource("/drawable/update_item_image.png").toString()));
            Rectangle clip = new Rectangle(newsImage.getFitWidth(), newsImage.getFitHeight());
            clip.setArcHeight(60);
            clip.setArcHeight(60);
            newsImage.setClip(clip);

            newsTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 13));
            newsDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));
            newsDetails.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 12));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
