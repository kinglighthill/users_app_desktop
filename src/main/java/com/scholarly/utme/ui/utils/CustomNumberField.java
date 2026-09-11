package com.scholarly.utme.ui.utils;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CustomNumberField extends TextField {

    public CustomNumberField() {
        this.addEventFilter(KeyEvent.KEY_TYPED, keyEvent -> {
            char[] ar = keyEvent.getCharacter().toCharArray();
            char ch = ar[keyEvent.getCharacter().toCharArray().length - 1];
            if (!(ch >= '0' && ch <= '9')) {
                setPromptText("Please enter a valid number");
                System.out.println("The char you entered is not a number");
                keyEvent.consume();
            }
        });
    }
}
