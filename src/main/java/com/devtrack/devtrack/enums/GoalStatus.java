package com.devtrack.devtrack.enums;

public enum GoalStatus {
    PENDING("Pending", "warning"),
    IN_PROGRESS("In Progress", "primary"),
    COMPLETED("Completed", "success");

    private final String displayName;
    private final String badgeClass;

    GoalStatus(String displayName, String badgeClass) {
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
