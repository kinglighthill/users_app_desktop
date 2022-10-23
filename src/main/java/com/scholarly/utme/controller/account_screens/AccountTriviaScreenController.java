package com.scholarly.utme.controller.account_screens;

import com.scholarly.utme.controller.landing_screens.LandingScreenController;
import com.scholarly.utme.data.model.listItems.TriviaParticipantItem;
import com.scholarly.utme.ui.utils.Alerts;
import com.scholarly.utme.ui.utils.FontUtil;
import com.scholarly.utme.ui.utils.View;
import com.scholarly.utme.ui.utils.ViewSwitcher;
import com.scholarly.utme.viewmodels.account_screens.AccountTriviaScreenVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/account_screens/AccountTriviaScreen.fxml")
public class AccountTriviaScreenController implements FxmlView<AccountTriviaScreenVM>, Initializable {

    @FXML
    private VBox centerVBox;

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
        TriviaParticipantItem participant4 = new TriviaParticipantItem(4, "", "Kingsley Ugwudinso", 230);
        TriviaParticipantItem participant5 = new TriviaParticipantItem(5, "", "Steve Aka", 180);
        TriviaParticipantItem participant6 = new TriviaParticipantItem(6, "", "Stanley Umeh", 180);
        TriviaParticipantItem participant7 = new TriviaParticipantItem(7, "", "Evelyn Chukwuka", 180);
        TriviaParticipantItem participant8 = new TriviaParticipantItem(8, "", "Frank Chika", 180);
        TriviaParticipantItem participant9 = new TriviaParticipantItem(9, "", "MaryRose Ekeh", 180);

        ObservableList<TriviaParticipantItem> participants = FXCollections.observableArrayList(participant1, participant2, participant3, participant4, participant5, participant6);

        rankingTable.setItems(participants);
        centerVBox.heightProperty().addListener(((observableValue, oldValue, newValue) -> {
            rankingTable.setPrefHeight(newValue.doubleValue());
        }));

        rankingColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty("#" + param.getValue().getRank());
        });

        rankingColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TriviaParticipantItem, String> call(TableColumn<TriviaParticipantItem, String> triviaParticipantItemStringTableColumn) {
                TableCell<TriviaParticipantItem, String> cell = new TableCell<>();

                cell.itemProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        Label label = new Label(newValue);
                        label.setTextFill(Paint.valueOf("#FFFFFF"));
                        label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));
                        label.setPadding(new Insets(10));
                        label.setStyle("-fx-background-color: #12AF20;");

                        int[] leastRank = {Integer.MIN_VALUE};

                        cell.getTableView().getItems().forEach(item -> {
                            if (item.getRank() > leastRank[0]) {
                                leastRank[0] = item.getRank();
                            }
                        });

                        if (cell.getTableRow() != null) {
                            int currentRank = cell.getTableRow().getItem().getRank();

                            if (currentRank == leastRank[0]) {
                                label.setStyle("-fx-background-color: #FFA300;");
                            }
                        }

                        cell.setGraphic(label);

//                        cell.graphicProperty().bind(Bindings.when(cell.emptyProperty()).then((Node) null).otherwise(label));

                    }
                });
                return cell;
            }
        });


        nameColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getName());
        });

        nameColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TriviaParticipantItem, String> call(TableColumn<TriviaParticipantItem, String> triviaParticipantItemStringTableColumn) {
                TableCell<TriviaParticipantItem, String> cell = new TableCell<>();

                cell.itemProperty().addListener(((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        HBox hBox = new HBox();
                        hBox.setSpacing(15);
                        hBox.setPadding(new Insets(10, 10, 10, 20));
                        hBox.setAlignment(Pos.CENTER_LEFT);

                        ImageView profileImage = new ImageView();
                        profileImage.setFitHeight(40);
                        profileImage.setFitWidth(40);
                        profileImage.setPreserveRatio(true);
                        profileImage.setImage(new Image(getClass().getResource("/drawable/account_screen_images/profile_image2.png").toString()));
                        Circle clip = new Circle(20, 20, 20);
                        profileImage.setClip(clip);

                        Label label = new Label(newValue);
                        label.setTextFill(Paint.valueOf("#006B17"));
                        label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, 16));

                        hBox.getChildren().addAll(profileImage, label);

                        cell.setGraphic(hBox);

                    }
                }));
                return cell;
            }
        });


        ratingColumn.setCellValueFactory(param -> {
            return new SimpleStringProperty(param.getValue().getPoints() + " pts");
        });

        ratingColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<TriviaParticipantItem, String> call(TableColumn<TriviaParticipantItem, String> triviaParticipantItemStringTableColumn) {
                TableCell<TriviaParticipantItem, String> cell = new TableCell<>();

                cell.itemProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue != null) {
                        Label label = new Label(newValue);
                        label.setTextFill(Paint.valueOf("#12AF20"));
                        label.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 16));

                        cell.setGraphic(label);
                    }
                });
                return cell;
            }
        });

        backButton.setOnAction(event -> {
            ViewSwitcher.passData(new LandingScreenController.InitialData("accountScreen"));
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
