package com.scholarly.viewmodels.syllabus_screens;

import com.scholarly.data.dao.newDb.*;
import com.scholarly.data.model.Subject;
import com.scholarly.data.model.newDb.*;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SelectSyllabusVM implements ViewModel {

    private final ObservableList<Subject> subjects = FXCollections.observableArrayList();
    private final ObservableList<SyllabusSubject> syllabusSubjects = FXCollections.observableArrayList();
    private final ObservableList<SyllabusCategory> categories = FXCollections.observableArrayList();
    private final HashMap<Integer, ObservableList<SyllabusCategory>> syllabusCategories = new HashMap<>();
    private final HashMap<Integer, ObservableList<SyllabusTopic>> syllabusTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<SyllabusSection>> syllabusSections = new HashMap<>();

    private final SimpleObjectProperty<Subject> selectedSubject = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SyllabusSubject> selectedSyllabusSubject = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SyllabusCategory> selectedCategory = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<SyllabusTopic> selectedTopic = new SimpleObjectProperty<>();

    private final SimpleBooleanProperty subjectsLoaded = new SimpleBooleanProperty();

    public SelectSyllabusVM() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Task<Boolean> subjectsTask = new Task<>() {
            @Override
            protected Boolean call() {
                ObservableList<SyllabusSubject> subjectList = SyllabusSubjectDao.getSyllabusSubjects();
                syllabusSubjects.addAll(subjectList);
                categories.addAll(SyllabusCategoryDao.getCategories());
                syllabusSubjects.forEach(syllabusSubject -> {
                    syllabusCategories.put(syllabusSubject.getSubjectId(), SyllabusCategoryDao.getCategories(getCategorySubjectId(syllabusSubject.getSubjectId())));

                    syllabusCategories.get(syllabusSubject.getSubjectId()).forEach(syllabusCategory -> {
                        syllabusTopics.put(syllabusCategory.getId(), SyllabusTopicDao.getTopicsForCategory(syllabusCategory.getId()));
                    });
                });
                return true;
            }
        };
        subjectsLoaded.bind(subjectsTask.valueProperty());

        executorService.execute(subjectsTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getSubjectsLoaded() {
        return subjectsLoaded;
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
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Task<ObservableList<SyllabusSection>> sectionsTask = new Task<>() {
                @Override
                protected ObservableList<SyllabusSection> call() {
                    return SyllabusSectionDao.getSections(selectedSubject.getId());
                }
            };
            sectionsTask.setOnSucceeded(
                    event -> {
                        syllabusSections.put(selectedSubject.getId(), sectionsTask.valueProperty().getValue());
                        this.selectedSyllabusSubject.set(selectedSubject);
                    }
            );

            executorService.execute(sectionsTask);
            executorService.shutdown();
        } else {
            this.selectedSyllabusSubject.set(null);
        }
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
