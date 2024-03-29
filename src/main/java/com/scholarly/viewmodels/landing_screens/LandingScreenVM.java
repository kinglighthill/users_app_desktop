package com.scholarly.viewmodels.landing_screens;

import com.scholarly.controller.landing_screens.LandingScreenController;
import com.scholarly.ui.utils.Screens;
import com.scholarly.util.PreferencesManager;
import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import static com.scholarly.util.Constants.PREF_KEY_ACTIVATION_STATE;
import static com.scholarly.util.Constants.PREF_KEY_USER_ID;

public class LandingScreenVM implements ViewModel {
    private static final String TAG = "LandingScreenVM: ";

    private final String userId;
    private final boolean activated;

    private final SimpleStringProperty screenProperty = new SimpleStringProperty();

    private final SimpleObjectProperty<Screens> currentScreen = new SimpleObjectProperty<>(null);

    public LandingScreenVM() {
        userId = PreferencesManager.get(PREF_KEY_USER_ID, "");
        System.out.println(TAG + "User ID -> " + userId);
        activated = PreferencesManager.getBoolean(PREF_KEY_ACTIVATION_STATE+userId, false);
    }

    public void processInitialData(LandingScreenController.InitialData data) {
        if (data != null) {
            currentScreen.set(data.screenToShow());
        }
    }

//    public void refreshState()

    public String getUserId() {
        return userId;
    }

    public boolean isActivated() {
        return activated;
    }

    public Screens getScreenToShow() {
        return currentScreen.get();
    }

    public String getSelectedScreen() {
        return screenProperty.get();
    }
}
