package com.example.specdriven.tickets;

public enum TransitMode {
    BUS("Bus", "🚌"),
    TRAIN("Train", "🚆"),
    METRO("Metro", "🚇"),
    FERRY("Ferry", "⛴️");

    private final String label;
    private final String emoji;

    TransitMode(String label, String emoji) {
        this.label = label;
        this.emoji = emoji;
    }

    public String getLabel() {
        return label;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getCssClass() {
        return "mode-" + name().toLowerCase();
    }
}
