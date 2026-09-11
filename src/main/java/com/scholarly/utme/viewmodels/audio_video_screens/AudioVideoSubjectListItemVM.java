package com.scholarly.utme.viewmodels.audio_video_screens;

import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.TopicDao;
import com.scholarly.utme.data.model.MediaSubTopic;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
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

public class AudioVideoSubjectListItemVM implements ViewModel {

    public enum Type {
        VIDEO,
        AUDIO
    }

    private SimpleStringProperty subjectName = new SimpleStringProperty("");
    private SimpleStringProperty subjectTableName = new SimpleStringProperty("");
    private SimpleStringProperty subjectColorName = new SimpleStringProperty("");

    private SimpleBooleanProperty subjectSelected = new SimpleBooleanProperty();

    private ObjectProperty<Topic> selectedTopicProperty = new SimpleObjectProperty<>();

    private ObjectProperty<MediaSubTopic> selectedSubtopicProperty = new SimpleObjectProperty<>();

    private Subject subject;

    private ObservableList<Topic> topics;
    private static ObservableList<MediaSubTopic> subTopics = FXCollections.observableArrayList();

    private BehaviorSubject<AudioVideoSubjectListItemVM.MediaSubjectState> subjectState = BehaviorSubject.create();

    public AudioVideoSubjectListItemVM(Subject subject) {
        this.subject = subject;
        subjectName.set(subject.getSubjectName());
        subjectTableName.set(subject.getTableName());
        subjectColorName.set(getColorName(subject.getTableName()));

        subjectState.onNext(new MediaSubjectState(subject, subjectSelected.get(), selectedTopicProperty.get(), selectedSubtopicProperty.get()));


        topics = TopicDao.getTopics(subject.getTableName() + "_topics");
//        System.out.println("Topics -> " + topics);
    }


    /**
     * Maps the current selection property of every subject to the SubjectState of each selected subject
     */
    public void mapPropertiesToState() {
        subjectSelected.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new AudioVideoSubjectListItemVM.MediaSubjectState(subject, newValue, selectedTopicProperty.get(), selectedSubtopicProperty.get()));
        }));
        selectedTopicProperty.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new AudioVideoSubjectListItemVM.MediaSubjectState(subject, subjectSelected.get(), newValue, selectedSubtopicProperty.get()));
        }));
        selectedSubtopicProperty.addListener(((observable, oldValue, newValue) -> {
            subjectState.onNext(new AudioVideoSubjectListItemVM.MediaSubjectState(subject, subjectSelected.get(), selectedTopicProperty.get(), newValue));
        }));

    }

    /**
     * Clears all previous subject selection properties
     */
    public void invalidate() {
        subjectSelected.set(false);
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getSubjectName() {
        return subjectName.get();
    }

    public SimpleBooleanProperty subjectSelectedProperty() {
        return subjectSelected;
    }

    public SimpleStringProperty subjectNameProperty() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName.set(subjectName);
    }

    public String getSubjectTableName() {
        return subjectTableName.get();
    }

    public SimpleStringProperty subjectTableNameProperty() {
        return subjectTableName;
    }

    public void setSubjectTableName(String subjectTableName) {
        this.subjectTableName.set(subjectTableName);
    }

    public String getSubjectColorName() {
        return subjectColorName.get();
    }

    public void setSelectedTopicProperty(Topic selectedTopic) {
        this.selectedTopicProperty.set(selectedTopic);
    }

    public void setSelectedSubtopicProperty(MediaSubTopic selectedSubtopic) {
        this.selectedSubtopicProperty.set(selectedSubtopic);
    }

    public ObservableList<Topic> getTopics() {
        return topics;
    }

    public ObservableList<MediaSubTopic> getSubTopics() {
        return subTopics;
    }

    public void loadSubtopics(Topic topic) {
        subTopics.clear();
        Observable.just(SubTopicDao.getSubTopicsWithTopicId(subjectTableName.get(), topic.getId()))
                        .subscribeOn(Schedulers.io())
                                .blockingSubscribe(it -> {
                                    subTopics.addAll(it);
                                });
//        System.out.println("Subtopics -> " + subTopics);
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

    public static class MediaSubjectState {
        private Subject subject;

        private Boolean isSelected;

        private Topic selectedTopic;
        private MediaSubTopic selectedSubtopic;

        public MediaSubjectState(Subject subject, Boolean isSelected, Topic selectedTopic, MediaSubTopic selectedSubtopic) {
            this.subject = subject;
            this.isSelected = isSelected;
            this.selectedTopic = selectedTopic;
            this.selectedSubtopic = selectedSubtopic;
        }

        public Subject getSubject() {
            return subject;
        }

        public Boolean getSelected() {
            return isSelected;
        }

        public Topic getSelectedTopic() {
            return selectedTopic;
        }

        public MediaSubTopic getSelectedSubtopic() {
            return selectedSubtopic;
        }
    }
}
