package com.scholarly.utme.controller;

import com.scholarly.utme.data.model.Year;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
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

@FxmlPath("/layouts/SubjectListItemView.fxml")
public class SubjectListItemController implements FxmlView<SubjectListItemVM>, Initializable {

    @FXML
    private StackPane subjectImageBackground;

    @FXML
    private CheckBox subjectCheckBox, shuffleQuestionsCheckBox, shuffleOptionsCheckBox;

    @FXML
    private ChoiceBox<Year> yearChoiceBox;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();

//        subjectImage.imageProperty().bind();
        System.out.println("Subject " + viewModel.getSubjectTableName() + " color name -> " + viewModel.getSubjectColorName());
        subjectImageBackground.setStyle("-fx-background-radius: 8 0 0 8; -fx-background-color: " + viewModel.getSubjectColorName());
        try {
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/" + viewModel.getSubjectTableName() + "_image.png").toString()));
        }catch (Exception e){
            subjectImage.setImage(new Image(getClass().getResource("/drawable/select_subject_images/irs_image.png").toString()));
            System.out.println(e.toString());
        }
        subjectText.textProperty().bind(viewModel.subjectNameProperty());
       // subjectCheckBox.textProperty().bind(viewModel.subjectNameProperty());
        subjectCheckBox.selectedProperty().bindBidirectional(viewModel.subjectSelectedProperty());

        shuffleQuestionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleQuestionsProperty());
        shuffleOptionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleOptionsProperty());

        questionNoChoiceBox.setItems(viewModel.getQuestionNumbers());
        questionNoChoiceBox.getItems().addListener((ListChangeListener<Integer>) c -> {
            System.out.println("List was changed");
            if (c.getList().size() != 0) {
                questionNoChoiceBox.setValue(c.getList().get(c.getList().size() - 1));
            }
        });
        questionNoChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNumberOfQuestions(newValue);
        });

        yearChoiceBox.getItems().addAll(viewModel.getYears());
        yearChoiceBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {
            viewModel.loadQuestionNumbersList(newValue);
            viewModel.setSelectedYearProperty(newValue);
        });
        yearChoiceBox.setValue(viewModel.getYears().get(0));



        subRoot.getChildren().removeAll(divider, optionPanel);
        viewModel.subjectSelectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("subject selected property changed to -> " + newValue + " from -> " + oldValue);
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
    }
}
