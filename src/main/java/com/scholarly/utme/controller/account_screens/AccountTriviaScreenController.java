package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.AccountTriviaScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountTriviaScreen.fxml")
public class AccountTriviaScreenController implements FxmlView<AccountTriviaScreenVM>, Initializable {

    @FXML
    private Button backButton;

    @FXML
    private ImageView profileImage;

    @FXML
    private TableView<TriviaParticipantItem> rankingTable;

    @FXML
    private TableColumn<TriviaParticipantItem, String> rankingColumn, nameColumn, ratingColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();
        initializeFonts();

        TriviaParticipantItem participant1 = new TriviaParticipantItem(1, "", "John Uzodinma", 500);
        TriviaParticipantItem participant2 = new TriviaParticipantItem(2, "", "Frank Mekwe", 435);
        TriviaParticipantItem participant3 = new TriviaParticipantItem(3, "", "Chinonso Umeh", 350);
        TriviaParticipantItem participant4 = new TriviaParticipantItem(4, "", "MaryRose Ekeh", 180);

        ObservableList<TriviaParticipantItem> participants = FXCollections.observableArrayList(participant1, participant2, participant3, participant4);

        rankingTable.setItems(participants);

        rankingColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty("# " + param.getValue().getRank());
        });
        rankingColumn.setStyle("-fx-alignment: CENTER;");

        nameColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getName());
        });
        nameColumn.setStyle("-fx-alignment: CENTER;");

        ratingColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getPoints() + " pts");
        });
        ratingColumn.setStyle("-fx-alignment: CENTER; -fx-text-fill: #12AF20;");


        backButton.setOnAction(event -> {
            ViewSwitcher.passData("accountScreen");
            ViewSwitcher.showScreen(View.LANDING_SCREEN);
        });

    }

    private void initializeViews() {
        Circle clip = new Circle(90, 90, 90);
        profileImage.setClip(clip);
        profileImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image2.png").toString()));
        backButton.setGraphic(new ImageView(new Image(getClass().getResource("/drawable/top_back_button.png").toString())));

        backButton.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {

    }
}
