package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.data.model.newDb.PQTopic;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import io.reactivex.rxjava3.core.Observable;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/SubjectListItemView.fxml")
public class SubjectListItemController implements FxmlView<SubjectListItemVM>, Initializable {

    @FXML
    private StackPane subjectImageBackground;

    @FXML
    private CheckBox subjectCheckBox, shuffleQuestionsCheckBox, shuffleOptionsCheckBox;

    @FXML
    private ChoiceBox<Year> yearChoiceBox;

    @FXML
    private CheckComboBox<PQTopic> topicsComboBox;

    @FXML
    private VBox subRoot;

    @FXML
    private ChoiceBox<Integer> questionNoChoiceBox;

    @FXML
    private Separator divider;

    @FXML
    private HBox optionPanel;

    @FXML
    private ImageView subjectImage;

    @FXML
    private Label subjectText;


    @InjectViewModel
    private SubjectListItemVM viewModel;

    private static final String TAG = "SubjectListItemController:  ";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initializeViews();

//        System.out.println("Subject " + viewModel.getSubjectTableName() + " color name -> " + viewModel.getSubjectColorName());
        subjectImageBackground.setStyle("-fx-background-radius: 8 0 0 8; -fx-background-color: " + viewModel.getSubjectColorName());
        try {
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/" + viewModel.getShortTitle() + "_image.png").toString()));
        } catch (Exception e){
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/IRS_image.png").toString()));
            System.out.println(TAG + e.toString());
        }
        subjectText.textProperty().bind(viewModel.subjectNameProperty());
        subjectCheckBox.selectedProperty().bindBidirectional(viewModel.subjectSelectedProperty());

        shuffleQuestionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleQuestionsProperty());
        shuffleOptionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleOptionsProperty());

        questionNoChoiceBox.setItems(viewModel.getQuestionNumbers());
        questionNoChoiceBox.getItems().addListener((ListChangeListener<Integer>) changeList -> {
//            System.out.println(TAG + "List was changed");
            if (changeList.getList().size() != 0) {
                questionNoChoiceBox.setValue(changeList.getList().get(changeList.getList().size() - 1));
            }
        });
        questionNoChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNumberOfQuestions(newValue);
        });

        yearChoiceBox.getItems().addAll(viewModel.getYears());
        yearChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            viewModel.loadQuestionNumbersList(newValue);
            viewModel.setSelectedYearProperty(newValue);
        });
        yearChoiceBox.setValue(viewModel.getYears().get(0));


        topicsComboBox.getItems().addAll(viewModel.getTopics());
        topicsComboBox.getCheckModel().checkAll();
        topicsComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<PQTopic>) changeList -> {
            if (topicsComboBox.getCheckModel().getCheckedItems().size() == 0) {
                topicsComboBox.getCheckModel().check(0);
            }
            List<Integer> topicIdList = changeList.getList().stream().map(PQTopic::getId).collect(Collectors.toList());
            viewModel.loadQuestionNumbersList(topicIdList);
        });


        subRoot.getChildren().removeAll(divider, optionPanel);
        viewModel.subjectSelectedProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(TAG + "subject selected property changed to -> " + newValue + " from -> " + oldValue);
            if (newValue) {
                subRoot.getChildren().addAll(divider, optionPanel);
            } else {
                subRoot.getChildren().removeAll(divider, optionPanel);
            }
        });


    }

    private void initializeViews() {
        questionNoChoiceBox.setBackground(Background.EMPTY);
        yearChoiceBox.setBackground(Background.EMPTY);
        topicsComboBox.setBackground(Background.EMPTY);
    }
}
