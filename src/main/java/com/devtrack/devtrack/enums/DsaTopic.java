package com.devtrack.devtrack.enums;

public enum DsaTopic {
    ARRAYS("Arrays"),
    STRINGS("Strings"),
    LINKED_LISTS("Linked Lists"),
    STACK("Stack"),
    QUEUE("Queue"),
    HASHING("Hashing"),
    BINARY_SEARCH("Binary Search"),
    TWO_POINTERS("Two Pointers"),
    SLIDING_WINDOW("Sliding Window"),
    TREES("Trees"),
    GRAPHS("Graphs"),
    GREEDY("Greedy"),
    DYNAMIC_PROGRAMMING("Dynamic Programming"),
    RECURSION("Recursion"),
    BACKTRACKING("Backtracking"),
    OTHER("Other");

    private final String displayName;

    DsaTopic(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
