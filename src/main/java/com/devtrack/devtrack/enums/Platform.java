package com.devtrack.devtrack.enums;

public enum Platform {
    LEETCODE("LeetCode", "warning"),
    GEEKSFORGEEKS("GeeksforGeeks", "success"),
    HACKERRANK("HackerRank", "info"),
    CODECHEF("CodeChef", "secondary"),
    OTHER("Other", "dark");

    private final String displayName;
    private final String badgeColor;

    Platform(String displayName, String badgeColor) {
        this.displayName = displayName;
        this.badgeColor = badgeColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBadgeColor() {
        return badgeColor;
    }
}
