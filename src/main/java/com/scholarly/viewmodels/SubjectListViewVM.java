package com.scholarly.viewmodels;

import com.scholarly.data.dao.SubjectDao;
import com.scholarly.data.model.newDb.FavoriteSubject;
import com.scholarly.data.model.newDb.PQSubject;
import com.scholarly.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SubjectListViewVM implements ViewModel, SceneLifecycle {
    private static final String TAG = "SubjectListViewVM: ";

    private final ObservableList<SubjectListItemVM> objectiveSubjects = FXCollections.observableArrayList();
    private final ObservableList<SubjectListItemVM> theorySubjects = FXCollections.observableArrayList();

    private final Map<Integer, SubjectState> objectiveHashMap = new LinkedHashMap<>();
    private final ObservableMap<Integer, SubjectState> selectedObjectiveSubjects = FXCollections.observableMap(objectiveHashMap); // LinkedHashMap because it maintains insertion order

    public ObservableMap<Integer, SubjectState> getSelectedObjectiveSubjects() {
        return selectedObjectiveSubjects;
    }

    private final Map<Integer, SubjectState> theoryHashMap = new LinkedHashMap<>();

    private final ObservableMap<Integer, SubjectState> selectedTheorySubjects = FXCollections.observableMap(theoryHashMap);

    public ObservableMap<Integer, SubjectState> getSelectedTheorySubjects() {
        return selectedTheorySubjects;
    }

    private final ObservableMap<Integer, Integer> selectedSubjectAllottedTime = FXCollections.observableHashMap();
    private final ObservableMap<Integer, Integer> selectedSubjectNumberOfQuestions = FXCollections.observableHashMap();

    public ObservableMap<Integer, Integer> getSelectedSubjectAllottedTime() {
        return selectedSubjectAllottedTime;
    }
    public ObservableMap<Integer, Integer> getSelectedSubjectNumberOfQuestions() {
        return selectedSubjectNumberOfQuestions;
    }

    private final CompositeDisposable disposables = new CompositeDisposable();

    private final SimpleBooleanProperty subjectLoaded = new SimpleBooleanProperty();

    public SubjectListViewVM() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Task<Boolean> subjectTask = new Task<>() {
            @Override
            protected Boolean call() {
                objectiveSubjects.addAll(SubjectDao.getObjFavoriteSubjects().stream().map(favoriteSubject -> new SubjectListItemVM(new PQSubject(favoriteSubject.getId(), favoriteSubject.getSubjectId(), favoriteSubject.getMinutesAllotted(), favoriteSubject.getOrder(), favoriteSubject.getTitle(), favoriteSubject.getShortTitle(), favoriteSubject.getDescription(), favoriteSubject.getColorCode(), favoriteSubject.isSelected()))).toList());

                theorySubjects.addAll(SubjectDao.getTheoryFavoriteSubjects().stream().map(favoriteSubject -> new SubjectListItemVM(new PQSubject(favoriteSubject.getId(), favoriteSubject.getSubjectId(), favoriteSubject.getMinutesAllotted(), favoriteSubject.getOrder(), favoriteSubject.getTitle(), favoriteSubject.getShortTitle(), favoriteSubject.getDescription(), favoriteSubject.getColorCode(), favoriteSubject.isSelected()))).toList());

                objectiveSubjects.forEach(vm -> disposables.add(
                        vm.getSubjectState()
                                .subscribeOn(Schedulers.io())
                                .observeOn(JavaFxScheduler.platform())
                                .subscribe(subjectState -> {
                                            if (subjectState.getSelected()) {
                                                System.out.println(TAG + "subject obj: " + subjectState.getSubject() + " selected.. adding to selected list in map with allotted time -> " + subjectState.getSubject().getMinutesAllotted());
                                                selectedObjectiveSubjects.put(subjectState.getSubject().getId(), subjectState);
                                                selectedSubjectAllottedTime.put(subjectState.getSubject().getId(), subjectState.getSubject().getMinutesAllotted());

                                                selectedSubjectNumberOfQuestions.put(subjectState.getSubject().getId(), subjectState.getNumberOfQuestions());

                                                System.out.println(TAG + "Selected Objective Subjects: (Key Set) -> " + selectedObjectiveSubjects.keySet());
                                            } else {
                                                selectedObjectiveSubjects.remove(subjectState.getSubject().getId());
                                                selectedSubjectAllottedTime.remove(subjectState.getSubject().getId());
                                            }
                                        })
                ));

                theorySubjects.forEach(vm -> disposables.add(
                        vm.getSubjectState()
                                .subscribeOn(Schedulers.io())
                                .observeOn(JavaFxScheduler.platform())
                                .subscribe(subjectState -> {
                                            if (subjectState.getSelected()) {
                                                System.out.println(TAG + "subject theory: " + subjectState.getSubject() + " selected.. adding to selected list in map");
                                                selectedTheorySubjects.put(subjectState.getSubject().getId(), subjectState);

                                                selectedSubjectAllottedTime.put(subjectState.getSubject().getId(), subjectState.getSubject().getMinutesAllotted());
                                                selectedSubjectNumberOfQuestions.put(subjectState.getSubject().getId(), subjectState.getNumberOfQuestions());

                                                System.out.println(TAG + "Selected Theory Subjects: (Key Set) -> " + selectedTheorySubjects.keySet());
                                            } else {
                                                selectedTheorySubjects.remove(subjectState.getSubject().getId());
                                                selectedSubjectAllottedTime.remove(subjectState.getSubject().getId());
                                            }
                                        })
                ));
                return true;
            }
        };
        subjectLoaded.bind(subjectTask.valueProperty());

        executorService.execute(subjectTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getSubjectLoaded() {
        return subjectLoaded;
    }

    /**
     * Invalidates all previous selections (called when the user changes selection between 'Practice', 'Past Question' or 'CBT Game')
     */
    public void invalidateSubjectStates() {
        objectiveSubjects.forEach(SubjectListItemVM::invalidate);
        theorySubjects.forEach(SubjectListItemVM::invalidate);
    }

    public void setSubjectSelected(PQSubject subject) {
        if (subject != null) {
            objectiveSubjects.forEach(vm -> {
                if (vm.getSubject().getId() == subject.getId()) {
                    vm.selectSubject(SubjectListItemVM.Type.OBJECTIVE);
                    vm.setSubject(subject);
                }
            });
        }
    }


    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
//        disposables.dispose();
    }

    public void dispose() {
        System.out.println(TAG + "SubjectListViewModel disposables disposed");
        disposables.dispose();
    }


    public ObservableList<SubjectListItemVM> getObjectiveSubjects() {
        return objectiveSubjects;
    }

    public ObservableList<SubjectListItemVM> getTheorySubjects() {
        return theorySubjects;
    }
}
