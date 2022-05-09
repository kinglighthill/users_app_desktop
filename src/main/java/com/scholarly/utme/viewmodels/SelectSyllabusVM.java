package com.scholarly.utme.viewmodels;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.SyllabusCategoryDao;
import com.scholarly.utme.data.dao.newDb.SyllabusTopicDao;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.SubTopic;
import com.scholarly.utme.data.model.newDb.SyllabusCategory;
import com.scholarly.utme.data.model.newDb.SyllabusTopic;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SelectSyllabusVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private HashMap<String, ObservableList<SyllabusCategory>> categories = new HashMap<>();
    private HashMap<String, ObservableList<SyllabusTopic>> syllabusTopics = new HashMap<>();
    private HashMap<String, ObservableList<SubTopic>> syllabusSubTopics = new HashMap<>();

    private SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SyllabusCategory> selectedCategory = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SyllabusTopic> selectedTopic = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SubTopic> selectedSubTopic = new SimpleObjectProperty<>();

    public SelectSyllabusVM() {
        ObservableList<Subject> subjectList = SubjectDao.getSubjects();

        subjects.addAll(subjectList);

        subjects.forEach(subject -> {
            categories.put(subject.getSubjectName(), SyllabusCategoryDao.getCategories("syllabus_" + subject.getTableName() + "_categories"));

            syllabusTopics.put(subject.getSubjectName(), SyllabusTopicDao.getSyllabusTopics("syllabus_" + subject.getTableName() + "_topics"));

            syllabusSubTopics.put(subject.getSubjectName(), SubTopicDao.getSubTopics("syllabus_" + subject.getTableName() + "_sub_topics"));

        });

    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public HashMap<String, ObservableList<SyllabusCategory>> getCategories() {
        return categories;
    }

    public HashMap<String, ObservableList<SyllabusTopic>> getSyllabusTopics() {
        return syllabusTopics;
    }

    public HashMap<String, ObservableList<SubTopic>> getSyllabusSubTopics() {
        return syllabusSubTopics;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public SimpleObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }

    public void setSelectedCategory(SyllabusCategory selectedCategory) {
        this.selectedCategory.set(selectedCategory);
    }

    public SimpleObjectProperty<SyllabusCategory> selectedCategoryProperty() {
        return selectedCategory;
    }

    public void setSelectedTopic(SyllabusTopic topic) {
        this.selectedTopic.set(topic);
    }

    public SyllabusTopic getSelectedTopic() {
        return selectedTopic.get();
    }

    public SimpleObjectProperty<SyllabusTopic> selectedTopicProperty() {
        return selectedTopic;
    }

    public void setSelectedSubTopic(SubTopic subTopic) {
        this.selectedSubTopic.set(subTopic);
    }

    public SimpleObjectProperty<SubTopic> selectedSubTopicProperty() {
        return selectedSubTopic;
    }
}
