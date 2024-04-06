package com.scholarly.viewmodels;

import com.scholarly.data.dao.ObjectiveQuestionDao;
import com.scholarly.data.dao.TheoryQuestionDao;
import com.scholarly.data.dao.YearsDao;
import com.scholarly.data.dao.newDb.TopicDao;
import com.scholarly.data.model.Year;
import com.scholarly.data.model.newDb.*;
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
import java.util.Objects;
import java.util.stream.Collectors;

public class SubjectListItemVM implements ViewModel {
    private static final String TAG = "SubjectListItemVM: ";

    public Year getSelectedYearProperty() {
        return selectedYearProperty.get();
    }

    public ObjectProperty<Year> selectedYearProperty() {
        return selectedYearProperty;
    }

    public void setSelectedYearProperty(Year selectedYearProperty) {
        this.selectedYearProperty.set(selectedYearProperty);
    }

    public ObjectProperty<List<Integer>> selectedTopicsProperty() {
        return selectedTopicsProperty;
    }

    public List<Integer> getSelectedTopicsProperty() {
        System.out.println(TAG + "Get Selected Topics called!");
        return selectedTopicsProperty.get();
    }

    public void setSelectedTopics(List<Integer> topicIds) {
        this.selectedTopicsProperty.set(topicIds);
    }

    public void setShuffleQuestions(Boolean shuffleQuestions) {
        this.shuffleQuestions.set(shuffleQuestions);
    }

    public boolean isFavoriteSubject() {
        return favoriteSubject.get();
    }

    public enum Type {
        OBJECTIVE,
        THEORY
    }

    private final SimpleStringProperty subjectName = new SimpleStringProperty("");
    private String subjectDesc = null;
    private final SimpleStringProperty subjectColorName = new SimpleStringProperty("");
    private ObservableList<Year> years;
    private final ObservableList<PQTopic> topics = FXCollections.observableArrayList();

    private final ObservableList<Integer> questionNumbers = FXCollections.observableArrayList();

    private Type type;

    private final SimpleBooleanProperty subjectSelected = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty shuffleQuestions = new SimpleBooleanProperty(false);
    private final SimpleBooleanProperty shuffleOptions = new SimpleBooleanProperty(false);
    private final ObjectProperty<List<Integer>> selectedTopicsProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Year> selectedYearProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> selectedNumberOfQuestions = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty favoriteSubject = new SimpleBooleanProperty(false);

    private PQSubject subject;
    private final BehaviorSubject<SubjectState> subjectState = BehaviorSubject.create();

