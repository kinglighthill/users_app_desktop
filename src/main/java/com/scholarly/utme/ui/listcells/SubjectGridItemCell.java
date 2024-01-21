package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.newDb.FavoriteSubject;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridCell;

import java.io.IOException;
import java.util.Objects;

public class SubjectGridItemCell extends GridCell<FavoriteSubject> {
    private static final String TAG = "SubjectGridItemCell: ";


    public Label subjectName;
    public ImageView subjectImage;
    public ImageView greenTickImage;

    public SubjectGridItemCell() {
        loadFxml();

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/subject_grid_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(FavoriteSubject item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            subjectName.setText(item.getTitle());
            subjectName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 13));
            System.out.println("GridItem shortTitle -> " + item.getShortTitle());
            subjectImage.setImage(new Image(Objects.requireNonNull(getClass().getResource("/drawable/subject_images/" + item.getShortTitle() + "_image.png")).toString()));

            updateGreenTick(item);

            setOnMouseClicked(event -> {
                item.setSelected(!item.isSelected());
                updateGreenTick(item);
            });


            setStyle("-fx-background-color: rgba(143, 152, 255, 0.10); -fx-background-radius: 7");
//            setBackground(new Background(new BackgroundFill(Paint.valueOf(item.getColorCode() != null ? item.getColorCode() : "12AF20"), new CornerRadii(3.0, 3.0, 3.0, 3.0, false), new Insets(3))));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }

    private void updateGreenTick(FavoriteSubject item) {
        if (item.isSelected()) {
            greenTickImage.setImage(new Image(getClass().getResource("/drawable/landing_screen_images/subject_green_tick_icon.png").toString()));
        } else {
            greenTickImage.setImage(null);
        }
    }

}
