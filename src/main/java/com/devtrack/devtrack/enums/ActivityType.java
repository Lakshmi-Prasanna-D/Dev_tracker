package com.devtrack.devtrack.enums;

public enum ActivityType {
    GOAL_CREATED("Goal Created", "bi-bullseye", "primary"),
    GOAL_COMPLETED("Goal Completed", "bi-check-circle-fill", "success"),
    DSA_ATTEMPTED("DSA Problem Attempted", "bi-code-square", "warning"),
    DSA_SOLVED("DSA Problem Solved", "bi-patch-check-fill", "success"),
    PROJECT_CREATED("Project Created", "bi-folder-plus", "info"),
    PROJECT_UPDATED("Project Updated", "bi-pencil-square", "secondary"),
    PROJECT_COMPLETED("Project Completed", "bi-trophy-fill", "success"),
    MILESTONE_COMPLETED("Milestone Completed", "bi-flag-fill", "success");

    private final String displayName;
    private final String iconClass;
    private final String colorClass;

    ActivityType(String displayName, String iconClass, String colorClass) {
        this.displayName = displayName;
        this.iconClass = iconClass;
        this.colorClass = colorClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getColorClass() {
        return colorClass;
    }
}
