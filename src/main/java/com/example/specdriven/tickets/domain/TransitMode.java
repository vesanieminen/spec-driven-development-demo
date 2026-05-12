package com.example.specdriven.tickets.domain;

public enum TransitMode {
    BUS("Bus", "\uD83D\uDE8C", "qt-mode-bus"),
    TRAIN("Train", "\uD83D\uDE86", "qt-mode-train"),
    METRO("Metro", "\uD83D\uDE87", "qt-mode-metro"),
    FERRY("Ferry", "\u26F4\uFE0F", "qt-mode-ferry");

    private final String label;
    private final String emoji;
    private final String cssClass;

    TransitMode(String label, String emoji, String cssClass) {
        this.label = label;
        this.emoji = emoji;
        this.cssClass = cssClass;
    }

    public String label() {
        return label;
    }

    public String emoji() {
        return emoji;
    }

    public String cssClass() {
        return cssClass;
    }
}

