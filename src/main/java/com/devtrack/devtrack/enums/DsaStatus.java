package com.devtrack.devtrack.enums;

public enum DsaStatus {
    ATTEMPTED("Attempted", "warning"),
    SOLVED("Solved", "success"),
    REVISIT("Revisit", "info");

    private final String displayName;
    private final String badgeClass;

    DsaStatus(String displayName, String badgeClass) {
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
