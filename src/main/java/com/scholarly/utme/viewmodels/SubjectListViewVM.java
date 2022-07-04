package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.SubjectTheoryDao;
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

    private ObservableList<SubjectListItemVM> objectiveSubjects = FXCollections.observableArrayList();
    private ObservableList<SubjectListItemVM> theorySubjects = FXCollections.observableArrayList();

    private Map<String, SubjectState> linkedHashMap = new LinkedHashMap<>();
    private ObservableMap<String, SubjectState> selectedObjectiveSubjects = FXCollections.observableMap(linkedHashMap); // LinkedHashMap because it maintains insertion order

    public ObservableMap<String, SubjectState> getSelectedObjectiveSubjects() {
        return selectedObjectiveSubjects;
    }

    public ObservableMap<String, SubjectState> getSelectedTheorySubjects() {
        return selectedTheorySubjects;
    }

    private ObservableMap<String, SubjectState> selectedTheorySubjects = FXCollections.observableHashMap();

    private CompositeDisposable disposables = new CompositeDisposable();


    public SubjectListViewVM() {
        objectiveSubjects.addAll(SubjectDao.getSubjects().stream().map(SubjectListItemVM::new).collect(Collectors.toList()));
        theorySubjects.addAll(SubjectTheoryDao.getSubjectsTheory().stream().map(SubjectListItemVM::new).collect(Collectors.toList()));

        objectiveSubjects.forEach(vm -> {
            disposables.add(
                    vm.getSubjectState()
                            .subscribeOn(Schedulers.io())
                            .observeOn(JavaFxScheduler.platform())
                            .subscribe(
                                    subjectState -> {
                                        if (subjectState.getSelected()) {
                                            System.out.println("subject obj: " + subjectState.getSubject() + " selected.. adding to selected list in map");
                                            selectedObjectiveSubjects.put(subjectState.getSubject().getTableName(), subjectState);

                                            System.out.println("Selected Objective Subjects: (Key Set) -> " + selectedObjectiveSubjects.keySet());
                                          //  System.out.println("SubjectStates (shuffleQuestion): " + subjectState.getShuffleQuestions());


                                        } else {
                                            System.out.println("subject obj unselected.. removing from selected list");
                                            selectedObjectiveSubjects.remove(subjectState.getSubject().getTableName());
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
                                            System.out.println("subject theory selected.. adding to selected list");
                                            selectedTheorySubjects.put(subjectState.getSubject().getTableName(), subjectState);
                                        } else {
                                            System.out.println("subject theory unselected.. removing from selected list");
                                            selectedTheorySubjects.remove(subjectState.getSubject().getTableName());
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
        System.out.println("SubjectListViewModel added");
    }

    @Override
    public void onViewRemoved() {
//        disposables.dispose();
        System.out.println("SubjectListViewModel removed");
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
