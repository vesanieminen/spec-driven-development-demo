package com.example.specdriven.domain;

public enum TransitMode {
    BUS("🚌", "#f59e0b", "bus"),
    TRAIN("🚆", "#fb923c", "train"),
    METRO("🚇", "#fbbf24", "metro"),
    FERRY("⛴️", "#a3e635", "ferry");

    public final String emoji;
    public final String color;
    public final String cssClass;

    TransitMode(String emoji, String color, String cssClass) {
        this.emoji = emoji;
        this.color = color;
        this.cssClass = cssClass;
    }

    public String displayName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
