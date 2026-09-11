package com.scholarly.viewmodels.note_screens;


import com.scholarly.data.dao.SubjectDao;
import com.scholarly.data.dao.newDb.SubTopicDao;
import com.scholarly.data.dao.newDb.TopicDao;
import com.scholarly.data.model.Subject;
import com.scholarly.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectNoteVM implements ViewModel {
    private final ObservableList<NoteSubject> noteSubjects = FXCollections.observableArrayList();
    private final ObservableList<NoteTopic> noteTopics = FXCollections.observableArrayList();
    private final HashMap<String, ObservableList<Topic>> subjectTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private final HashMap<String, ObservableList<SubTopic>> subjectSubTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    private final SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>(null);
    private final SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);
    private final SimpleObjectProperty<SubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);

    private final SimpleObjectProperty<NoteSubject> selectedNoteSubject = new SimpleObjectProperty<>(null);
    private final SimpleObjectProperty<NoteTopic> selectedNoteTopic = new SimpleObjectProperty<>(null);
    private final SimpleObjectProperty<NoteSubTopic> selectedNoteSubTopic = new SimpleObjectProperty<>(null);

    private final SimpleBooleanProperty subjectsLoaded = new SimpleBooleanProperty();

    public SelectNoteVM() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Task<Boolean> subjectsTask = new Task<>() {
            @Override
            protected Boolean call() {
                ObservableList<NoteSubject> noteSubjectsList = SubjectDao.getNoteSubjects();

                ObservableList<NoteTopic> noteTopicsList = TopicDao.getNoteTopics();

                noteSubjects.addAll(noteSubjectsList);

                noteTopics.addAll(noteTopicsList);

                noteSubjects.forEach(subject -> noteSubjectTopics.put(subject.getId(), TopicDao.getNoteTopicsForSubject(subject.getId())));

                noteTopics.forEach(topic -> noteSubTopics.put(topic.getId(), SubTopicDao.getSubTopicsForTopic(topic.getId())));
                return true;
            }
        };
        subjectsLoaded.bind(subjectsTask.valueProperty());

        executorService.execute(subjectsTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getSubjectsLoaded() {
        return subjectsLoaded;
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
