package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.data.model.listItems.AppItem;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridCell;

import java.io.IOException;

public class AppGridItemCell extends GridCell<AppItem> {
    public AppItem appItem;

    public ImageView appImage;
    public Label appName;

    MainApplication application = new MainApplication();

    public AppGridItemCell() {
        loadFxml();

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/app_grid_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(AppItem item, boolean empty) {
        super.updateItem(item, empty);
        appItem = item;

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {

            try {
                appImage.setImage(new Image(item.getImageUrl()));
            } catch (Exception e) {
                appImage.setImage(new Image(getClass().getResource("/drawable/app_screen_images/scholarly_logo.png").toString()));
                System.out.println(e.getMessage());
                System.out.println("App image not found, using default image (Scholarly logo)");
            }

            appName.setText(item.getName());
            appName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

            setOnMouseClicked(event -> {
                application.openBrowser(item.getDownloadLink());
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
