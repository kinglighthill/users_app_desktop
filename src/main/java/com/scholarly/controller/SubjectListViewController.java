package com.scholarly.controller;

import com.scholarly.async.PQScreen;
import com.scholarly.controller.practice_screens.CBTGameScreenController;
import com.scholarly.controller.practice_screens.PracticeScreenController;
//import com.scholarly.controller.practice_screens.StudyPastQuestScreenController;
import com.scholarly.controller.practice_screens.StudyPastQuestScreenController;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.ui.utils.*;
import com.scholarly.util.AppProperties;
import com.scholarly.util.PreferencesManager;
import com.scholarly.viewmodels.SubjectListItemVM;
import com.scholarly.viewmodels.SubjectListItemVM.SubjectState;
import com.scholarly.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.*;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import de.saxsys.mvvmfx.utils.viewlist.ViewListCellFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import javafx.util.BuilderFactory;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.scholarly.util.Constants.*;

@FxmlPath("/layouts/SubjectListView.fxml")
public class SubjectListViewController implements FxmlView<SubjectListViewVM>, Initializable {
    private static final String TAG = "SubjectListViewController:  ";

    @InjectViewModel
    private SubjectListViewVM viewModel;

    @FXML
    private ListView<SubjectListItemVM> mySubjectsObjectiveList, objectiveList, theoryList;
    @FXML
    private Label subjectSelectedLabel, timeSettingsLabel, mySubjectsLabel, otherSubjectsLabel;
    @FXML
    private VBox hoursSelector, minutesSelector, activateNowDialog, dimmer, subjectListVBox;
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
    Button startButton, activateInvisibleButton;
    @FXML
    private BorderPane subjectsPane;

    private SubjectListOption selectedOption;

    private final ObservableList<SubjectState> selectedObjectiveSubjects = FXCollections.observableArrayList();
    private final ObservableList<SubjectState> selectedTheorySubjects = FXCollections.observableArrayList();

    private final SimpleBooleanProperty isLoadingDone = new SimpleBooleanProperty(false);

    private final boolean hasTheory = AppProperties.getInstance().isHasTheory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProgressBar();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        String lastSelectedTab = PreferencesManager.get(PREF_KEY_SELECTED_TAB, PREF_VALUE_OBJECTIVE_TAB);

        if (lastSelectedTab.equalsIgnoreCase(PREF_VALUE_OBJECTIVE_TAB)){
            tabMenu.getSelectionModel().select(objectiveTab);
        } else if (lastSelectedTab.equalsIgnoreCase(PREF_VALUE_THEORY_TAB) && hasTheory){
            tabMenu.getSelectionModel().select(theoryTab);
        }

        Task<ViewListCellFactory<SubjectListItemVM>> objectiveSubjectTask = new Task<>() {
            @Override
            protected ViewListCellFactory<SubjectListItemVM> call() {
                return CachedViewModelCellFactory.create(vm -> {
                    vm.setType(SubjectListItemVM.Type.OBJECTIVE);
                    vm.populateYearsList();
                    /*var f = FluentViewLoader.fxmlView(SubjectListItemController.class);
                    var v = f.viewModel(vm);
                    var l = v.load();
                    return l;*/
                    return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
                });
            }
        };

        Task<ViewListCellFactory<SubjectListItemVM>> theorySubjectTask = new Task<>() {
            @Override
            protected ViewListCellFactory<SubjectListItemVM> call() {
                return CachedViewModelCellFactory.create(vm -> {
                    vm.setType(SubjectListItemVM.Type.THEORY);
                    vm.populateYearsList();
                    return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
                });
            }
        };

        viewModel.getSubjectLoaded().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                executorService.execute(objectiveSubjectTask);
                executorService.execute(theorySubjectTask);
                executorService.shutdown();

                viewModel.getSelectedObjectiveSubjects().addListener((MapChangeListener<? super Integer, ? super SubjectState>) change -> {
                    selectedObjectiveSubjects.clear();
                    selectedObjectiveSubjects.addAll(viewModel.getSelectedObjectiveSubjects().values());
                    questionOverviewTable.setItems(selectedObjectiveSubjects);
                });

