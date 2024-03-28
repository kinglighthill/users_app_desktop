package com.scholarly.ui.listcells;

import com.scholarly.data.model.listItems.BookmarkItem;
import com.scholarly.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridCell;

import java.io.IOException;

public class BookmarkGridItemCell extends GridCell<BookmarkItem> {
    public ImageView bookmarkImage;
    public Label bookmarkTitle;
    public Label bookmarkedItem;
    public Label bookmarkCategory;

    public BookmarkGridItemCell() {
        loadFxml();
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/bookmark_grid_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(BookmarkItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            initializeFonts();
            bookmarkImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/" + item.getImageUrl() + ".png").toString()));
            bookmarkTitle.setText(item.getTitle());
            bookmarkedItem.setText(item.getItem());
            bookmarkCategory.setText(item.getCategory());

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void initializeFonts() {
        bookmarkTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        bookmarkedItem.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        bookmarkCategory.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }
}
