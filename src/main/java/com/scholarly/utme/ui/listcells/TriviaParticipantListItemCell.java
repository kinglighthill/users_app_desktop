package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.TriviaChallengeItem;
import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.utils.FontUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class TriviaParticipantListItemCell extends ListCell<TriviaParticipantItem> {

    public ImageView playerImage;

    public Button statusButton;

    public Label statusText, participantName, numOfPoints;

    public TriviaParticipantListItemCell() {
        loadFxml();

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/trivia_participants_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(TriviaParticipantItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            playerImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
            Circle clip = new Circle(18, 18, 18);
            playerImage.setClip(clip);

            participantName.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 15));
            numOfPoints.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 14));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
