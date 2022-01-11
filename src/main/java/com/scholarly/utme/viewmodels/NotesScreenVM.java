package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.NotesScreenController.InitialData;
import com.scholarly.utme.data.dao.HighlightsDao;
import com.scholarly.utme.data.dao.NoteDao;
import com.scholarly.utme.data.dao.SectionDao;
import com.scholarly.utme.data.dao.SubSectionDao;
import com.scholarly.utme.data.model.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;

public class NotesScreenVM implements ViewModel {

    Subject subject;
    ObservableList<Topic> topics = FXCollections.observableArrayList();
    SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);

    ObservableList<Section> sections = FXCollections.emptyObservableList();
    HashMap<Integer, ObservableList<SubSection>> sectionsSubSections = new HashMap<>();
    HashMap<Integer, ObservableList<Highlights>> sectionsHighlights = new HashMap<>();
    HashMap<Integer, ObservableList<Note>> sectionsNotes = new HashMap<>();

    public NotesScreenVM() {}

    public void initialize(InitialData data) {
        subject = data.getSubject();
        selectedTopic.set(data.getSelectedTopic());
        topics.addAll(data.getTopics());

        sections = SectionDao.getSections(subject.getTableName() + "_sections");


        sections.forEach(section -> {
            sectionsSubSections.put(section.getId(), SubSectionDao.getSubSections(subject.getTableName() + "_sub_sections", section.getId()));
            sectionsHighlights.put(section.getId(), HighlightsDao.getHighlights(subject.getTableName() + "_sub_sections"));
            sectionsNotes.put(section.getId(), NoteDao.getNotes(subject.getTableName() + "_sub_sections"));
        });
    }


    public void handleHighlight(SubSection selectedSubSection, String colorCode) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getTopicId() == selectedTopic.get().getId()) {
                ObservableList<Highlights> highlightsList = sectionsHighlights.get(sections.get(i).getId());

                boolean highlighted = false;

                for (int j = 0; j < highlightsList.size(); j++) {
                    Highlights currentHighlight = highlightsList.get(j);
                    if (highlightsList.get(j).getNoteId() == selectedSubSection.getId()) {
                        if (highlightsList.get(j).getColor().equals(colorCode)) {
                            HighlightsDao.deleteHighlight(highlightsList.get(j).getId());
                            highlightsList.remove(j);
                        } else {
                            HighlightsDao.updateHighlight(new Highlights(currentHighlight.getId(), currentHighlight.getNoteTableName(), currentHighlight.getNoteId(), colorCode));
                            highlightsList.clear();
                            highlightsList.addAll(HighlightsDao.getHighlights(subject.getTableName() + "_sub_sections"));
                        }
                        highlighted = true;
                        break;
                    }
                }

                if (!highlighted) {
                    HighlightsDao.createHighlight(subject.getTableName() + "_sub_sections", selectedSubSection.getId(), colorCode);
                    highlightsList.clear();
                    highlightsList.addAll(HighlightsDao.getHighlights(subject.getTableName() + "_sub_sections"));
                }

                break;
            }
        }
    }


    public HashMap<Integer, ObservableList<Highlights>> getSectionsHighlights() {
        return sectionsHighlights;
    }


    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Topic getSelectedTopic() {
        return selectedTopic.get();
    }

    public SimpleObjectProperty<Topic> selectedTopicProperty() {
        return selectedTopic;
    }

    public void setSelectedTopic(Topic selectedTopic) {
        this.selectedTopic.set(selectedTopic);
    }

    public ObservableList<Section> getSections() {
        return sections;
    }

    public void setSections(ObservableList<Section> sections) {
        this.sections = sections;
    }

    public HashMap<Integer, ObservableList<SubSection>> getSectionsSubSections() {
        return sectionsSubSections;
    }

    public void setSectionsSubSections(HashMap<Integer, ObservableList<SubSection>> sectionsSubSections) {
        this.sectionsSubSections = sectionsSubSections;
    }

    public ObservableList<Topic> getTopics() {
        return topics;
    }

    public HashMap<Integer, ObservableList<Note>> getSectionsNotes() {
        return sectionsNotes;
    }

    public void addNote(SubSection selectedSubSection, String note) {
        for (int i = 0; i < sections.size(); i++) {
            if (sections.get(i).getTopicId() == selectedTopic.get().getId()) {
                ObservableList<Note> noteList = sectionsNotes.get(sections.get(i).getId());

                boolean highlighted = false;

                for (int j = 0; j < noteList.size(); j++) {
                    Note currentNote = noteList.get(j);
                    if (noteList.get(j).getNoteId() == selectedSubSection.getId()) {
                        {
                            NoteDao.updateNote(new Note(currentNote.getId(), currentNote.getNoteTableName(), currentNote.getNoteId(), note));
                            noteList.clear();
                            noteList.addAll(NoteDao.getNotes(subject.getTableName() + "_sub_sections"));
                        }
                        highlighted = true;
                        break;
                    }
                }

                if (!highlighted) {
                    NoteDao.createNote(subject.getTableName() + "_sub_sections", selectedSubSection.getId(), note);
                    noteList.clear();
                    noteList.addAll(NoteDao.getNotes(subject.getTableName() + "_sub_sections"));
                }

                break;
            }
        }
    }
}
