package com.scholarly.utme.controller;

import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.*;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import de.saxsys.mvvmfx.utils.viewlist.ViewListCellFactory;
import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

@FxmlPath("/layouts/SubjectListView.fxml")
public class SubjectListViewController implements FxmlView<SubjectListViewVM>, Initializable {


    @InjectViewModel
    private SubjectListViewVM viewModel;

    @FXML
    private ListView<SubjectListItemVM> objectiveList, theoryList;

    @FXML
    private Label timeSettingsLabel;

    @FXML
    VBox hoursSelector, minutesSelector;

    @FXML
    ChoiceBox<Integer> hoursChoiceBox, minutesChoiceBox;

    @FXML
    TabPane tabMenu;

    @FXML
    Tab objectiveTab, theoryTab;

    @FXML
    private TableView<SubjectState> questionOverviewTable;

    @FXML
    private TableColumn<SubjectState, String> subjectColumn, yearColumn, questionsColumn;

    @FXML
    private Button startButton;

    private SubjectListOption selectedOption;


    private ObservableList<SubjectState> selectedObjectiveSubjects = FXCollections.observableArrayList();
    private ObservableList<SubjectState> selectedTheorySubjects = FXCollections.observableArrayList();


    private final String PREF_KEY_SELECTED_TAB = "SELECTED_TAB";
    private final String SELECTED_TAB_STYLE = "-fx-background-color: #12AF20;";
    private final String UNSELECTED_TAB_STYLE = "-fx-background-color: #FFFFFF;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences tabPreferences = AppPreferences.getPreferences();
        String lastSelectedTab = tabPreferences.get(PREF_KEY_SELECTED_TAB, "Objective");

        if (lastSelectedTab.equalsIgnoreCase("Objective")){
            tabMenu.getSelectionModel().select(objectiveTab);
            objectiveTab.setStyle(SELECTED_TAB_STYLE);
            theoryTab.setStyle(UNSELECTED_TAB_STYLE);
        }else if (lastSelectedTab.equalsIgnoreCase("Theory")){
            tabMenu.getSelectionModel().select(theoryTab);
            theoryTab.setStyle(SELECTED_TAB_STYLE);
            objectiveTab.setStyle(UNSELECTED_TAB_STYLE);
        }

        initializeViews();

        initializeFonts();

        objectiveList.setItems(viewModel.getObjectiveSubjects());
        theoryList.setItems(viewModel.getTheorySubjects());

        ViewListCellFactory<SubjectListItemVM> objectiveCellFactory = CachedViewModelCellFactory.create(vm -> {
            vm.setType(SubjectListItemVM.Type.OBJECTIVE);
            return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
        });

        ViewListCellFactory<SubjectListItemVM> theoryCellFactory = CachedViewModelCellFactory.create(vm -> {
            vm.setType(SubjectListItemVM.Type.THEORY);
            return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
        });

        objectiveList.setCellFactory(objectiveCellFactory);
        theoryList.setCellFactory(theoryCellFactory);

        objectiveList.setSelectionModel(new NoSelectionModel<>());
        objectiveList.setFocusTraversable(false);

        theoryList.setSelectionModel(new NoSelectionModel<>());
        theoryList.setFocusTraversable(false);

        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();

        for (int i = 0; i <= 12; i++) {
            hours.add(i);
        }

        for (int i = 0; i <= 60; i++) {
            minutes.add(i);
        }

        hoursChoiceBox.setItems(hours);
        hoursChoiceBox.setValue(2);

        minutesChoiceBox.setItems(minutes);
        minutesChoiceBox.setValue(40);

        viewModel.getSelectedObjectiveSubjects().addListener((MapChangeListener<? super String, ? super SubjectState>) change -> {
            selectedObjectiveSubjects.clear();
            selectedObjectiveSubjects.addAll(viewModel.getSelectedObjectiveSubjects().values());
           // System.out.println("selectedObjectiveSubjects -> " + viewModel.getSelectedObjectiveSubjects().values().toString());
        });

        viewModel.getSelectedTheorySubjects().addListener((MapChangeListener<? super String, ? super SubjectState>) change -> {
            selectedTheorySubjects.clear();
            selectedTheorySubjects.addAll(viewModel.getSelectedTheorySubjects().values());
        });

