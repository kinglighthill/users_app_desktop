package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.Topic;
import com.scholarly.utme.viewmodels.VideoAudioSubjectListItemVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/VideoAudioSubjectListItemView.fxml")
public class VideoAudioSubjectListItemController implements FxmlView<VideoAudioSubjectListItemVM>, Initializable {

    @FXML
    private StackPane subjectImageBackground;

    @FXML
    private CheckBox subjectCheckBox;

    @FXML
    private VBox subRoot;

    @FXML
    private ChoiceBox<Topic> topicsChoiceBox;

    @FXML
    private ChoiceBox<SubTopic> subtopicsChoiceBox;

    @FXML
    private Separator divider;

    @FXML
    private HBox optionPanel;

    @FXML
    private ImageView subjectImage;

    @FXML
    private Label subjectText;

    @InjectViewModel
    private VideoAudioSubjectListItemVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        subjectImageBackground.setStyle("-fx-background-radius: 8 0 0 8; -fx-background-color: " + viewModel.getSubjectColorName());
        try {
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/" + viewModel.getSubjectTableName() + "_image.png").toString()));
        }catch (Exception e){
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/irs_image.png").toString()));
            System.out.println(e.toString());
        }

        subjectText.textProperty().bind(viewModel.subjectNameProperty());
        topicsChoiceBox.setItems(viewModel.getTopics());
        subjectCheckBox.selectedProperty().bindBidirectional(viewModel.subjectSelectedProperty());


        subRoot.getChildren().removeAll(divider, optionPanel);
        viewModel.subjectSelectedProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue) {
                subRoot.getChildren().addAll(divider, optionPanel);
            }else {
                subRoot.getChildren().removeAll(divider, optionPanel);
            }
        }));
    }

    private void initializeViews() {
        topicsChoiceBox.setBackground(Background.EMPTY);
        subtopicsChoiceBox.setBackground(Background.EMPTY);
    }
}
