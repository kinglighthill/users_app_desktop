package com.scholarly.utme.viewmodels.landing_screens;

import com.google.gson.Gson;
import com.scholarly.utme.network.model.UserData;
import com.scholarly.utme.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.scholarly.utme.util.Constants.PREF_KEY_USER_DATA;
import static com.scholarly.utme.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenAccountVM implements ViewModel {
    private static final String TAG = "LandingScreenAccountVM: ";
    Gson gson = new Gson();

    private String userId;
    private UserData userData;

    private final SimpleBooleanProperty uidLoaded = new SimpleBooleanProperty();

    public LandingScreenAccountVM() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

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

        executorService.execute(uidTask);
        executorService.shutdown();
    }

    public SimpleBooleanProperty getUidLoaded() {
        return uidLoaded;
    }

    public String getUserId() {
        return userId;
    }

    public UserData getUser() {
        return userData;
    }

}
