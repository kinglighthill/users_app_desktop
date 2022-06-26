package com.scholarly.utme.viewmodels;

import com.scholarly.utme.controller.SyllabusScreenController.InitialData;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.SyllabusSubjectDao;
import com.scholarly.utme.data.dao.newDb.SyllabusTopicDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.Topic;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusSubject;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import de.saxsys.mvvmfx.FxmlPath;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SyllabusScreenVM implements ViewModel {
    private Subject selectedSubject;
    private SyllabusCategory selectedCategory;
    private SimpleObjectProperty<SyllabusTopic> selectedTopic = new SimpleObjectProperty<>();

    private ObservableList<SyllabusTopic> topics = FXCollections.observableArrayList();

    private HashMap<String, ObservableList<SubTopic>> subtopics = new HashMap<>();

    private SyllabusSubject syllabusSubject;

    public SyllabusScreenVM() {

    }

    public void processInitialData(InitialData data) {
        selectedSubject = data.getSubject();
        selectedCategory = data.getCategory();
        selectedTopic.set(data.getSelectedSyllabusTopic());

        topics.addAll(data.getSyllabusTopics());

        subtopics.put(selectedSubject.getSubjectName(), SubTopicDao.getSubTopics("syllabus_" + selectedSubject.getTableName() + "_sub_topics"));

        syllabusSubject = SyllabusSubjectDao.getSubjectWithId(selectedSubject.getId());

    }

    public Subject getSubject() {
        return selectedSubject;
    }

    public SyllabusCategory getSelectedCategory() {
        return selectedCategory;
    }

    public SyllabusTopic getSelectedTopic() {
        return selectedTopic.get();
    }

    public void setSelectedTopic(SyllabusTopic selectedTopic) {
        this.selectedTopic.set(selectedTopic);
    }

    public SimpleObjectProperty<SyllabusTopic> selectedTopicProperty() {
        return selectedTopic;
    }

    public ObservableList<SyllabusTopic> getTopics() {
        return topics;
    }

    public HashMap<String, ObservableList<SubTopic>> getSubtopics() {
        return subtopics;
    }

    public SyllabusSubject getSyllabusSubject() {
        return syllabusSubject;
    }
}
