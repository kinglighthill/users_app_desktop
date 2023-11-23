package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.data.dao.NovelChapterDao;
import com.scholarly.utme.data.dao.NovelsDao;
import com.scholarly.utme.data.dao.SubjectDao;
import com.scholarly.utme.data.dao.newDb.SectionDao;
import com.scholarly.utme.data.dao.newDb.SubTopicDao;
import com.scholarly.utme.data.dao.newDb.TopicDao;
import com.scholarly.utme.data.model.newDb.*;
import com.scholarly.utme.data.model.novels.NovelChapter;
import com.scholarly.utme.data.model.novels.NovelModel;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenHomeVM implements ViewModel {
    private static final String TAG = "LandingScreenHomeVM: ";
    private final NoteLastSession noteLastSession;
    private NoteSection noteLastSection;
    private NoteSubject lastSectionSubject;
    private NoteTopic selectedNoteTopic;

    private final NovelLastSession novelLastSession;
    private NovelChapter lastSessionChapter;
    private NovelModel lastSessionNovel;
    private ObservableList<NovelChapter> lastSessionChapters;

    private final ObservableList<FavoriteSubject> subjects;

    private ObservableList<FavoriteSubject> favoriteSubjects;

    private final HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    Gson gson = new Gson();

    private UserData userData;
    private final String userId;


    public LandingScreenHomeVM() {
        long startDisplay = System.currentTimeMillis();
        NovelChapterDao novelChapterDao = new NovelChapterDao();
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        System.out.println(TAG + "Got User ID -> " + userId);
        System.out.println(TAG + "Time taken to load NovelChapterDao -> " + (System.currentTimeMillis() - startDisplay) + "ms");

        String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
        System.out.println(TAG + "Got user data string -> " + userDataString);
        userData = gson.fromJson(userDataString, UserData.class);


        favoriteSubjects = FXCollections.observableArrayList();
        subjects = SubjectDao.getFavoriteSubjects();

        ObservableList<SubjectCombination> subjectCombinations = SubjectDao.retrieveSubjectCombination(userId);

        subjects.forEach(subject -> {
            subject.setSelected(false);
            subjectCombinations.forEach(subjectCombination -> {
                if (subject.getSubjectId() == subjectCombination.getSubjectId()) {
                    subject.setSelected(true);
                    favoriteSubjects.add(subject);
                }
            });
        });

        System.out.println(TAG + "Time taken to load Favorite Subjects -> " + (System.currentTimeMillis() - startDisplay) + "ms");

        noteLastSession = SectionDao.retrieveLastSession(userId);

        Task<Void> noteLastSessionTask = new Task<>() {
            @Override
            protected Void call() {
                if (noteLastSession != null) {
                    noteLastSection = SectionDao.getNoteSectionWithId(noteLastSession.getSectionId());
                    assert noteLastSection != null;
                    selectedNoteTopic = TopicDao.getTopic(noteLastSection.getTopicId());
                    lastSectionSubject = SubjectDao.getNoteSubject(noteLastSection.getSubjectId());
                    assert lastSectionSubject != null;
                    noteSubjectTopics.put(lastSectionSubject.getId(), TopicDao.getNoteTopicsForSubject(lastSectionSubject.getId()));
                    TopicDao.getNoteTopicsForSubject(lastSectionSubject.getId()).forEach(topic -> {
                        noteSubTopics.put(topic.getId(), SubTopicDao.getSubTopicsForTopic(topic.getId()));
                    });
                }
                return null;
            }
        };
        Thread noteLastSessionThread = new Thread(noteLastSessionTask);
        noteLastSessionThread.start();

        System.out.println(TAG + "Time taken to load Note Last Session -> " + (System.currentTimeMillis() - startDisplay) + "ms");

        novelLastSession = NovelChapterDao.retrieveLastSession(userId);

        if (novelLastSession != null) {
            lastSessionChapter = novelChapterDao.getNovelChapters().stream().filter(novelChapter ->
                    novelChapter.getId() == novelLastSession.getChapterId()).toList().get(0);
            assert lastSessionChapter != null;
            System.out.println(TAG + "NovelLastSessionChapter -> " + lastSessionChapter);
            lastSessionNovel = NovelsDao.getNovel(lastSessionChapter.getNovelId());
            System.out.println(TAG + "NovelLastSessionNovel -> " + lastSessionNovel);
            lastSessionChapters = novelChapterDao.getNovelChapters().stream().filter(novelChapter ->
                    novelChapter.getNovelId() == lastSessionNovel.getNovel().getId()).collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

//        Task<Void> novelLastSessionTask = new Task<>() {
//            @Override
//            protected Void call() {
//                if (novelLastSession != null) {
//                    lastSessionChapter = novelChapterDao.getNovelChapters().stream().filter(novelChapter ->
//                            novelChapter.getId() == novelLastSession.getChapterId()).toList().get(0);
//                    assert lastSessionChapter != null;
//                    Platform.runLater(() -> {
//                        lastSessionNovel = NovelsDao.getNovel(lastSessionChapter.getNovelId());
//                        lastSessionChapters = novelChapterDao.getNovelChapters().stream().filter(novelChapter ->
//                                novelChapter.getNovelId() == lastSessionNovel.getNovel().getId()).collect(Collectors.toCollection(FXCollections::observableArrayList));
//                    });
//                }
//                return null;
//            }
//        };
//        Thread novelLastSessionThread = new Thread(novelLastSessionTask);
//        novelLastSessionThread.start();
        System.out.println(TAG + "Time taken to load Novel Last Session -> " + (System.currentTimeMillis() - startDisplay) + "ms");
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

    public NoteLastSession getNoteLastSession() {
        return noteLastSession;
    }

    public NovelLastSession getNovelLastSession() {
        return novelLastSession;
    }

    public NoteSection getNoteLastSection() {
        return noteLastSection;
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

    public NovelChapter getLastSessionChapter() {
        return lastSessionChapter;
    }

    public NovelModel getLastSessionNovel() {
        return lastSessionNovel;
    }

    public ObservableList<NovelChapter> getLastSessionChapters() {
        return lastSessionChapters;
    }
}