        tabMenu.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.getText().equalsIgnoreCase("Objective")) {
                tabMenu.getSelectionModel().select(objectiveTab);
                objectiveTab.setStyle(SELECTED_TAB_STYLE);
                theoryTab.setStyle(UNSELECTED_TAB_STYLE);
                tabPreferences.put(PREF_KEY_SELECTED_TAB, "Objective");
                animate(newValue.getTabPane());
                questionOverviewTable.setItems(selectedObjectiveSubjects);
            } else {
                tabMenu.getSelectionModel().select(theoryTab);
                theoryTab.setStyle(SELECTED_TAB_STYLE);
                objectiveTab.setStyle(UNSELECTED_TAB_STYLE);
                tabPreferences.put(PREF_KEY_SELECTED_TAB, "Theory");
                animate(newValue.getTabPane());
                questionOverviewTable.setItems(selectedTheorySubjects);
            }
        }));

        questionOverviewTable.setItems(selectedObjectiveSubjects);


        subjectColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(param.getValue().getSubject().getSubjectName());
        });

        yearColumn.setCellValueFactory( param -> {
            return new SimpleStringProperty(param.getValue().getSelectedYear().getShortDescription());
        });

        questionsColumn.setCellValueFactory( param -> {
            if (param.getValue().getNumberOfQuestions() == null) {
                return new SimpleStringProperty("0");
            } else {
                return new SimpleStringProperty(Integer.toString(param.getValue().getNumberOfQuestions()));
            }
        });



        startButton.setOnAction(event -> {
            List<SubjectState> subjectStates;

            if (tabMenu.getSelectionModel().getSelectedItem() == objectiveTab) {
                subjectStates = selectedObjectiveSubjects;
                //userPreferences.put(SELECTED_TAB_PREF_KEY, "Objective");

            } else {
                subjectStates = selectedTheorySubjects;
                //userPreferences.put(SELECTED_TAB_PREF_KEY, "Theory");

            }

            // Ensure at least a subject is selected before the start of practice, study or cbt game
            if (selectedObjectiveSubjects.size() > 0 || selectedTheorySubjects.size() > 0) {

                Object initialData = null;

                 if (selectedOption == SubjectListOption.STUDY) {
                    initialData = new StudyPastQuestScreenController.InitialData(subjectStates);
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.STUDY_PAST_QUESTION_SCREEN);
                } else if (selectedOption == SubjectListOption.CBT_GAME) {
                    initialData = new CBTGameScreenController.InitialData(subjectStates, false, false);
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.CBT_GAME_SCREEN);
                }

                // Ensure time(hours and minutes) selected is greater than for CBT Practice
                if (!hoursChoiceBox.getSelectionModel().isSelected(0) || !minutesChoiceBox.getSelectionModel().isSelected(0)){

                    if (selectedOption == SubjectListOption.PRACTICE) {
                        initialData = new PracticeScreenController.InitialData(subjectStates, hoursChoiceBox.getValue(), minutesChoiceBox.getValue());
                        ViewSwitcher.passData(initialData);
                        ViewSwitcher.showScreen(View.PRACTICE_SCREEN);
                    }

                }else {
                    Alerts.info(
                            this.getClass(),
                                    "Message",
                                    null,
                                    "Select a time greater than 0"
                            ).show();
                }


            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText(null);
                alert.setContentText("Select at least one subject for practice");
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(this.getClass().getResource("/drawable/app_logo.png").toString()));
                alert.showAndWait();

               // System.out.println("Please select at least one subject");
            }

        });

    }

    private void initializeViews() {
        tabMenu.widthProperty().addListener(((observable, oldValue, newValue) -> {
            tabMenu.setTabMinWidth((Double) newValue/2.1);
        }));
        tabMenu.setBackground(Background.EMPTY);
        hoursChoiceBox.setBackground(Background.EMPTY);
        minutesChoiceBox.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
//        startButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
    }

    /**
     * Plays a FadeTransition showing screen change when changing menu options
     * @param node on which the transition is played
     */
    public void animate(Node node){
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), node);

        fadeTransition.setFromValue(0.1);
        fadeTransition.setToValue(1.0);

        fadeTransition.play();
    }

    public void setOption(SubjectListOption option) {
        if (option == SubjectListOption.PRACTICE) {
            selectedOption = SubjectListOption.PRACTICE;
            viewModel.invalidateSubjectStates();
            timeSettingsLabel.setVisible(true);
            hoursSelector.setVisible(true);
            minutesSelector.setVisible(true);
        } else if (option == SubjectListOption.STUDY) {
            selectedOption = SubjectListOption.STUDY;
            viewModel.invalidateSubjectStates();
            timeSettingsLabel.setVisible(false);
            hoursSelector.setVisible(false);
            minutesSelector.setVisible(false);
        } else if (option == SubjectListOption.CBT_GAME) {
            selectedOption = SubjectListOption.CBT_GAME;
            viewModel.invalidateSubjectStates();
            timeSettingsLabel.setVisible(false);
            hoursSelector.setVisible(false);
            minutesSelector.setVisible(false);
        }
    }

    public enum SubjectListOption {
        PRACTICE,
        STUDY,
        CBT_GAME
    }
}
