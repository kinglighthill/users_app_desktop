package com.scholarly.ui.listcells;

import com.scholarly.data.model.listItems.NotificationItem;
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

import java.io.IOException;

public class NotificationListItemCell extends ListCell<NotificationItem> {

    public ImageView notificationOptionsIcon;
    public Label notificationDate, notificationTitle, notificationText;

    public NotificationListItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            ViewSwitcher.showScreen(View.NOTIFICATION_DETAILS_SCREEN);
        });

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/notification_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(NotificationItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            initializeViews();
            initializeFonts();

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void initializeViews() {
        notificationOptionsIcon.setImage(new Image(getClass().getResource("/drawable/account_screen_images/notification_options_icon.png").toString()));
    }

    private void initializeFonts() {
        notificationDate.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
        notificationTitle.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
        notificationText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));
    }
}
