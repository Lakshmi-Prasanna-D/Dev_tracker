package com.devtrack.devtrack.enums;

public enum GoalCategory {
    JAVA("Java"),
    DSA("DSA"),
    SQL("SQL"),
    SPRING_BOOT("Spring Boot"),
    FULL_STACK("Full Stack Development"),
    PROJECTS("Projects"),
    OTHER("Other");

    private final String displayName;

    GoalCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
