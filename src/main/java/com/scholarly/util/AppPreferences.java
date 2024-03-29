package com.scholarly.util;

import java.util.prefs.Preferences;

public class AppPreferences {

    static Preferences userPreferences = Preferences.userRoot().node("com.scholarly.utme");


    public static Preferences getPreferences(){
        return userPreferences;
    }
}
