package com.scholarly.utme.viewmodels.note_screens;


import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.TopicDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SelectNoteVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObservableList<NoteSubject> noteSubjects = FXCollections.observableArrayList();
    private ObservableList<NoteTopic> noteTopics = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<Topic>> subjectTopics = new HashMap<>();
    private HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private HashMap<String, ObservableList<SubTopic>> subjectSubTopics = new HashMap<>();
    private HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    private SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<SubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);

    private SimpleObjectProperty<NoteSubject> selectedNoteSubject = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<NoteTopic> selectedNoteTopic = new SimpleObjectProperty<>(null);
    private SimpleObjectProperty<NoteSubTopic> selectedNoteSubTopic = new SimpleObjectProperty<>(null);

    public SelectNoteVM() {

        ObservableList<Subject> subjectList = SubjectDao.getSubjects();

        ObservableList<NoteSubject> noteSubjectsList = SubjectDao.getNoteSubjects();

        ObservableList<NoteTopic> noteTopicsList = TopicDao.getNoteTopics();

        subjects.addAll(subjectList);

        noteSubjects.addAll(noteSubjectsList);

        noteTopics.addAll(noteTopicsList);

        noteSubjects.forEach(subject -> {
            noteSubjectTopics.put(subject.getId(), TopicDao.getNoteTopicsForSubject(subject.getId()));
        });

        noteTopics.forEach(topic -> {
            noteSubTopics.put(topic.getTopicId(), SubTopicDao.getSubTopicsForTopic(topic.getTopicId()));
        });

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

    public ObservableList<NoteSubject> getNoteSubjects() {
        return noteSubjects;
    }

    public ObservableList<NoteTopic> getNoteTopics() {
        return noteTopics;
    }

    public HashMap<Integer, ObservableList<NoteTopic>> getNoteSubjectTopics() {
        return noteSubjectTopics;
    }

    public HashMap<Integer, ObservableList<NoteSubTopic>> getNoteSubTopics() {
        return noteSubTopics;
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

    public NoteSubject getSelectedNoteSubject() {
        return selectedNoteSubject.get();
    }

    public SimpleObjectProperty<NoteSubject> selectedNoteSubjectProperty() {
        return selectedNoteSubject;
    }

    public void setSelectedNoteSubject(NoteSubject selectedNoteSubject) {
        this.selectedNoteSubject.set(selectedNoteSubject);
    }

    public NoteTopic getSelectedNoteTopic() {
        return selectedNoteTopic.get();
    }

    public SimpleObjectProperty<NoteTopic> selectedNoteTopicProperty() {
        return selectedNoteTopic;
    }

    public void setSelectedNoteTopic(NoteTopic selectedNoteTopic) {
        this.selectedNoteTopic.set(selectedNoteTopic);
    }

    public NoteSubTopic getSelectedNoteSubTopic() {
        return selectedNoteSubTopic.get();
    }

    public SimpleObjectProperty<NoteSubTopic> selectedNoteSubTopicProperty() {
        return selectedNoteSubTopic;
    }

    public void setSelectedNoteSubTopic(NoteSubTopic selectedNoteSubTopic) {
        this.selectedNoteSubTopic.set(selectedNoteSubTopic);
    }
}
