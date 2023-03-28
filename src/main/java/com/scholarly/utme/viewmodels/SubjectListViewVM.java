package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.viewmodels.SubjectListItemVM.SubjectState;
import de.saxsys.mvvmfx.SceneLifecycle;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.pdfsam.rxjavafx.schedulers.JavaFxScheduler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class SubjectListViewVM implements ViewModel, SceneLifecycle {
    private static final String TAG = "SubjectListViewVM:  ";

    private ObservableList<SubjectListItemVM> objectiveSubjects = FXCollections.observableArrayList();
    private ObservableList<SubjectListItemVM> theorySubjects = FXCollections.observableArrayList();

    private Map<String, SubjectState> objectiveHashMap = new LinkedHashMap<>();
    private ObservableMap<String, SubjectState> selectedObjectiveSubjects = FXCollections.observableMap(objectiveHashMap); // LinkedHashMap because it maintains insertion order

    public ObservableMap<String, SubjectState> getSelectedObjectiveSubjects() {
        return selectedObjectiveSubjects;
    }

    private Map<String, SubjectState> theoryHashMap = new LinkedHashMap<>();

    private ObservableMap<String, SubjectState> selectedTheorySubjects = FXCollections.observableMap(theoryHashMap);

    public ObservableMap<String, SubjectState> getSelectedTheorySubjects() {
        return selectedTheorySubjects;
    }

    private ObservableMap<String, Integer> selectedSubjectAllottedTime = FXCollections.observableHashMap();

    public ObservableMap<String, Integer> getSelectedSubjectAllottedTime() {
        return selectedSubjectAllottedTime;
    }

    private CompositeDisposable disposables = new CompositeDisposable();


    public SubjectListViewVM() {
        objectiveSubjects.addAll(SubjectDao.getPQSubjects().stream().map(SubjectListItemVM::new).collect(Collectors.toList()));

        theorySubjects.addAll(SubjectDao.getPQSubjects().stream().map(SubjectListItemVM::new).collect(Collectors.toList()));

        objectiveSubjects.forEach(vm -> {
            disposables.add(
                    vm.getSubjectState()
                            .subscribeOn(Schedulers.io())
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(
                                    subjectState -> {
                                        if (subjectState.getSelected()) {
                                            System.out.println(TAG + "subject obj: " + subjectState.getSubject() + " selected.. adding to selected list in map with allotted time -> " + subjectState.getSubject().getMinutesAllotted());
                                            selectedObjectiveSubjects.put(subjectState.getSubject().getShortTitle(), subjectState);
                                            selectedSubjectAllottedTime.put(subjectState.getSubject().getShortTitle(), subjectState.getSubject().getMinutesAllotted());

                                            System.out.println(TAG + "Selected Objective Subjects: (Key Set) -> " + selectedObjectiveSubjects.keySet());
                                            System.out.println(TAG + "Selected Objective Subjects Time Map: (Key Set) -> " + selectedSubjectAllottedTime.keySet() + " with values -> " + selectedSubjectAllottedTime.values());

                                          //  System.out.println("SubjectStates (shuffleQuestion): " + subjectState.getShuffleQuestions());

                                        } else {
                                            System.out.println(TAG + "subject obj: " + subjectState.getSubject() + " unselected.. removing from selected list");
                                            selectedObjectiveSubjects.remove(subjectState.getSubject().getShortTitle());
                                            selectedSubjectAllottedTime.remove(subjectState.getSubject().getShortTitle());
                                        }
                                    }
                            )
            );
        });

        theorySubjects.forEach(vm -> {
            disposables.add(
                    vm.getSubjectState()
                            .subscribeOn(Schedulers.io())
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(
                                    subjectState -> {
                                        if (subjectState.getSelected()) {
                                            System.out.println(TAG + "subject theory: " + subjectState.getSubject() + " selected.. adding to selected list in map");
                                            selectedTheorySubjects.put(subjectState.getSubject().getShortTitle(), subjectState);

                                            selectedSubjectAllottedTime.put(subjectState.getSubject().getShortTitle(), subjectState.getSubject().getMinutesAllotted());

                                            System.out.println(TAG + "Selected Theory Subjects: (Key Set) -> " + selectedTheorySubjects.keySet());
                                        } else {
                                            System.out.println(TAG + "subject theory unselected.. removing from selected list");
                                            selectedTheorySubjects.remove(subjectState.getSubject().getShortTitle());
                                            selectedSubjectAllottedTime.remove(subjectState.getSubject().getShortTitle());
                                        }
                                    }
                            )
            );
        });
    }

    /**
     * Invalidates all previous selections (called when the user changes selection between 'Practice', 'Past Question' or 'CBT Game')
     */
    public void invalidateSubjectStates() {
        objectiveSubjects.forEach(SubjectListItemVM::invalidate);
        theorySubjects.forEach(SubjectListItemVM::invalidate);
    }

    @Override
    public void onViewAdded() {

    }

    @Override
    public void onViewRemoved() {
//        disposables.dispose();
    }

    public void dispose() {
        System.out.println("SubjectListViewModel disposables disposed");
        disposables.dispose();
    }


    public ObservableList<SubjectListItemVM> getObjectiveSubjects() {
        return objectiveSubjects;
    }

    public ObservableList<SubjectListItemVM> getTheorySubjects() {
        return theorySubjects;
    }
}
