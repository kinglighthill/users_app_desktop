package com.scholarly.utme.viewmodels.syllabus_screens;

import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.*;
import com.scholarly.utme.data.model.Subject;
import com.scholarly.utme.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class SelectSyllabusVM implements ViewModel {

    private ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private ObservableList<SyllabusSubject> syllabusSubjects = FXCollections.observableArrayList();
    private ObservableList<SyllabusCategory> categories = FXCollections.observableArrayList();
    private HashMap<Integer, ObservableList<SyllabusCategory>> syllabusCategories = new HashMap<>();
    private HashMap<Integer, ObservableList<SyllabusTopic>> syllabusTopics = new HashMap<>();
    private HashMap<Integer, ObservableList<SyllabusSection>> syllabusSections = new HashMap<>();

    private SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SyllabusSubject> selectedSyllabusSubject = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SyllabusCategory> selectedCategory = new SimpleObjectProperty<>();
    private SimpleObjectProperty<SyllabusTopic> selectedTopic = new SimpleObjectProperty<>();

    public SelectSyllabusVM() {
        ObservableList<SyllabusSubject> subjectList = SyllabusSubjectDao.getSyllabusSubjects();

//        subjects.addAll(subjectList);

        syllabusSubjects.addAll(subjectList);

//        this.setSelectedSyllabusSubject(syllabusSubjects.get(0));

        categories.addAll(SyllabusCategoryDao.getCategories());

        syllabusSubjects.forEach(syllabusSubject -> {
            syllabusCategories.put(syllabusSubject.getSubjectId(), SyllabusCategoryDao.getCategories(getCategorySubjectId(syllabusSubject.getSubjectId())));

            syllabusCategories.get(syllabusSubject.getSubjectId()).forEach(syllabusCategory -> {
                syllabusTopics.put(syllabusCategory.getId(), SyllabusTopicDao.getTopicsForCategory(syllabusCategory.getId()));
            });

        });

    }

    private int getCategorySubjectId(int subjectId) {
        return SyllabusSubjectDao.getCategorySubjectId(subjectId);
    }

    public ObservableList<Subject> getSubjects() {
        return subjects;
    }

    public ObservableList<SyllabusSubject> getSyllabusSubjects() {
        return syllabusSubjects;
    }

    public ObservableList<SyllabusCategory> getCategories() {
        return categories;
    }

    public HashMap<Integer, ObservableList<SyllabusCategory>> getSyllabusCategories() {
        return syllabusCategories;
    }

    public HashMap<Integer, ObservableList<SyllabusTopic>> getSyllabusTopics() {
        return syllabusTopics;
    }

    public void setSelectedSubject(Subject selectedSubject) {
        this.selectedSubject.set(selectedSubject);
    }
    public void setSelectedSyllabusSubject(SyllabusSubject selectedSubject) {
        if (selectedSubject != null) {
            syllabusSections.put(selectedSubject.getId(), SyllabusSectionDao.getSections(selectedSubject.getId()));
        }
        this.selectedSyllabusSubject.set(selectedSubject);
    }

    public Subject getSelectedSubject() {
        return selectedSubject.get();
    }

    public SyllabusSubject getSelectedSyllabusSubject() {
        return selectedSyllabusSubject.get();
    }

    public HashMap<Integer, ObservableList<SyllabusSection>> getSyllabusSections() {
        return syllabusSections;
    }

    public SimpleObjectProperty<Subject> selectedSubjectProperty() {
        return selectedSubject;
    }
    public SimpleObjectProperty<SyllabusSubject> selectedSyllabusSubjectProperty() {
        return selectedSyllabusSubject;
    }

    public void setSelectedCategory(SyllabusCategory selectedCategory) {
        this.selectedCategory.set(selectedCategory);
    }

    public SyllabusCategory getSelectedCategory() {
        return selectedCategory.get();
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

}