    public SubjectListItemVM(PQSubject subject) {
        this.subject = subject;
        subjectName.set(subject.getTitle());
        subjectDesc = subject.getDescription();

        subjectColorName.set(subject.getColorCode());
        favoriteSubject.set(subject.isFavorite());

//        years = YearsDao.getAvailableYearsForSubject(type, subject.getId());
//        topics = TopicDao.getTopicsForSubject(subject.getSubjectId());

        subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), selectedTopicsProperty.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));

        mapPropertiesToState();
    }

    public SubjectListItemVM() {

    }

    /**
     * Maps the current selection property of every subject to the SubjectState of each selected subject
     */
    public void mapPropertiesToState() {
        subjectSelected.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, newValue, shuffleQuestions.get(), shuffleOptions.get(), selectedTopicsProperty.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        shuffleQuestions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), newValue, shuffleOptions.get(), selectedTopicsProperty.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        shuffleOptions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), newValue, selectedTopicsProperty.get(), selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        selectedTopicsProperty.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), newValue, selectedYearProperty.get(), selectedNumberOfQuestions.get()));
        }));
        selectedYearProperty.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), selectedTopicsProperty.get(), newValue, selectedNumberOfQuestions.get()));
        }));
        selectedNumberOfQuestions.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new SubjectState(subject, type, subjectSelected.get(), shuffleQuestions.get(), shuffleOptions.get(), selectedTopicsProperty.get(), selectedYearProperty.get(), newValue));
        }));
    }

    /**
     * Clears all existing subject selection properties
     */
    public void invalidate() {
        subjectSelected.set(false);
        shuffleQuestions.set(false);
        shuffleOptions.set(false);
    }

    public void selectSubject(Type subjectType) {
//        type = Type.OBJECTIVE;
        type = subjectType;
        subjectSelected.set(true);
        shuffleQuestions.set(false);
        shuffleOptions.set(false);
        selectedTopicsProperty.set(topics.stream().map(PQTopic::getId).collect(Collectors.toList()));
        Year year = Objects.requireNonNull(new YearsDao().getAvailableYearsForSubject(type, subject.getId())).stream().filter(Year::isFree).toList().get(0);
        selectedYearProperty.set(year);
    }

    public void setSubject(PQSubject subject) {
        this.subject = subject;
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public String getSubjectDesc() {
        return subjectDesc;
    }

    public String getSubjectColorName() {
        return subjectColorName.get();
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public ObservableList<Year> getYears() {
        return years;
    }

    public ObservableList<PQTopic> getTopics() {
        return topics;
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

    public PQSubject getSubject() {
        return subject;
    }

    public void loadQuestionNumbersList(Year year) {
        questionNumbers.clear();

        if (type == Type.OBJECTIVE) {
            Observable.just(Objects.requireNonNull(ObjectiveQuestionDao.getQuestions(subject.getId(), year.getId(), FXCollections.emptyObservableList(), false)))
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
                                if (numberList.size() == 0) {
                                    questionNumbers.add(0);
                                }
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );

        } else {
            Observable.just(Objects.requireNonNull(TheoryQuestionDao.getQuestions(subject.getId(), year.getId(), FXCollections.emptyObservableList(), false)))
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
                                if (numberList.size() == 0) {
                                    questionNumbers.add(0);
                                }
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );
        }
    }

    public void loadTopicsForYear(Year year) {
        topics.clear();
        topics.addAll(Objects.requireNonNull(TopicDao.getPQTopicsForSubjectAndYear(subject.getId(), year.getId())));
    }

    public void loadQuestionNumbersList(List<Integer> topicIdsList) {
        questionNumbers.clear();

        if (type == Type.OBJECTIVE) {
            Observable.just(Objects.requireNonNull(ObjectiveQuestionDao.getQuestions(subject.getId(), selectedYearProperty.get().getId(), FXCollections.observableArrayList(topicIdsList), false)))
                    .subscribeOn(Schedulers.io())
                    .map(it -> {
                        List<Integer> numberList = new ArrayList<>();
                        if (it.size() > 50) {
                            for ( int i = 10; i <= it.size(); i+=10) {
                                numberList.add(i);
                            }
                        } else if (it.size() < 50 && it.size() > 10){
                            for ( int i = 5; i <= it.size(); i+=5) {
                                numberList.add(i);
                            }
                        } else {
                            for ( int i = 0; i <= it.size(); i++) {
                                numberList.add(i);
                            }
                        }
                        return numberList;
                    })
                    .blockingSubscribe(
                            numberList -> {
                                if (numberList.size() == 0) {
                                    questionNumbers.add(0);
                                }
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );

        } else {
            Observable.just(Objects.requireNonNull(TheoryQuestionDao.getQuestions(subject.getId(), selectedYearProperty.get().getId(), FXCollections.observableArrayList(topicIdsList), false)))
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
                                if (numberList.size() == 0) {
                                    questionNumbers.add(0);
                                }
                                questionNumbers.addAll(numberList);
                            },
                            error -> {}
                    );
        }
    }

    public void populateYearsList() {
        years = new YearsDao().getAvailableYearsForSubject(type, subject.getId());
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
            case "Eng" -> "#E90000";
            case "Maths" -> "#E86D1C";
            case "Bio" -> "#009D9A";
            case "Lit-In-Eng" -> "#5A67D8";
            case "Comm" -> "#56749E";
            case "Econs" -> "#B76623";
            case "Phy" -> "#D68E00";
            case "Chm" -> "#00A14B";
            case "Govt" -> "#0067C8";
            case "Acct" -> "#D12C81";
            case "CRS" -> "#005F7A";
            case "IRS" -> "#630F0F";
            default -> "#00A14B";
        };
    }


    public static class SubjectState {
        private final PQSubject subject;
        private final Type type;
        private final Boolean isSelected;
        private final Boolean shuffleQuestions;
        private final Boolean shuffleOptions;
        private final List<Integer> selectedTopics;
        private final Year selectedYear;
        private final Integer numberOfQuestions;

        public SubjectState(PQSubject subject, Type type, Boolean isSelected, Boolean shuffleQuestions, Boolean shuffleOptions, List<Integer> selectedTopics, Year selectedYear, Integer numberOfQuestions) {
            this.subject = subject;
            this.type = type;
            this.isSelected = isSelected;
            this.shuffleQuestions = shuffleQuestions;
            this.shuffleOptions = shuffleOptions;
            this.selectedTopics = selectedTopics;
            this.selectedYear = selectedYear;
            this.numberOfQuestions = numberOfQuestions;
        }

        public PQSubject getSubject() {
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

        public List<Integer> getSelectedTopics() {
            return selectedTopics;
        }

        public Year getSelectedYear() {
            return selectedYear;
        }

        public Integer getNumberOfQuestions() {
            return numberOfQuestions;
        }
    }
}
