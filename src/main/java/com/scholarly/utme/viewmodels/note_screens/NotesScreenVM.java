package com.scholarly.utme.viewmodels.note_screens;

import com.google.gson.Gson;
import com.scholarly.utme.controller.note_screens.NotesScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.data.model.newDb.NoteLastSession;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

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
//        subTopics.addAll(data.getSubTopics());
//        System.out.println(TAG + "Got questions for subject with id -> " + getPQSubjectId(data.getSubject().getSubjectId()) + ObjectiveQuestionDao.getQuestionsWithNoteSubjectId(getPQSubjectId(data.getSubject().getSubjectId())));
//        noteSubjectQuestions.addAll(ObjectiveQuestionDao.getQuestionsWithNoteSubjectId(getPQSubjectId(data.getSubject().getSubjectId())));

        selectedTopicIndex.set(data.getSelectedNoteTopic().getOrder()-1);

        noteTopics.forEach(topic -> {
            noteSections.put(topic.getId(), SectionDao.getNoteSectionsWithTopicId(topic.getId()));
            subTopics.put(topic.getId(), SubTopicDao.getSubTopicsForTopic(topic.getId()));
        });

        if (selectedSubTopic.get() != null) {
            List<NoteSection> selectedNoteSection = noteSections.get(selectedTopic.get().getId()).stream().filter(section -> section.getSubtopicId() == selectedSubTopic.get().getId()).toList();
//            System.out.println(TAG + "SelectedSubtopic Section -> " + selectedNoteSection);
            selectedSubtopicSection.set(selectedNoteSection.get(0));
        }
    }

    public void putLastSession(NoteLastSession lastSession) {
        int id = SectionDao.insertLastSection(lastSession);
        System.out.println(TAG + "Inserted last session with id -> " + id);
    }

//    public void handleHighlight(Section selectedSection, String colorCode) {
//
//        boolean highlighted = false;
//
//        for (int i = 0; i < subjectHighlights.size(); i++) {
//            Highlights currentHighlight = subjectHighlights.get(i);
//            if (subjectHighlights.get(i).getNoteId() == selectedSection.getId()) {
//                if (subjectHighlights.get(i).getColor().equals(colorCode)) {
//                    HighlightsDao.deleteHighlight(subjectHighlights.get(i).getId());
//                    subjectHighlights.remove(i);
//                } else {
//                    HighlightsDao.updateHighlight(new Highlights(currentHighlight.getId(), currentHighlight.getNoteTableName(), currentHighlight.getNoteId(), colorCode));
//                    subjectHighlights.clear();
//                    subjectHighlights.addAll(HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections"));
//                }
//                highlighted = true;
//                break;
//            }
//        }
//
//        if (!highlighted) {
//            HighlightsDao.createHighlight("note_" + subject.getTableName() + "_sections", selectedSection.getId(), colorCode);
//            subjectHighlights.clear();
//            subjectHighlights.addAll(HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections"));
//        }
//    }

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

//    public void setSubTopics(ObservableList<NoteSubTopic> subTopics) {
//        this.subTopics = subTopics;
//    }

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

    //    public void addNote(Section selectedSection, String note) {
//
//        boolean noted = false;
//
//        for (int i = 0; i < subjectNotes.size(); i++) {
//            Note currentNote = subjectNotes.get(i);
//            if (subjectNotes.get(i).getNoteId() == selectedSection.getId()) {
//                {
//                    NoteDao.updateNote(new Note(currentNote.getId(), currentNote.getNoteTableName(), currentNote.getNoteId(), note));
//                    subjectNotes.clear();
//                    subjectNotes.addAll(NoteDao.getNotes("note_" + subject.getTableName() + "_sections"));
//                }
//                noted = true;
//                break;
//            }
//        }
//
//        if (!noted) {
//            NoteDao.createNote("note_" + subject.getTableName() + "_sections", selectedSection.getId(), note);
//            subjectNotes.clear();
//            subjectNotes.addAll(NoteDao.getNotes("note_" + subject.getTableName() + "_sections"));
//        }
//    }


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
//        System.out.println(TAG + "getNoteSubtopicSection Subtopic Id -> " + subTopic.getId());
//        System.out.println(TAG + "getNoteSubtopicSection selectedTopic Id -> " + selectedTopic.get().getId());
        return noteSections.get(selectedTopic.get().getId()).stream().filter(section -> section.getSubtopicId() == subTopic.getId()).toList().get(0);
    }

    public ObjectiveQuestion getQuestion(int subjectId, int yearId, int questionNum) {
        return ObjectiveQuestionDao.getNoteCBTQuestion(subjectId, yearId, questionNum);
//        return noteSubjectQuestions.stream().filter(objectiveQuestion ->
//                objectiveQuestion.getYearId() == yearId && objectiveQuestion.getQuestionNumber() == questionNum
//        ).toList().get(0);
    }

    private int getPQSubjectId(int noteSubjectId) {
        return SubjectDao.getPQSubjectId(noteSubjectId);
    }
}
