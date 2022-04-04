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
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

@FxmlPath("/layouts/SubjectListItemView.fxml")
public class SubjectListItemView implements FxmlView<SubjectListItemVM>, Initializable {

    @FXML
    public CheckBox subjectCheckBox, shuffleQuestionsCheckBox, shuffleOptionsCheckBox;

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


    @InjectViewModel
    private SubjectListItemVM viewModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        subjectCheckBox.textProperty().bind(viewModel.subjectNameProperty());
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
        yearChoiceBox.setValue(viewModel.getAvailableYears().get(0));



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
}
