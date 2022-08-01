package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.TheoryQuestionDao;
import com.scholarly.utme.data.dao.YearsDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Year;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class SubjectListItemVM implements ViewModel {

    public Year getSelectedYearProperty() {
        return selectedYearProperty.get();
    }

    public ObjectProperty<Year> selectedYearProperty() {
        return selectedYearProperty;
    }

    public void setSelectedYearProperty(Year selectedYearProperty) {
        this.selectedYearProperty.set(selectedYearProperty);
    }

    public void setShuffleQuestions(Boolean shuffleQuestions) {
        this.shuffleQuestions.set(shuffleQuestions);
    }

    public enum Type {
        OBJECTIVE,
        THEORY
    }

    private SimpleStringProperty subjectName = new SimpleStringProperty("");
    private SimpleStringProperty subjectTableName = new SimpleStringProperty("");
    private SimpleStringProperty subjectColorName = new SimpleStringProperty("");
    private ObservableList<Year> years;

    private ObservableList<Integer> questionNumbers = FXCollections.observableArrayList();

    private Type type;

    private SimpleBooleanProperty subjectSelected = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty shuffleQuestions = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty shuffleOptions = new SimpleBooleanProperty(false);

    private ObjectProperty<Year> selectedYearProperty = new SimpleObjectProperty<>();

    private ObjectProperty<Integer> selectedNumberOfQuestions = new SimpleObjectProperty<>();

    private Subject subject;

    private BehaviorSubject<SubjectState> subjectState = BehaviorSubject.create();

    public SubjectListItemVM(Subject subject) {
        this.subject = subject;
        subjectName.set(subject.getSubjectName());
        subjectTableName.set(subject.getTableName());
        subjectColorName.set(getColorName(subject.getTableName()));

       // years = YearsDao.getYears();

        subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));

        mapPropertiesToState();

        years = YearsDao.getAvailableYearsForSubject(subject.getTableName());
    }

    /**
     * Maps the current selection property of every subject to the SubjectState of each selected subject
     */
    public void mapPropertiesToState() {
        subjectSelected.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, newValue, shuffleQuestions.get(), shuffleOptions.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        shuffleQuestions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), newValue, shuffleOptions.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        shuffleOptions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), newValue, selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        selectedYearProperty.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), newValue, selectedNumberOfQuestions.get()));
        }));
        selectedNumberOfQuestions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), selectedYearProperty.get(), newValue));
        }));
    }

    /**
     * Clears all previous subject selection properties
     */
    public void invalidate() {
        subjectSelected.set(false);
        shuffleQuestions.set(false);
        shuffleOptions.set(false);
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public String getSubjectTableName() {
        return subjectTableName.get();
    }

    public String getSubjectColorName() {
        return subjectColorName.get();
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public SimpleStringProperty subjectTableNameProperty() {
        return subjectTableName;
    }

    public ObservableList<Year> getYears() {
        return years;
    }

    public ObservableList<Integer> getQuestionNumbers() {
        return questionNumbers;
    }

    public boolean isSubjectSelected() {
        return subjectSelected.get();
    }

    public SimpleBooleanProperty subjectSelectedProperty() {
        return subjectSelected;
    }

    public boolean isShuffleQuestions() {
        return shuffleQuestions.get();
    }

    public SimpleBooleanProperty shuffleQuestionsProperty() {
        return shuffleQuestions;
    }

    public boolean isShuffleOptions() {
        return shuffleOptions.get();
    }

    public SimpleBooleanProperty shuffleOptionsProperty() {
        return shuffleOptions;
    }

    public Subject getSubject() {
        return subject;
    }

    public void loadQuestionNumbersList(Year year) {

        questionNumbers.clear();
        if (type == Type.OBJECTIVE) {
            Observable.just(ObjectiveQuestionDao.getQuestions(subject.getTableName(), year.getId(), false))
                    .subscribeOn(Schedulers.io())
                    .map(it -> {
                        List<Integer> numberList = new ArrayList<>();
                        for ( int i = 10; i <= it.size(); i+=10) {
                            numberList.add(i);
                        }
                        return numberList;
                    })
                    .blockingSubscribe(
                            numberList -> {
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );
//            ObservableList<ObjectiveQuestion> questionList = ObjectiveQuestionDao.getQuestions(subject.getTableName(), year.getId(), false);
//            for ( int i = 1; i <= questionList.size(); i++) {
//                questionNumbers.add(i);
//            }
        } else {
//            ObservableList<TheoryQuestion> questionList = TheoryQuestionDao.getQuestions(subject.getTableName(), year.getId(), false);
//            for ( int i = 1; i <= questionList.size(); i++) {
//                questionNumbers.add(i);
//            }
            Observable.just(TheoryQuestionDao.getQuestions(subject.getTableName(), year.getId(), false))
                    .subscribeOn(Schedulers.io())
                    .map(it -> {
                        List<Integer> numberList = new ArrayList<>();
                        for ( int i = 10; i <= it.size(); i+=10) {
                            numberList.add(i);
                        }
                        return numberList;
                    })
                    .blockingSubscribe(
                            numberList -> {
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Observable<SubjectState> getSubjectState() {
        return subjectState;
    }

    public Integer getSelectedNumberOfQuestions() {
        return selectedNumberOfQuestions.get();
    }

    public ObjectProperty<Integer> selectedNumberOfQuestionsProperty() {
        return selectedNumberOfQuestions;
    }

    public void setSelectedNumberOfQuestions(Integer selectedNumberOfQuestions) {
        this.selectedNumberOfQuestions.set(selectedNumberOfQuestions);
    }

    private String getColorName(String subjectTableName) {
        return switch (subjectTableName) {
            case "english", "english_theory" -> "#E90000";
            case "mathematics", "mathematics_theory" -> "#E86D1C";
            case "biology", "biology_theory" -> "#009D9A";
            case "literature", "literature_theory" -> "#5A67D8";
            case "commerce", "commerce_theory" -> "#56749E";
            case "economics", "economics_theory" -> "#B76623";
            case "physics", "physics_theory" -> "#D68E00";
            case "chemistry", "chemistry_theory" -> "#00A14B";
            case "government", "government_theory" -> "#0067C8";
            case "accounts", "accounts_theory" -> "#D12C81";
            case "crs", "crs_theory" -> "#005F7A";
            case "irs", "irs_theory" -> "#630F0F";
            default -> "#00A14B";
        };
    }


    public static class SubjectState {
        private Subject subject;
        private Type type;

        private Boolean isSelected;
        private Boolean shuffleQuestions;
        private Boolean shuffleOptions;

        private Year selectedYear;
        private Integer numberOfQuestions;

        public SubjectState(Subject subject, Type type, Boolean isSelected, Boolean shuffleQuestions, Boolean shuffleOptions, Year selectedYear, Integer numberOfQuestions) {
            this.subject = subject;
            this.type = type;
            this.isSelected = isSelected;
            this.shuffleQuestions = shuffleQuestions;
            this.shuffleOptions = shuffleOptions;
            this.selectedYear = selectedYear;
            this.numberOfQuestions = numberOfQuestions;
        }

        public Subject getSubject() {
            return subject;
        }

        public Type getType() {
            return type;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public Boolean getShuffleQuestions() {
            return shuffleQuestions;
        }

        public Boolean getShuffleOptions() {
            return shuffleOptions;
        }

        public Year getSelectedYear() {
            return selectedYear;
        }

        public Integer getNumberOfQuestions() {
            return numberOfQuestions;
        }
    }
}
