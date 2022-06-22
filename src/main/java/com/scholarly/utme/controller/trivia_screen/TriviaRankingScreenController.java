package com.scholarly.utme.controller.trivia_screen;

import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.cellFactories.TriviaParticipantListCellFactory;
import com.scholarly.utme.ui.cellFactories.TriviaRankingGridCellFactory;
import com.scholarly.utme.viewmodels.trivia_screen.TriviaRankingScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import org.controlsfx.control.GridView;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/trivia_screen/TriviaRankingScreen.fxml")
public class TriviaRankingScreenController implements FxmlView<TriviaRankingScreenVM>, Initializable {

    @FXML
    private ListView<TriviaParticipantItem> rankingsList;

    @FXML
    private GridView<TriviaParticipantItem> rankingGrid;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        TriviaParticipantItem participant1 = new TriviaParticipantItem(1, "", "Uche Umeh", 300);
        TriviaParticipantItem participant2 = new TriviaParticipantItem(2, "", "John Uzo", 500);
        TriviaParticipantItem participant3 = new TriviaParticipantItem(3, "", "Kingsley Ugwu", 400);
        TriviaParticipantItem participant4 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant5 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant6 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant7 = new TriviaParticipantItem(0, "", "John Uzo", 500);
        TriviaParticipantItem participant8 = new TriviaParticipantItem(0, "", "Kingsley Ugwu", 400);
        TriviaParticipantItem participant9 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant10 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant11 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant12 = new TriviaParticipantItem(0, "", "John Uzo", 500);
        TriviaParticipantItem participant13 = new TriviaParticipantItem(0, "", "Kingsley Ugwu", 400);
        TriviaParticipantItem participant14 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        TriviaParticipantItem participant15 = new TriviaParticipantItem(0, "", "Uche Umeh", 300);
        ObservableList<TriviaParticipantItem> participants = FXCollections.observableArrayList(participant1, participant2, participant3, participant4, participant5, participant6, participant7, participant8, participant9, participant10, participant11, participant12, participant13, participant14, participant15);
        rankingsList.setItems(participants);
        rankingsList.setCellFactory(new TriviaParticipantListCellFactory());


        TriviaParticipantItem winner1 = new TriviaParticipantItem(1, "", "Uche Umeh", 500);
        TriviaParticipantItem winner2 = new TriviaParticipantItem(2, "", "John Uzo", 400);
        TriviaParticipantItem winner3 = new TriviaParticipantItem(3, "", "Kingsley Ugwu", 300);

        ObservableList<TriviaParticipantItem> winners = FXCollections.observableArrayList(winner1, winner2, winner3);
        rankingGrid.setItems(winners);
        rankingGrid.setCellFactory(new TriviaRankingGridCellFactory());

    }

    private void initializeViews() {
        backButton.setBackground(Background.EMPTY);
        rankingsList.setBackground(Background.EMPTY);
//        scrollPane.setBackground(Background.EMPTY);

        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/trivia_screen_images/back_button.png").toString())));
    }

    private void initializeFonts() {

    }
}
