package com.scholarly.utme.ui.utils;

import javafx.scene.text.Font;

public class FontUtil {

    public enum GilroyFontFamily {
        THIN("/fonts/gilroy_thin.otf"),
        LIGHT("/fonts/gilroy_light.otf"),
        REGULAR("/fonts/gilroy_regular.otf"),
        MEDIUM("/fonts/gilroy_medium.otf"),
        MEDIUM_ITALIC("/fonts/gilroy_medium_italic.ttf"),
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

    public enum FontSize {
        TWELVE(12),
        FOURTEEN(14),
        SIXTEEN(16),
        EIGHTEEN(18),
        TWENTY(20),
        TWENTY_TWO(22),
        TWENTY_FOUR(24),
        TWENTY_SIX(26),
        THIRTY(30),
        THIRTY_TWO(32),
        THIRTY_FOUR(34);

        public final int size;

        FontSize(int  size) {
            this.size = size;
        }
    }
}