                viewModel.getSelectedTheorySubjects().addListener((MapChangeListener<? super Integer, ? super SubjectState>) change -> {
                    selectedTheorySubjects.clear();
                    selectedTheorySubjects.addAll(viewModel.getSelectedTheorySubjects().values());
                    questionOverviewTable.setItems(selectedTheorySubjects);
                });

                viewModel.getSelectedSubjectAllottedTime().addListener((MapChangeListener<? super Integer, ? super Integer>) change -> {
                    int totalTime = viewModel.getSelectedSubjectAllottedTime().values().stream().reduce(0, Integer::sum);
                    hoursChoiceBox.setValue(0);
                    minutesChoiceBox.setValue(totalTime);
                    if (totalTime >= 60) {
                        hoursChoiceBox.setValue(totalTime / 60);
                        minutesChoiceBox.setValue(totalTime % 60);
                    }
                });

                tabMenu.getSelectionModel().selectedItemProperty().addListener(((tabObservable, tabOldValue, tabNewValue) -> {
                    if (tabNewValue.getText().equalsIgnoreCase(PREF_VALUE_OBJECTIVE_TAB)) {
                        tabMenu.getSelectionModel().select(objectiveTab);
                        PreferencesManager.put(PREF_KEY_SELECTED_TAB, PREF_VALUE_OBJECTIVE_TAB);
                        Animations.animate(tabNewValue.getTabPane());
                        questionOverviewTable.setItems(selectedObjectiveSubjects);
                    } else {
                        tabMenu.getSelectionModel().select(theoryTab);
                        PreferencesManager.put(PREF_KEY_SELECTED_TAB, PREF_VALUE_THEORY_TAB);
                        Animations.animate(tabNewValue.getTabPane());
                        questionOverviewTable.setItems(selectedTheorySubjects);
                    }
                }));

                subjectColumn.setCellValueFactory(param -> {
                    PQSubject subject = param.getValue().getSubject();
                    String title = subject.getTitle();

                    if (subject.getDescription() != null) {
                        title += "\n" + subject.getDescription();
                    }

                    return new SimpleStringProperty(title);
                });

                yearColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSelectedYear().getShortDescription()));

                questionsColumn.setCellValueFactory( param -> {
                    if (param.getValue().getNumberOfQuestions() == null) {
                        return new SimpleStringProperty("0");
                    } else {
                        return new SimpleStringProperty(Integer.toString(param.getValue().getNumberOfQuestions()));
                    }
                });
            }
        });

        objectiveSubjectTask.setOnSucceeded(event -> {
            hideProgressBar();

            objectiveList.setCellFactory(objectiveSubjectTask.valueProperty().getValue());
            objectiveList.setSelectionModel(new NoSelectionModel<>());
            objectiveList.setFocusTraversable(false);
            objectiveList.setItems(viewModel.getObjectiveSubjects());
        });

        theorySubjectTask.setOnSucceeded(event -> {
            hideProgressBar();

            theoryList.setCellFactory(theorySubjectTask.valueProperty().getValue());
            theoryList.setSelectionModel(new NoSelectionModel<>());
            theoryList.setFocusTraversable(false);
            theoryList.setItems(viewModel.getTheorySubjects());
        });

        theoryTab.setDisable(!hasTheory);

        initializeViews();
        initializeFonts();

        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();

        for (int i = 0; i <= 12; i++) {
            hours.add(i);
        }

        for (int i = 0; i <= 60; i+=10) {
            minutes.add(i);
        }

        hoursChoiceBox.setItems(hours);
        hoursChoiceBox.setValue(0);
        minutesChoiceBox.setItems(minutes);
        minutesChoiceBox.setValue(0);

        activateInvisibleButton.setOnAction(event -> {
            dimmer.setVisible(true);
            Alerts.activateDialog(
                    this.getClass(),
                    "Activate",
                    null,
                    null
            ).show();
        });

        startButton.setOnAction(event -> {
            List<SubjectState> subjectStates;

            if (tabMenu.getSelectionModel().getSelectedItem() == objectiveTab) {
                subjectStates = selectedObjectiveSubjects;
            } else {
                subjectStates = selectedTheorySubjects;;
            }

            // Ensure at least a subject is selected before the start of practice, study or cbt game
            if (selectedObjectiveSubjects.size() > 0 || selectedTheorySubjects.size() > 0) {
                Object initialData;

                 if (selectedOption == SubjectListOption.STUDY) {
                     PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.PAST_QUESTION_SCREEN.getName());
                     initialData = new StudyPastQuestScreenController.InitialData(subjectStates);
                    PQScreen.logOut();
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.STUDY_PAST_QUESTION_SCREEN);
                } else if (selectedOption == SubjectListOption.CBT_GAME) {
                     PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.CBT_GAME_SCREEN.getName());
                    initialData = new CBTGameScreenController.InitialData(subjectStates, false, false);
                     PQScreen.logOut();
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.CBT_GAME_SCREEN);
                }

                // Ensure that selected number of questions are greater than 0
                 if (!viewModel.getSelectedSubjectNumberOfQuestions().containsValue(0)) {

                     // Ensure time(hours and minutes) selected is greater than for CBT Practice
                     if (hoursChoiceBox.getValue() != 0 || minutesChoiceBox.getValue() != 0) {
                         if (selectedOption == SubjectListOption.PRACTICE) {
                             PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.PRACTICE_SCREEN.getName());
                             initialData = new PracticeScreenController.InitialData(subjectStates, hoursChoiceBox.getValue(), minutesChoiceBox.getValue());
                             PQScreen.logOut();
                             ViewSwitcher.passData(initialData);
                             ViewSwitcher.showScreen(View.PRACTICE_SCREEN);
                         }
                     } else {
                         Alerts.info(
                                 this.getClass(),
                                 "Message",
                                 null,
                                 "Select a time greater than 0"
                         ).show();
                     }
                 } else {
                     Integer value = 0;
                     List<Integer> subjects = viewModel.getSelectedSubjectNumberOfQuestions().entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey).toList();

                     /*Alerts.info(
                             this.getClass(),
                             "Message",
                             null,
                             "Selected number of questions selected for Subject(s):" + subjects.toString().replace("[", "").replace("]", "") + " must be greater than 0"
                     ).show();*/

                     String message;
                     if (subjects.size() > 1) {
                         message = "Selected number of questions for selected subjects must be greater than 0";
                     } else {
                         message = "Selected number of questions for selected subject must be greater than 0";
                     }

                     Alerts.info(
                             this.getClass(),
                             "Message",
                             null,
                             message
                     ).show();
                 }
            } else {
                Alerts.info(
                        this.getClass(),
                        "Message",
                        null,
                        "Select at least one subject for practice"
                ).show();
            }
        });
    }

    private void initializeViews() {
        tabMenu.widthProperty().addListener(((observable, oldValue, newValue) -> {
            double width;

            if (hasTheory) {
                width = (Double) newValue / 2;
            } else  {
                width = (Double) newValue;
            }

            tabMenu.setTabMinWidth(width);
        }));
        tabMenu.setBackground(Background.EMPTY);
        hoursChoiceBox.setBackground(Background.EMPTY);
        minutesChoiceBox.setBackground(Background.EMPTY);
    }

    private void initializeFonts() {
//        startButton.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.MEDIUM, FontUtil.FontSize.FOURTEEN.size));
//        mySubjectsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
//        otherSubjectsLabel.setFont(FontUtil.getFont(FontUtil.GilroyFontFamily.SEMI_BOLD, 20));
    }

    public SimpleBooleanProperty getIsLoadingDone() {
        return isLoadingDone;
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

    public void setSelectedSubject(PQSubject subject) {
        if (subject != null) {
            viewModel.getObjectiveSubjects().forEach(vm -> {
                if (vm.getSubject().getId() == subject.getId()) {
                    objectiveList.scrollTo(vm);
                }
            });

            viewModel.setSubjectSelected(subject);
        }
    }

    public void showActivateDialog() {
        activateInvisibleButton.fire();
    }

//    public enum SubjectListOption {
//        PRACTICE,
//        STUDY,
//        CBT_GAME
//    }

    public void dispose() {
        viewModel.invalidateSubjectStates();
        viewModel.dispose();
    }

    private void hideProgressBar() {
        subjectsPane.setVisible(true);
        isLoadingDone.setValue(true);
    }

    private void showProgressBar() {
        isLoadingDone.setValue(false);
        subjectsPane.setVisible(false);
    }
}
