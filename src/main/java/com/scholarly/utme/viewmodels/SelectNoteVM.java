package com.scholarly.utme.viewmodels;


import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.TopicDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SelectNoteVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<Topic>> subjectTopics = new HashMap<>();
    private HashMap<String, ObservableList<SubTopic>> subjectSubTopics = new HashMap<>();

    private SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<SubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);

    public SelectNoteVM() {

        ObservableList<Subject> subjectList = SubjectDao.getSubjects();

        subjects.addAll(subjectList);

        subjects.forEach(subject -> {
            subjectTopics.put(subject.getSubjectName(), TopicDao.getTopics("note_" + subject.getTableName() + "_topics"));
        });

        subjects.forEach(subject -> {
            subjectSubTopics.put(subject.getSubjectName(), SubTopicDao.getSubTopics("note_" + subject.getTableName() + "_sub_topics"));
        });
    }


    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public HashMap<String, ObservableList<Topic>> getSubjectTopics() {
        return subjectTopics;
    }

    public HashMap<String, ObservableList<SubTopic>> getSubjectSubTopics() {
        return subjectSubTopics;
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public SimpleObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public Topic getSelectedTopic() {
        return selectedTopic.get();
    }

    public SimpleObjectProperty<Topic> selectedTopicProperty() {
        return selectedTopic;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic.set(selectedTopic);
    }

    public SubTopic getSelectedSubTopic() {
        return selectedSubTopic.get();
    }

    public SimpleObjectProperty<SubTopic> selectedSubTopicProperty() {
        return selectedSubTopic;
    }

    public void setSelectedSubTopic(SubTopic selectedSubTopic) {
        this.selectedSubTopic.set(selectedSubTopic);
    }
}
