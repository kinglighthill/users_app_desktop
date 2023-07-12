package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.TopicDao;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.network.model.User;
import com.scholarly.utme.util.AppPreferences;
import com.scholarly.utme.util.Constants;
import com.scholarly.utme.util.Helper;
import de.saxsys.mvvmfx.ViewModel;
import javafx.collections.ObservableList;

import java.lang.constant.Constable;
import java.util.HashMap;
import java.util.Optional;
import java.util.prefs.Preferences;

public class LandingScreenHomeVM implements ViewModel {
    private static final String TAG = "LandingScreenHomeVM: ";
    private final Preferences preferences = AppPreferences.getPreferences();
    private final NoteLastSection noteLastSection;
    private NoteSection noteSection;
    private NoteSubject lastSectionSubject;
    private NoteTopic selectedNoteTopic;

    private final HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    private final User user;


    public LandingScreenHomeVM() {
        String userData = preferences.get(Constants.PREF_KEY_USER_DATA, "");
        Gson gson = new Gson();
        user = gson.fromJson(userData, User.class);

        noteLastSection = SectionDao.retrieveLastSection(user.getId());
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

    public NoteLastSection getLastSession() {
        return noteLastSection;
    }

    public NoteSection getNoteSection() {
        return noteSection;
    }

    public NoteTopic getSelectedNoteTopic() {
        return selectedNoteTopic;
    }

    public NoteSubject getLastSessionSubject() {
//        NoteSection section = SectionDao.getNoteSectionWithId(sectionId);
//        System.out.println(TAG + "Got NoteSection with Id -> " + section.getId());
//        System.out.println(TAG + "Got NoteSection with subjectId -> " + section.getSubjectId());
//        NoteSubject subject = SubjectDao.getNoteSubject(section.getSubjectId());
//        System.out.println(TAG + "Got NoteSubject -> " + Helper.toString(subject));
        return lastSectionSubject;
    }

    public HashMap<Integer, ObservableList<NoteTopic>> getNoteSubjectTopics() {
        return noteSubjectTopics;
    }

    public HashMap<Integer, ObservableList<NoteSubTopic>> getNoteSubTopics() {
        return noteSubTopics;
    }

    public User getUser() {
        return user;
    }
}
