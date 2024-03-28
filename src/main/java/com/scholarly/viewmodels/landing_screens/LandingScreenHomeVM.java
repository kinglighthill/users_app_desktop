package com.scholarly.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.data.dao.NovelChapterDao;
import com.scholarly.data.dao.NovelsDao;
import com.scholarly.data.dao.SubjectDao;
import com.scholarly.data.dao.newDb.SectionDao;
import com.scholarly.data.dao.newDb.SubTopicDao;
import com.scholarly.data.dao.newDb.TopicDao;
import com.scholarly.data.model.newDb.*;
import com.scholarly.data.model.novels.NovelChapter;
import com.scholarly.data.model.novels.NovelModel;
import com.scholarly.network.model.UserData;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.scholarly.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenHomeVM implements ViewModel {
    private static final String TAG = "LandingScreenHomeVM: ";
    private NoteLastSession noteLastSession;
    private NoteSection noteLastSection;
    private NoteSubject lastSectionSubject;
    private NoteTopic selectedNoteTopic;

    private NovelLastSession novelLastSession;
    private NovelChapter lastSessionChapter;
    private NovelModel lastSessionNovel;
    private ObservableList<NovelChapter> lastSessionChapters;

    private ObservableList<FavoriteSubject> subjects;

    private ObservableList<FavoriteSubject> favoriteSubjects;

    private final HashMap<Integer, ObservableList<NoteTopic>> noteSubjectTopics = new HashMap<>();
    private final HashMap<Integer, ObservableList<NoteSubTopic>> noteSubTopics = new HashMap<>();

    Gson gson = new Gson();

    private UserData userData;
    private String userId;

    private final SimpleBooleanProperty uidLoaded = new SimpleBooleanProperty();
    private final SimpleBooleanProperty subjectLoaded = new SimpleBooleanProperty();
    private final SimpleBooleanProperty lastSessionLoaded = new SimpleBooleanProperty();

    public LandingScreenHomeVM() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Task<Boolean> uidTask = new Task<>() {
            @Override
            protected Boolean call() {
                userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
                String userDataString = PreferencesManager.get(PREF_KEY_USER_DATA+userId, "");
                userData = gson.fromJson(userDataString, UserData.class);
                return true;
            }
        };
        uidLoaded.bind(uidTask.valueProperty());

        Task<Boolean> subjectTask = new Task<>() {
            @Override
            protected Boolean call() {
                favoriteSubjects = FXCollections.observableArrayList();
                // TODO: Based on comment in SubjectDao, this method should change to return either ObjectiveSubjects or TheorySubjects directly and then remodelled to FavoriteSubject based on isFavorite field
                subjects = SubjectDao.getObjFavoriteSubjects();

                ObservableList<SubjectCombination> subjectCombinations = SubjectDao.retrieveSubjectCombination(userId);

                subjects.forEach(subject -> {
                    subject.setSelected(false);
                    subjectCombinations.forEach(subjectCombination -> {
                        if (subject.getSubjectId() == subjectCombination.getSubjectId()) {
                            // TODO: When the preceding comments have been implemented there will be no need for having 'subjects' and 'favoriteSubjects', can be named any of each then returned in its getter
                            subject.setSelected(true);
                            favoriteSubjects.add(subject);
                        }
                    });
                });
                return true;
            }
        };
        subjectLoaded.bind(subjectTask.valueProperty());

        Task<Boolean> lastSessionTask = new Task<>() {
            @Override
            protected Boolean call() {
                noteLastSession = SectionDao.retrieveLastSession(userId);
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

                NovelChapterDao novelChapterDao = new NovelChapterDao();
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

                return true;
            }
        };
        lastSessionLoaded.bind(lastSessionTask.valueProperty());

        executorService.submit(uidTask).get();
        executorService.execute(subjectTask);
        executorService.execute(lastSessionTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getUidLoaded() {
        return uidLoaded;
    }

    public SimpleBooleanProperty getSubjectLoaded() {
        return subjectLoaded;
    }

    public SimpleBooleanProperty getLastSessionLoaded() {
        return lastSessionLoaded;
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
