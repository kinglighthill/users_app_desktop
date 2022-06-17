package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.data.model.listItems.TriviaChallengeItem;
import com.scholarly.utme.ui.cellFactories.TriviaChallengeListCellFactory;
import com.scholarly.utme.viewmodels.trivia_screen.TriviaChallengeScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaChallengeScreen.fxml")
public class TriviaChallengeScreenController implements FxmlView<TriviaChallengeScreenVM>, Initializable {

    @FXML
    private TextField searchChallenge;

    @FXML
    private Button backButton;

    @FXML
    private ListView<TriviaChallengeItem> challengeList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        TriviaChallengeItem challenge1 = new TriviaChallengeItem("", "", "", "", "", "", "", 1);
        TriviaChallengeItem challenge2 = new TriviaChallengeItem("", "", "", "", "", "", "", 0);
        TriviaChallengeItem challenge3 = new TriviaChallengeItem("", "", "", "", "", "", "", 2);
        TriviaChallengeItem challenge4 = new TriviaChallengeItem("", "", "", "", "", "", "", 1);

        ObservableList<TriviaChallengeItem> items = FXCollections.observableArrayList(challenge1, challenge2, challenge3, challenge4);
        challengeList.setCellFactory(new TriviaChallengeListCellFactory());
        challengeList.setItems(items);

    }

    private void initializeViews() {
        ImageView backIcon = new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_icon.png").toString()));
        backButton.setGraphic(backIcon);
        Circle clip = new Circle(18, 18, 18);
        backButton.setClip(clip);

        challengeList.setBackground(Background.EMPTY);

    }

    private void initializeFonts() {

    }
}
