package com.scholarly.utme.viewmodels.note_screens;

import com.scholarly.utme.controller.note_screens.NotesScreenController.InitialData;
import com.scholarly.utme.data.dao.ObjectiveQuestionDao;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.model.ObjectiveQuestion;
import com.scholarly.utme.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NotesScreenVM implements ViewModel {
    private static final String TAG = "NotesScreenVM: ";

    NoteSubject subject;
    NoteTopic topic;
    ObservableList<NoteSubTopic> subTopics = FXCollections.observableArrayList();
    SimpleObjectProperty<NoteSubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);

    HashMap<Integer, ObservableList<Section>> subTopicSections = new HashMap<>();
    HashMap<Integer, ObservableList<NoteSection>> noteSections = new HashMap<>();
    ObservableList<Highlights> subjectHighlights = FXCollections.observableArrayList();
    ObservableList<Note> subjectNotes = FXCollections.observableArrayList();
    ObservableList<ObjectiveQuestion> noteSubjectQuestions = FXCollections.observableArrayList();

    public NotesScreenVM() {}

    public void initialize(InitialData data) {
        subject = data.getSubject();
        topic = data.getTopic();
        selectedSubTopic.set(data.getSelectedSubTopic());
        subTopics.addAll(data.getSubTopics());
        noteSubjectQuestions.addAll(ObjectiveQuestionDao.getQuestionsWithNoteSubjectId(getPQSubjectId(data.getSubject().getSubjectId())));
//        System.out.println(TAG + "Got Subtopics -> " + Helper.toString(subTopics));

        subTopics.forEach(subTopic -> {
//            subTopicSections.put(subTopic.getId(), SectionDao.getSections("note_" + subject.getTableName() + "_sections", subTopic.getSectionId()));
//            noteSections.put();
//            subjectHighlights = HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections");
//            subjectNotes = NoteDao.getNotes("note_" + subject.getTableName() + "_sections");
        });

        noteSections.put(topic.getId(), SectionDao.getNoteSectionsWithTopicId(topic.getId()));
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
        return topic;
    }

    public void setTopic(NoteTopic topic) {
        this.topic = topic;
    }

    public ObservableList<NoteSubTopic> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(ObservableList<NoteSubTopic> subTopics) {
        this.subTopics = subTopics;
    }

    public NoteSubTopic getSelectedSubTopic() {
        return selectedSubTopic.get();
    }

    public SimpleObjectProperty<NoteSubTopic> selectedSubTopicProperty() {
        return selectedSubTopic;
    }

    public void setSelectedSubTopic(NoteSubTopic selectedSubTopic) {
        this.selectedSubTopic.set(selectedSubTopic);
    }

    public HashMap<Integer, ObservableList<Section>> getSubTopicSections() {
        return subTopicSections;
    }

    public void setSubTopicSections(HashMap<Integer, ObservableList<Section>> subTopicSections) {
        this.subTopicSections = subTopicSections;
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

    public ObjectiveQuestion getQuestion(int yearId, int questionNum) {
        return noteSubjectQuestions.stream().filter(objectiveQuestion ->
            objectiveQuestion.getYearId() == yearId && objectiveQuestion.getQuestionNumber() == questionNum
        ).collect(Collectors.toList()).get(0);
    }

    private int getPQSubjectId(int noteSubjectId) {
        return SubjectDao.getPQSubjectId(noteSubjectId);
    }
}
