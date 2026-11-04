package com.devtrack.devtrack.enums;

public enum ReminderStatus {
    PENDING("Pending", "warning"),
    COMPLETED("Completed", "success"),
    DISMISSED("Dismissed", "secondary");

    private final String displayName;
    private final String badgeClass;

    ReminderStatus(String displayName, String badgeClass) {
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
