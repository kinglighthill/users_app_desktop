package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.NotesScreenController.InitialData;
import com.scholarly.utme.data.dao.SectionDao;
import com.scholarly.utme.data.dao.SubSectionDao;
import com.scholarly.utme.data.model.Section;
import com.scholarly.utme.data.model.SubSection;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class NotesScreenVM implements ViewModel {

    Subject subject;
    ObservableList<Topic> topics = FXCollections.observableArrayList();
    SimpleObjectProperty<Topic> selectedTopic = new SimpleObjectProperty<>(null);

    ObservableList<Section> sections = FXCollections.emptyObservableList();
    HashMap<Integer, ObservableList<SubSection>> sectionsSubSections = new HashMap<>();

    public NotesScreenVM() {}

    public void initialize(InitialData data) {
        subject = data.getSubject();
        selectedTopic.set(data.getSelectedTopic());
        topics.addAll(data.getTopics());

        sections = SectionDao.getSections(subject.getTableName() + "_sections");


        sections.forEach(section -> {
            sectionsSubSections.put(section.getId(), SubSectionDao.getSubSections(subject.getTableName() + "_sub_sections", section.getId()));
        });
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
}
