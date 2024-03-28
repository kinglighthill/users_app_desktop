package com.scholarly.controller.audio_video_screens;

import com.scholarly.data.model.MediaSubTopic;
import com.scholarly.data.model.Topic;
import com.scholarly.viewmodels.audio_video_screens.AudioVideoSubjectListItemVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.collections.ListChangeListener;
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

@FxmlPath("/layouts/audio_video_screens/AudioVideoSubjectListItemView.fxml")
public class AudioVideoSubjectListItemController implements FxmlView<AudioVideoSubjectListItemVM>, Initializable {

    @FXML
    private StackPane subjectImageBackground;

    @FXML
    private CheckBox subjectCheckBox;

    @FXML
    private VBox subRoot;

    @FXML
    private ChoiceBox<Topic> topicsChoiceBox;

    @FXML
    private ChoiceBox<MediaSubTopic> subtopicsChoiceBox;

    @FXML
    private Separator divider;

    @FXML
    private HBox optionPanel;

    @FXML
    private ImageView subjectImage;

    @FXML
    private Label subjectText;

    @InjectViewModel
    private AudioVideoSubjectListItemVM viewModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeViews();

        subjectImageBackground.setStyle("-fx-background-radius: 8 0 0 8; -fx-background-color: " + viewModel.getSubjectColorName());
        try {
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/" + viewModel.getSubjectTableName() + "_image.png").toString()));
        }catch (Exception e){
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/IRS_image.png").toString()));
            System.out.println(e.toString());
        }

        subjectText.textProperty().bind(viewModel.subjectNameProperty());
        subjectCheckBox.selectedProperty().bindBidirectional(viewModel.subjectSelectedProperty());

        topicsChoiceBox.setItems(viewModel.getTopics());
        topicsChoiceBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldTopic, newTopic) -> {
            viewModel.loadSubtopics(newTopic);
            viewModel.setSelectedTopicProperty(newTopic);
        }));
        topicsChoiceBox.setValue(viewModel.getTopics().get(0));


        subtopicsChoiceBox.setItems(viewModel.getSubTopics());
        subtopicsChoiceBox.getItems().addListener((ListChangeListener<? super MediaSubTopic>) c -> {
            if (c.getList().size() != 0) {
                subtopicsChoiceBox.setValue(c.getList().get(0));
            }
        });
        subtopicsChoiceBox.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            viewModel.setSelectedSubtopicProperty(newValue);
        }));


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
