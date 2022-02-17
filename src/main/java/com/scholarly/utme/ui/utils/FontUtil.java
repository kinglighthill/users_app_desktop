package com.scholarly.utme.ui.utils;

import javafx.scene.text.Font;

public class FontUtil {

    public enum GilroyFontFamily {
        THIN("/fonts/gilroy_thin.otf"),
        LIGHT("/fonts/gilroy_light.otf"),
        REGULAR("/fonts/gilroy_regular.otf"),
        MEDIUM("/fonts/gilroy_medium.otf"),
        SEMI_BOLD("/fonts/gilroy_semi_bold.otf"),
        BOLD("/fonts/gilroy_bold.ttf"),
        EXTRA_BOLD("/fonts/gilroy_extrabold.otf"),
        HEAVY("/fonts/gilroy_heavy.ttf");

        String path;

        GilroyFontFamily(String path) {
            this.path = path;
        }
    }


    public static Font getFont(GilroyFontFamily type, int size) {
        return Font.loadFont(FontUtil.class.getResourceAsStream(type.path), size);
    }
}
