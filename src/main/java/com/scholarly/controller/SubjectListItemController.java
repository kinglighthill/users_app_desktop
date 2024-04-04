package com.scholarly.controller;

import com.scholarly.data.model.Year;
import com.scholarly.data.model.newDb.PQTopic;
import com.scholarly.ui.utils.FontUtil;
import com.scholarly.util.Helper;
import com.scholarly.viewmodels.SubjectListItemVM;
import com.scholarly.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@FxmlPath("/layouts/SubjectListItemView.fxml")
public class SubjectListItemController implements FxmlView<SubjectListItemVM>, Initializable {
    private static final String TAG = "SubjectListItemController:  ";

    @InjectViewModel
    private SubjectListItemVM viewModel;


    @FXML
    private BorderPane subjectPane;
    @FXML
    private StackPane subjectImageBackground;
    @FXML
    public CheckBox subjectCheckBox, shuffleQuestionsCheckBox, shuffleOptionsCheckBox;
    @FXML
    public ChoiceBox<Year> yearChoiceBox;
    @FXML
    private CheckComboBox<PQTopic> topicsComboBox;
    @FXML
    private VBox subRoot, subjectVBox;
    @FXML
    private ChoiceBox<Integer> questionNoChoiceBox;
    @FXML
    private Separator divider;
    @FXML
    private HBox optionPanel;
    @FXML
    private ImageView subjectImage;
//    @FXML
//    private Label subjectText, subjectDesc;

    SubjectListViewController subjectListViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeViews();

        ViewTuple<SubjectListViewController, SubjectListViewVM> subjectListViewTuple = FluentViewLoader.fxmlView(SubjectListViewController.class).load();
        subjectListViewController = subjectListViewTuple.getCodeBehind();

//        ViewTuple<SubjectListViewControllerWaec, SubjectListViewWaecVM> subjectListViewWaecTuple = FluentViewLoader.fxmlView(SubjectListViewControllerWaec.class).load();
//        subjectListViewControllerWaec = subjectListViewWaecTuple.getCodeBehind();

        subjectImageBackground.setStyle("-fx-background-radius: 8 0 0 8; -fx-background-color: " + viewModel.getSubjectColorName());
        subjectImage.setImage(Helper.getSubjectIcon(getClass(), viewModel.getSubjectName()));

        Label subjectText = new Label();
        subjectText.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.BOLD, FontUtil.FontSize.EIGHTEEN.size));
        subjectText.textProperty().bind(viewModel.subjectNameProperty());
        subjectVBox.getChildren().add(subjectText);

        if (viewModel.getSubjectDesc() != null) {
            Label subjectDesc = new Label();
            subjectDesc.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.LIGHT, FontUtil.FontSize.TWELVE.size));
            subjectDesc.setText(viewModel.getSubjectDesc());
            subjectDesc.setPadding(new Insets(8, 0, 0, 0));
            subjectVBox.getChildren().add(subjectDesc);
        }

        subjectPane.setOnMouseClicked(event -> subjectCheckBox.setSelected(!subjectCheckBox.isSelected()));

        subjectCheckBox.selectedProperty().bindBidirectional(viewModel.subjectSelectedProperty());
        shuffleQuestionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleQuestionsProperty());
        shuffleOptionsCheckBox.selectedProperty().bindBidirectional(viewModel.shuffleOptionsProperty());

        questionNoChoiceBox.setItems(viewModel.getQuestionNumbers());
        questionNoChoiceBox.getItems().addListener((ListChangeListener<Integer>) changeList -> {
            if (changeList.getList().size() != 0) {
                questionNoChoiceBox.setValue(changeList.getList().get(changeList.getList().size() - 1));
            }
        });
        questionNoChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setSelectedNumberOfQuestions(newValue);
        });

        yearChoiceBox.getItems().addAll(viewModel.getYears());
        List<Year> freeYears = viewModel.getYears().stream().filter(Year::isFree).toList();

        if (!freeYears.isEmpty()) {
            viewModel.setSelectedYearProperty(freeYears.get(0));
        } else {
            viewModel.setSelectedYearProperty(viewModel.getYears().get(0));
        }

        yearChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.isFree()) {
                    viewModel.setSelectedYearProperty(newValue);
                    viewModel.loadTopicsForYear(newValue);
                    viewModel.loadQuestionNumbersList(newValue);
                } else {
                    yearChoiceBox.getSelectionModel().clearSelection();
                    if (!freeYears.isEmpty()) {
                        yearChoiceBox.getSelectionModel().select(freeYears.get(0));
                        subjectListViewController.showActivateDialog();
                    } else {
                        yearChoiceBox.getSelectionModel().select(oldValue);
                        if (viewModel.isSubjectSelected()) {
                            subjectListViewController.showActivateDialog();
                        }
                    }
                }
            }
        });

        if (!freeYears.isEmpty()) {
            yearChoiceBox.setValue(freeYears.get(0));
        } else {
            yearChoiceBox.setValue(viewModel.getYears().get(0));
        }

        topicsComboBox.getItems().addAll(viewModel.getTopics());
        topicsComboBox.getCheckModel().checkAll();
        List<Integer> topicsIdList = topicsComboBox.getCheckModel().getCheckedItems().stream().map(PQTopic::getId).collect(Collectors.toList());
        viewModel.setSelectedTopics(topicsIdList);

        viewModel.getTopics().addListener((ListChangeListener<? super PQTopic>) changedList -> {
            if (changedList.getList().size() != 0) {
                topicsComboBox.getItems().clear();
                topicsComboBox.getItems().addAll(changedList.getList());
                topicsComboBox.getCheckModel().checkAll();
            }
        });

//        topicsComboBox.getItems().addListener((ListChangeListener<? super PQTopic>) changedList -> {
//            System.out.println(TAG + "TopicsComboBox changedList -> " + changedList.getList());
//        });

        topicsComboBox.getCheckModel().getCheckedItems().addListener((ListChangeListener<PQTopic>) changeList -> {
            if (topicsComboBox.getCheckModel().getCheckedItems().size() == 0) {
                topicsComboBox.getCheckModel().check(0);
            }
            List<Integer> changedTopicsList = changeList.getList().stream().map(PQTopic::getId).collect(Collectors.toList());
            viewModel.loadQuestionNumbersList(changedTopicsList);
            viewModel.setSelectedTopics(changedTopicsList);
        });

        subRoot.getChildren().removeAll(divider, optionPanel);

        if (viewModel.isSubjectSelected()) {
            subRoot.getChildren().addAll(divider, optionPanel);
        }

        viewModel.subjectSelectedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(TAG + viewModel.getSubject() + " selected property changed to -> " + newValue + " from -> " + oldValue);
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
