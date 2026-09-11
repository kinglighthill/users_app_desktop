package com.scholarly.util;

import java.util.prefs.Preferences;

import static com.scholarly.util.EncryptionManager.decrypt;
import static com.scholarly.util.EncryptionManager.encrypt;

public class PreferencesManager {
    private static final String TAG = "New PreferenceManager: ";

    static Preferences preferences = null;

    public static void put(String key, String value) {
        try {
            String encryptedInput = encrypt(value);
            preferences.put(key, encryptedInput);
        } catch (Exception ignored) {
            preferences.put(key, value);
        }
    }

    public static String get(String key, String defaultValue) {
        String decryptedOutput = "";
        try {
            String encryptedOutput = preferences.get(key, defaultValue);
            decryptedOutput = decrypt(encryptedOutput);
        } catch (Exception ignored) {
            decryptedOutput = preferences.get(key, defaultValue);
        }
        return decryptedOutput;
    }

    public static void putBoolean(String key, boolean value) {
        String booleanString = String.valueOf(value);
        try {
            String encryptedInput = encrypt(booleanString);
            preferences.put(key, encryptedInput);
        } catch (Exception ignored) {
            preferences.put(key, booleanString);
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String decryptedOutput = "";
        try {
            String encryptedOutput = preferences.get(key, String.valueOf(defaultValue));
            decryptedOutput = decrypt(encryptedOutput);
        } catch (Exception ignored) {
            decryptedOutput = preferences.get(key, String.valueOf(defaultValue));
        }
        return Boolean.parseBoolean(decryptedOutput);
    }

    public static void putInt(String key, int value) {
        String intString = String.valueOf(value);
        try {
            String encryptedInput = encrypt(intString);
            preferences.put(key, encryptedInput);
        } catch (Exception ignored) {
            preferences.putInt(key, value);
        }
    }

    public static int getInt(String key, int defaultValue) {
        try {
            String encryptedOutput = preferences.get(key, String.valueOf(defaultValue));
            String decryptedOutput = decrypt(encryptedOutput);
            return Integer.parseInt(decryptedOutput);
        } catch (Exception ignored) {
            return preferences.getInt(key, defaultValue);
        }
    }

    public static void initialize() {
        String packageName = "com.scholarly" + AppProperties.getInstance().getApplicationIdSuffix();
        preferences = Preferences.userRoot().node(packageName);
    }

    /*public static void put(String key, String value) {
        preferences.put(key, value);
    }

    public static String get(String key, String defaultValue) {
        return preferences.get(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        preferences.putBoolean(key, value);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }*/

    public static void flush() {
        try {
            preferences.flush();
        } catch (Exception ignored) {

        }
    }
}
