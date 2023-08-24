package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.TopicDao;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenHomeVM implements ViewModel {
    private static final String TAG = "LandingScreenHomeVM: ";
    private final Preferences preferences = AppPreferences.getPreferences();
    private final NoteLastSection noteLastSection;
    private NoteSection noteSection;
    private NoteSubject lastSectionSubject;
    private NoteTopic selectedNoteTopic;

    private final ObservableList<FavoriteSubject> subjects;

    private ObservableList<FavoriteSubject> favoriteSubjects;

    private final HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    Gson gson = new Gson();

    private final UserData userData;
    private final String userId;


    public LandingScreenHomeVM() {
        userId = preferences.get(PREF_KEY_USER_ID, "");

        String userDataString = preferences.get(PREF_KEY_USER_DATA+userId, "");
        userData = gson.fromJson(userDataString, UserData.class);

        favoriteSubjects = FXCollections.observableArrayList();
        subjects = SubjectDao.getFavoriteSubjects();

        ObservableList<SubjectCombination> subjectCombinations = SubjectDao.retrieveSubjectCombination(userData.getId());

        subjects.forEach(subject -> {
            subject.setSelected(false);
            subjectCombinations.forEach(subjectCombination -> {
                if (subject.getSubjectId() == subjectCombination.getSubjectId()) {
                    subject.setSelected(true);
                    favoriteSubjects.add(subject);
                }
            });
        });

        noteLastSection = SectionDao.retrieveLastSection(userData.getId());
        if (noteLastSection != null) {
            noteSection = SectionDao.getNoteSectionWithId(noteLastSection.getSectionId());
            assert noteSection != null;
            selectedNoteTopic = TopicDao.getTopic(noteSection.getTopicId());
            lastSectionSubject = SubjectDao.getNoteSubject(noteSection.getSubjectId());
            assert lastSectionSubject != null;
            noteSubjectTopics.put(lastSectionSubject.getId(), TopicDao.getNoteTopicsForSubject(lastSectionSubject.getId()));
            TopicDao.getNoteTopicsForSubject(lastSectionSubject.getId()).forEach(topic -> {
                noteSubTopics.put(topic.getId(), SubTopicDao.getSubTopicsForTopic(topic.getId()));
            });
        }
    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return userData;
    }

    public ObservableList<FavoriteSubject> getFavoriteSubjects() {
        return favoriteSubjects;
    }

    public void setFavoriteSubjects(ObservableList<FavoriteSubject> favoriteSubjects) {
        this.favoriteSubjects = favoriteSubjects;
    }

    public NoteLastSection getLastSession() {
        return noteLastSection;
    }

    public NoteSection getNoteSection() {
        return noteSection;
    }

    public NoteTopic getSelectedNoteTopic() {
        return selectedNoteTopic;
    }

    public ObservableList<FavoriteSubject> getSubjects() {
        return subjects;
    }

    public NoteSubject getLastSessionSubject() {
//        NoteSection section = SectionDao.getNoteSectionWithId(sectionId);
//        System.out.println(TAG + "Got NoteSection with Id -> " + section.getId());
//        System.out.println(TAG + "Got NoteSection with subjectId -> " + section.getSubjectId());
//        NoteSubject subject = SubjectDao.getNoteSubject(section.getSubjectId());
//        System.out.println(TAG + "Got NoteSubject -> " + Helper.toString(subject));
        return lastSectionSubject;
    }

    public void putSubjectCombination(ObservableList<FavoriteSubject> subjects) {
        List<SubjectCombination> subjectCombinationList = subjects.stream().map(objectiveSubject -> new SubjectCombination(objectiveSubject.getId(), objectiveSubject.getSubjectId(), userData.getId())).toList();
        SubjectDao.deletePreviousSubjectCombination(userData.getId());
        subjectCombinationList.forEach(SubjectDao::insertSubjectCombination);
    }
    public ObservableList<ObjectiveSubject> getSubjectCombination() {
        List<SubjectCombination> subjectCombinationList = SubjectDao.retrieveSubjectCombination(userData.getId());
        ObservableList<ObjectiveSubject> favSubjects = FXCollections.observableArrayList();
        subjects.forEach(subject -> {
            subjectCombinationList.forEach(subjectCombination -> {
                if (subject.getSubjectId() == subjectCombination.getSubjectId()) {
                    favSubjects.add(subject);
                }
            });
        });
        return favSubjects;
    }

    public HashMap<Integer, ObservableList<NoteTopic>> getNoteSubjectTopics() {
        return noteSubjectTopics;
    }

    public HashMap<Integer, ObservableList<NoteSubTopic>> getNoteSubTopics() {
        return noteSubTopics;
    }
}
