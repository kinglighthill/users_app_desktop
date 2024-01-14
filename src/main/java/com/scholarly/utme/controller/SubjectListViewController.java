package com.scholarly.utme.controller;

import com.scholarly.utme.MainApplication;
import com.scholarly.utme.controller.practice_screens.CBTGameScreenController;
import com.scholarly.utme.controller.practice_screens.PracticeScreenController;
//import com.scholarly.utme.controller.practice_screens.StudyPastQuestScreenController;
import com.scholarly.utme.controller.practice_screens.StudyPastQuestScreenController;
import com.scholarly.utme.data.model.newDb.PQSubject;
import com.scholarly.utme.ui.utils.*;
import com.scholarly.utme.util.PreferencesManager;
import com.scholarly.utme.viewmodels.SubjectListItemVM;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import com.scholarly.utme.viewmodels.SubjectListViewVM;
import de.saxsys.mvvmfx.*;
import de.saxsys.mvvmfx.utils.viewlist.CachedViewModelCellFactory;
import de.saxsys.mvvmfx.utils.viewlist.ViewListCellFactory;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.scholarly.utme.util.Constants.*;

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
    private ProgressIndicator progressBar;
    @FXML
    private BorderPane subjectsPane;

    private SubjectListOption selectedOption;

    private final ObservableList<SubjectState> selectedObjectiveSubjects = FXCollections.observableArrayList();
    private final ObservableList<SubjectState> selectedTheorySubjects = FXCollections.observableArrayList();

    private final SimpleBooleanProperty isLoadingDone = new SimpleBooleanProperty(false);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showProgressBar();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        String lastSelectedTab = PreferencesManager.get(PREF_KEY_SELECTED_TAB, PREF_VALUE_OBJECTIVE_TAB);

        if (lastSelectedTab.equalsIgnoreCase(PREF_VALUE_OBJECTIVE_TAB)){
            tabMenu.getSelectionModel().select(objectiveTab);
        } else if (lastSelectedTab.equalsIgnoreCase(PREF_VALUE_THEORY_TAB)){
            tabMenu.getSelectionModel().select(theoryTab);
        }

        Task<ViewListCellFactory<SubjectListItemVM>> objectiveSubjectTask = new Task<>() {
            @Override
            protected ViewListCellFactory<SubjectListItemVM> call() {
                return CachedViewModelCellFactory.create(vm -> {
                    vm.setType(SubjectListItemVM.Type.OBJECTIVE);
                    vm.populateYearsList();
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
//                theoryList.setItems(viewModel.getTheorySubjects());
                executorService.execute(objectiveSubjectTask);
                executorService.execute(theorySubjectTask);
                executorService.shutdown();

                viewModel.getSelectedObjectiveSubjects().addListener((MapChangeListener<? super String, ? super SubjectState>) change -> {
                    selectedObjectiveSubjects.clear();
                    selectedObjectiveSubjects.addAll(viewModel.getSelectedObjectiveSubjects().values());
                });

                viewModel.getSelectedTheorySubjects().addListener((MapChangeListener<? super String, ? super SubjectState>) change -> {
                    selectedTheorySubjects.clear();
                    selectedTheorySubjects.addAll(viewModel.getSelectedTheorySubjects().values());

                });

                viewModel.getSelectedSubjectAllottedTime().addListener((MapChangeListener<? super String, ? super Integer>) change -> {
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

                questionOverviewTable.setItems(selectedObjectiveSubjects);

                subjectColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSubject().getTitle()));

                yearColumn.setCellValueFactory( param -> new SimpleStringProperty(param.getValue().getSelectedYear().getShortDescription()));

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
            objectiveList.setCellFactory(objectiveSubjectTask.valueProperty().getValue());
            objectiveList.setSelectionModel(new NoSelectionModel<>());
            objectiveList.setFocusTraversable(false);
            objectiveList.setItems(viewModel.getObjectiveSubjects());

            hideProgressBar();
        });

        initializeViews();
        initializeFonts();

//        ObservableList<SubjectListItemVM> mySubjects = viewModel.getObjectiveSubjects().stream().filter(SubjectListItemVM::isFavoriteSubject).collect(Collectors.toCollection(FXCollections::observableArrayList));
//        ObservableList<SubjectListItemVM> otherSubjects = viewModel.getObjectiveSubjects().stream().filter(subjectListItemVM -> !subjectListItemVM.isFavoriteSubject()).collect(Collectors.toCollection(FXCollections::observableArrayList));

//        if (mySubjects.isEmpty()) {
//            subjectListVBox.getChildren().removeAll(mySubjectsLabel, mySubjectsObjectiveList);
//        }
//        mySubjectsObjectiveList.setItems(mySubjects);
        objectiveList.setItems(viewModel.getObjectiveSubjects());
        theoryList.setItems(viewModel.getTheorySubjects());

        ViewListCellFactory<SubjectListItemVM> objectiveCellFactory = CachedViewModelCellFactory.create(vm -> {
            vm.setType(SubjectListItemVM.Type.OBJECTIVE);
            vm.populateYearsList();
            return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
        });

        ViewListCellFactory<SubjectListItemVM> theoryCellFactory = CachedViewModelCellFactory.create(vm -> {
            vm.setType(SubjectListItemVM.Type.THEORY);
            vm.populateYearsList();
            return FluentViewLoader.fxmlView(SubjectListItemController.class).viewModel(vm).load();
        });

//        mySubjectsObjectiveList.setCellFactory(objectiveCellFactory);
        objectiveList.setCellFactory(objectiveCellFactory);
        theoryList.setCellFactory(theoryCellFactory);

//        mySubjectsObjectiveList.setSelectionModel(new NoSelectionModel<>());
//        mySubjectsObjectiveList.setFocusTraversable(false);

        objectiveList.setSelectionModel(new NoSelectionModel<>());
        objectiveList.setFocusTraversable(false);

        theoryList.setSelectionModel(new NoSelectionModel<>());
        theoryList.setFocusTraversable(false);

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
//                    initialData = new StudyPastQuestScreenController2.InitialData(subjectStates);
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.STUDY_PAST_QUESTION_SCREEN);
                } else if (selectedOption == SubjectListOption.CBT_GAME) {
                     PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.CBT_GAME_SCREEN.getName());
                    initialData = new CBTGameScreenController.InitialData(subjectStates, false, false);
                    ViewSwitcher.passData(initialData);
                    ViewSwitcher.showScreen(View.CBT_GAME_SCREEN);
                }

                // Ensure that selected number of questions are greater than 0
                 if (!viewModel.getSelectedSubjectNumberOfQuestions().containsValue(0)) {

                     // Ensure time(hours and minutes) selected is greater than for CBT Practice
                     if (hoursChoiceBox.getValue() != 0 || minutesChoiceBox.getValue() != 0){

                         if (selectedOption == SubjectListOption.PRACTICE) {
                             PreferencesManager.put(PREF_KEY_LAST_SELECTED_PRACTICE, Screens.PRACTICE_SCREEN.getName());
                             initialData = new PracticeScreenController.InitialData(subjectStates, hoursChoiceBox.getValue(), minutesChoiceBox.getValue());
                             ViewSwitcher.passData(initialData);
                             ViewSwitcher. showScreen(View.PRACTICE_SCREEN);
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
                     List<String> subjects = viewModel.getSelectedSubjectNumberOfQuestions().entrySet().stream().filter(entry -> value.equals(entry.getValue())).map(Map.Entry::getKey).toList();

                     Alerts.info(
                             this.getClass(),
                             "Message",
                             null,
                             "Selected number of questions selected for Subject(s):" + subjects.toString().replace("[", "").replace("]", "") + " must be greater than 0"
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
            tabMenu.setTabMinWidth((Double) newValue);
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
                if (vm.getSubject().getSubjectId() == subject.getSubjectId()) {
                    objectiveList.scrollTo(vm);
                }
            });

            viewModel.setSubjectSelected(subject);
        }
    }

    public void showActivateDialog() {
        activateInvisibleButton.fire();
    }

    public enum SubjectListOption {
        PRACTICE,
        STUDY,
        CBT_GAME
    }

    public void dispose() {
        viewModel.invalidateSubjectStates();
        viewModel.dispose();
    }

    private void hideProgressBar() {
        subjectsPane.setVisible(true);
        progressBar.setVisible(false);
        isLoadingDone.setValue(true);
    }

    private void showProgressBar() {
        isLoadingDone.setValue(false);
        subjectsPane.setVisible(false);
        progressBar.setVisible(true);
    }
}
