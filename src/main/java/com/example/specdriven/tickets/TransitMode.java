package com.example.specdriven.tickets;

public enum TransitMode {
    BUS("Bus", "🚌", "mode-bus"),
    TRAIN("Train", "🚆", "mode-train"),
    METRO("Metro", "🚇", "mode-metro"),
    FERRY("Ferry", "⛴️", "mode-ferry");

    private final String displayName;
    private final String emoji;
    private final String cssClass;

    TransitMode(String displayName, String emoji, String cssClass) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.cssClass = cssClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getCssClass() {
        return cssClass;
    }
}
