package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.NotesScreenController.InitialData;
import com.scholarly.utme.data.dao.HighlightsDao;
import com.scholarly.utme.data.dao.NoteDao;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.model.Highlights;
import com.scholarly.utme.data.model.Note;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.Section;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.Topic;
import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class NotesScreenVM implements ViewModel {

    Subject subject;
    Topic topic;
    ObservableList<SubTopic> subTopics = FXCollections.observableArrayList();
    SimpleObjectProperty<SubTopic> selectedSubTopic = new SimpleObjectProperty<>(null);

    HashMap<Integer, ObservableList<Section>> subTopicSections = new HashMap<>();
    ObservableList<Highlights> subjectHighlights = FXCollections.observableArrayList();
    ObservableList<Note> subjectNotes = FXCollections.observableArrayList();

    public NotesScreenVM() {}

    public void initialize(InitialData data) {
        subject = data.getSubject();
        topic = data.getTopic();
        selectedSubTopic.set(data.getSelectedSubTopic());
        subTopics.addAll(data.getSubTopics());

        subTopics.forEach(subTopic -> {
            subTopicSections.put(subTopic.getId(), SectionDao.getSections("note_" + subject.getTableName() + "_sections", subTopic.getSectionId()));
            subjectHighlights = HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections");
            subjectNotes = NoteDao.getNotes("note_" + subject.getTableName() + "_sections");
        });
    }


    public void handleHighlight(Section selectedSection, String colorCode) {

        boolean highlighted = false;

        for (int i = 0; i < subjectHighlights.size(); i++) {
            Highlights currentHighlight = subjectHighlights.get(i);
            if (subjectHighlights.get(i).getNoteId() == selectedSection.getId()) {
                if (subjectHighlights.get(i).getColor().equals(colorCode)) {
                    HighlightsDao.deleteHighlight(subjectHighlights.get(i).getId());
                    subjectHighlights.remove(i);
                } else {
                    HighlightsDao.updateHighlight(new Highlights(currentHighlight.getId(), currentHighlight.getNoteTableName(), currentHighlight.getNoteId(), colorCode));
                    subjectHighlights.clear();
                    subjectHighlights.addAll(HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections"));
                }
                highlighted = true;
                break;
            }
        }

        if (!highlighted) {
            HighlightsDao.createHighlight("note_" + subject.getTableName() + "_sections", selectedSection.getId(), colorCode);
            subjectHighlights.clear();
            subjectHighlights.addAll(HighlightsDao.getHighlights("note_" + subject.getTableName() + "_sections"));
        }
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public ObservableList<SubTopic> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(ObservableList<SubTopic> subTopics) {
        this.subTopics = subTopics;
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

    public HashMap<Integer, ObservableList<Section>> getSubTopicSections() {
        return subTopicSections;
    }

    public void setSubTopicSections(HashMap<Integer, ObservableList<Section>> subTopicSections) {
        this.subTopicSections = subTopicSections;
    }


    public void addNote(Section selectedSection, String note) {

        boolean noted = false;

        for (int i = 0; i < subjectNotes.size(); i++) {
            Note currentNote = subjectNotes.get(i);
            if (subjectNotes.get(i).getNoteId() == selectedSection.getId()) {
                {
                    NoteDao.updateNote(new Note(currentNote.getId(), currentNote.getNoteTableName(), currentNote.getNoteId(), note));
                    subjectNotes.clear();
                    subjectNotes.addAll(NoteDao.getNotes("note_" + subject.getTableName() + "_sections"));
                }
                noted = true;
                break;
            }
        }

        if (!noted) {
            NoteDao.createNote("note_" + subject.getTableName() + "_sections", selectedSection.getId(), note);
            subjectNotes.clear();
            subjectNotes.addAll(NoteDao.getNotes("note_" + subject.getTableName() + "_sections"));
        }
    }


    public ObservableList<Highlights> getSubjectHighlights() {
        return subjectHighlights;
    }

    public ObservableList<Note> getSubjectNotes() {
        return subjectNotes;
    }
}
