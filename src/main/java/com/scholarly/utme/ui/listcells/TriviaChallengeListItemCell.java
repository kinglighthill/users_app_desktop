package com.scholarly.utme.ui.listcells;

import com.scholarly.utme.data.model.listItems.TriviaChallengeItem;
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

public class TriviaChallengeListItemCell extends ListCell<TriviaChallengeItem> {

    public ImageView challengeImage;
    public ImageView playerImage;

    public Button statusButton;

    public Label statusText;


    private static final String BUTTON_STYLE_GREEN = "-fx-background-color: #12AF20; -fx-background-radius: 10;";
    private static final String BUTTON_STYLE_RED = "-fx-background-color: #FA0000; -fx-background-radius: 10;";
    private static final String BUTTON_STYLE_YELLOW = "-fx-background-color: #EE8700; -fx-background-radius: 10;";

    public TriviaChallengeListItemCell() {
        loadFxml();

    }

    private void loadFxml() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/list_items/trivia_challenge_list_item.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void updateItem(TriviaChallengeItem item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setBackground(Background.EMPTY);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }else {
            challengeImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_icon.png").toString()));
            playerImage.setImage(new Image(getClass().getResource("/drawable/trivia_screen_images/prev_challenge_player_image.png").toString()));
            Circle clip = new Circle(15, 15, 15);
            playerImage.setClip(clip);

            if (item.getChallengeStatus() == 0) {
                statusButton.setText("Ended");
                statusButton.setStyle(BUTTON_STYLE_RED);
            }else if (item.getChallengeStatus() == 1) {
                statusButton.setText("New");
                statusButton.setStyle(BUTTON_STYLE_GREEN);
            } else if (item.getChallengeStatus() == 2) {
                statusButton.setText("Upcoming");
                statusText.setVisible(true);
                statusButton.setStyle(BUTTON_STYLE_YELLOW);
            }

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
    }
}
