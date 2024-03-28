package com.scholarly.viewmodels.note_screens;

import com.google.gson.Gson;
import com.scholarly.controller.note_screens.NotesScreenController.InitialData;
import com.scholarly.data.dao.ObjectiveQuestionDao;
import com.scholarly.data.dao.SubjectDao;
import com.scholarly.network.model.UserData;
import com.scholarly.data.model.newDb.NoteLastSession;
import com.scholarly.data.dao.newDb.SectionDao;
import com.scholarly.data.dao.newDb.SubTopicDao;
import com.scholarly.data.model.Highlights;
import com.scholarly.data.model.Note;
import com.scholarly.data.model.ObjectiveQuestion;
import com.scholarly.data.model.newDb.*;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

import static com.scholarly.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class NotesScreenVM implements ViewModel {
    private static final String TAG = "NotesScreenVM: ";

    NoteSubject subject;
    NoteTopic noteTopic;
    ObservableList<NoteTopic> noteTopics = FXCollections.observableArrayList();
    SimpleObjectProperty<NoteTopic> selectedTopic = new SimpleObjectProperty<>(null);
    SimpleObjectProperty<NoteSubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);
    SimpleObjectProperty<NoteSection> selectedSubtopicSection = new SimpleObjectProperty<>(null);
    SimpleObjectProperty<Integer> selectedTopicIndex = new SimpleObjectProperty<>(null);
    SimpleObjectProperty<NoteSection> selectedSection = new SimpleObjectProperty<>(null);

    HashMap<Integer, ObservableList<NoteSubTopic>> subTopics = new HashMap<>();
    HashMap<Integer, ObservableList<Section>> subTopicSections = new HashMap<>();
    HashMap<Integer, ObservableList<NoteSection>> noteSections = new HashMap<>();
    ObservableList<Highlights> subjectHighlights = FXCollections.observableArrayList();
    ObservableList<Note> subjectNotes = FXCollections.observableArrayList();
    ObservableList<ObjectiveQuestion> noteSubjectQuestions = FXCollections.observableArrayList();


    Gson gson = new Gson();

    private final UserData userData;
    private final String userId;

    public NotesScreenVM() {
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
        userData = gson.fromJson(userDataString, UserData.class);
    }

    public void initialize(InitialData data) {
        subject = data.getSubject();
        noteTopics.addAll(data.getTopics());
        noteTopic = data.getSelectedNoteTopic();
        selectedTopic.set(noteTopic);
        selectedSubTopic.set(data.getSelectedSubTopic());
        selectedSection.set(data.getSelectedSection());
        selectedTopicIndex.set(data.getSelectedNoteTopic().getOrder()-1);

        noteTopics.forEach(topic -> {
            noteSections.put(topic.getId(), SectionDao.getNoteSectionsWithTopicId(topic.getId()));
            subTopics.put(topic.getId(), SubTopicDao.getSubTopicsForTopic(topic.getId()));
        });

        if (selectedSubTopic.get() != null) {
            List<NoteSection> selectedNoteSection = noteSections.get(selectedTopic.get().getId()).stream().filter(section -> section.getSubtopicId() == selectedSubTopic.get().getId()).toList();
            selectedSubtopicSection.set(selectedNoteSection.get(0));
        }
    }

    public void putLastSession(NoteLastSession lastSession) {
        int id = SectionDao.insertLastSection(lastSession);
        System.out.println(TAG + "Inserted last session with id -> " + id);
    }

    public NoteSubject getSubject() {
        return subject;
    }

    public void setSubject(NoteSubject subject) {
        this.subject = subject;
    }

    public NoteTopic getTopic() {
        return noteTopic;
    }

    public void setNoteTopic(NoteTopic noteTopic) {
        this.noteTopic = noteTopic;
    }

    public HashMap<Integer, ObservableList<NoteSubTopic>> getSubTopics() {
        return subTopics;
    }

    public ObservableList<NoteTopic> getNoteTopics() {
        return noteTopics;
    }

    public NoteSubTopic getSelectedSubTopic() {
        return selectedSubTopic.get();
    }
    public NoteSection getSelectedSubtopicSection() {
        return selectedSubtopicSection.get();
    }

    public NoteTopic getSelectedTopic() {
        return selectedTopic.get();
    }

    public Integer getSelectedTopicIndex() {
        return selectedTopicIndex.get();
    }

    public NoteSection getSelectedSection() {
        return selectedSection.get();
    }

    public SimpleObjectProperty<NoteTopic> selectedTopicProperty() {
        return selectedTopic;
    }
    public SimpleObjectProperty<NoteSubTopic> selectedSubTopicProperty() {
        return selectedSubTopic;
    }

    public SimpleObjectProperty<NoteSection> selectedSubtopicSectionProperty() {
        return selectedSubtopicSection;
    }

    public SimpleObjectProperty<Integer> selectedTopicIndexProperty() {
        return selectedTopicIndex;
    }

    public void setSelectedTopic(NoteTopic selectedTopic) {
        this.selectedTopic.set(selectedTopic);
    }

    public void setSelectedSubTopic(NoteSubTopic selectedSubTopic) {
        this.selectedSubTopic.set(selectedSubTopic);
    }

    public void setSelectedSubtopicSection(NoteSection selectedSubTopicSection) {
        this.selectedSubtopicSection.set(selectedSubTopicSection);
    }

    public void setSelectedTopicIndex(Integer selectedTopicIndex) {
        this.selectedTopicIndex.set(selectedTopicIndex);
    }

    public HashMap<Integer, ObservableList<Section>> getSubTopicSections() {
        return subTopicSections;
    }

    public void setSubTopicSections(HashMap<Integer, ObservableList<Section>> subTopicSections) {
        this.subTopicSections = subTopicSections;
    }

    public UserData getUser() {
        return userData;
    }

    public ObservableList<Highlights> getSubjectHighlights() {
        return subjectHighlights;
    }

    public ObservableList<Note> getSubjectNotes() {
        return subjectNotes;
    }

    public HashMap<Integer, ObservableList<NoteSection>> getNoteSections() {
        return noteSections;
    }

    public NoteSection getNoteSubtopicSection(NoteSubTopic subTopic) {
        return noteSections.get(selectedTopic.get().getId()).stream().filter(section -> section.getSubtopicId() == subTopic.getId()).toList().get(0);
    }

    public ObjectiveQuestion getQuestion(int subjectId, int yearId, int questionNum) {
        return ObjectiveQuestionDao.getNoteCBTQuestion(subjectId, yearId, questionNum);
    }

    private int getPQSubjectId(int noteSubjectId) {
        return SubjectDao.getPQSubjectId(noteSubjectId);
    }
}
