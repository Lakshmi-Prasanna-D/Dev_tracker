package com.devtrack.devtrack.dto;

import com.devtrack.devtrack.entity.Activity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreakStatsDto {

    private int currentStreak;
    private int longestStreak;
    private int totalActiveDays;
    private LocalDate lastActiveDate;
    private boolean activeToday;

    // Date to activity count for calendar / GitHub heatmap
    private Map<String, Long> activityDateMap = new HashMap<>();
    private List<Activity> recentActivities = new ArrayList<>();

    public StreakStatsDto() {
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public int getTotalActiveDays() {
        return totalActiveDays;
    }

    public void setTotalActiveDays(int totalActiveDays) {
        this.totalActiveDays = totalActiveDays;
    }

    public LocalDate getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(LocalDate lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public boolean isActiveToday() {
        return activeToday;
    }

    public void setActiveToday(boolean activeToday) {
        this.activeToday = activeToday;
    }

    public Map<String, Long> getActivityDateMap() {
        return activityDateMap;
    }

    public void setActivityDateMap(Map<String, Long> activityDateMap) {
        this.activityDateMap = activityDateMap;
    }

    public List<Activity> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<Activity> recentActivities) {
        this.recentActivities = recentActivities;
    }
}
