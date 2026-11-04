package com.devtrack.devtrack.enums;

public enum Difficulty {
    EASY("Easy", "success"),
    MEDIUM("Medium", "warning"),
    HARD("Hard", "danger");

    private final String displayName;
    private final String badgeClass;

    Difficulty(String displayName, String badgeClass) {
        this.displayName = displayName;
        this.badgeClass = badgeClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeClass() {
        return badgeClass;
    }
}
