package com.paylink.paylink.utils;

import javafx.scene.control.PasswordField;

public class CustomPasswordField extends PasswordField {
    private char maskChar = '*'; // Default mask character

    public CustomPasswordField() {
        super();
    }

    public void setMaskChar(char maskChar) {
        this.maskChar = maskChar;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, text);
        updateDisplayText();
    }

    @Override
    public void replaceSelection(String text) {
        super.replaceSelection(text);
        updateDisplayText();
    }

    private void updateDisplayText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getLength(); i++) {
            sb.append(maskChar);
        }
        setText(sb.toString());
    }
}
