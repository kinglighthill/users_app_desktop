package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.HelloApplication;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;

import java.io.IOException;

public class AppListItemCell extends ListCell<AppItem> {
    private AppItem app;

    public ImageView appImage;
    public Label appName;

    HelloApplication application = new HelloApplication();

    public AppListItemCell() {
        loadFxml();

        setOnMouseClicked(event -> {
            application.openBrowser(app.getDownloadLink());
        });
    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/app_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(AppItem item, boolean empty) {
        super.updateItem(item, empty);
        app = item;

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            appName.setText(item.getName());
            appName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

            try {
                appImage.setImage(new Image(item.getImageUrl()));
            } catch (Exception e) {
                appImage.setImage(new Image(getClass().getResource("/drawable/app_screen_images/scholarly_logo.png").toString()));
                System.out.println(e.getMessage());
                System.out.println("App image not found, using default image (Scholarly logo)");
            }

//            setOnMouseClicked(event -> {
//                appName.setText(item.getName());
//                String url = item.getDownloadLink();
//                application.openBrowser(url);
//            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
