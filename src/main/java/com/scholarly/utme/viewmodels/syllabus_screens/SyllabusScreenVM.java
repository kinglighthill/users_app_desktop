package com.scholarly.utme.viewmodels.syllabus_screens;

import com.scholarly.utme.controller.syllabus_screens.SyllabusScreenController.InitialData;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.SyllabusSectionDao;
import com.scholarly.utme.data.dao.newDb.SyllabusSubjectDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SyllabusScreenVM implements ViewModel {
    private SyllabusSubject selectedSubject;
    private SyllabusCategory selectedCategory;
    private SimpleObjectProperty<SyllabusTopic> selectedTopic = new SimpleObjectProperty<>();

    private ObservableList<SyllabusTopic> topics = FXCollections.observableArrayList();

    private ObservableList<SyllabusCategory> syllabusCategories = FXCollections.observableArrayList();

    private HashMap<String, ObservableList<SubTopic>> subtopics = new HashMap<>();

    public HashMap<Integer, ObservableList<SyllabusSection>> getSyllabusSections() {
        return syllabusSections;
    }

    private HashMap<Integer, ObservableList<SyllabusSection>> syllabusSections = new HashMap<>();

    private SyllabusSubject syllabusSubject;

    public SyllabusScreenVM() {

    }

    public void processInitialData(InitialData data) {
        selectedSubject = data.getSubject();
        selectedCategory = data.getCategory();
//        selectedTopic.set(data.getSelectedSyllabusTopic());

        topics.addAll(data.getSyllabusTopics());
        syllabusCategories.addAll(data.getSyllabusCategories());

//        subtopics.put(selectedSubject.getSubjectName(), SubTopicDao.getSubTopics("syllabus_" + selectedSubject.getTableName() + "_sub_topics"));

        syllabusSections.put(selectedSubject.getId(), SyllabusSectionDao.getSections(selectedSubject.getId()));

//        syllabusSubject = SyllabusSubjectDao.getSubjectWithId(selectedSubject.getId());
        syllabusSubject = data.getSubject();

    }

    public SyllabusSubject getSubject() {
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

    public ObservableList<SyllabusCategory> getSyllabusCategories() {
        return syllabusCategories;
    }

    public HashMap<String, ObservableList<SubTopic>> getSubtopics() {
        return subtopics;
    }

    public SyllabusSubject getSyllabusSubject() {
        return syllabusSubject;
    }


}
