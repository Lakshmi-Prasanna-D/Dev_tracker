package com.devtrack.devtrack.dto;

import com.devtrack.devtrack.entity.Activity;
import com.devtrack.devtrack.entity.Goal;
import com.devtrack.devtrack.entity.Project;
import com.devtrack.devtrack.entity.Reminder;

import java.util.ArrayList;
import java.util.List;

public class DashboardStatsDto {

    // Goals
    private long totalGoals;
    private long completedGoals;
    private long pendingGoals;
    private long inProgressGoals;
    private int goalCompletionRate;

    // DSA
    private long totalDsaProblems;
    private long solvedDsaProblems;
    private long attemptedDsaProblems;
    private long revisitDsaProblems;

    // Projects
    private long totalProjects;
    private long activeProjects;
    private long completedProjects;
    private long planningProjects;

    // Streaks
    private int currentStreak;
    private int longestStreak;
    private int totalActiveDays;

    // Productivity & Deadlines
    private long weeklyProductivityCount;
    private long upcomingDeadlinesCount;
    private long overdueGoalsCount;
    private long pendingRemindersCount;

    // Lists for Dashboard Widgets
    private List<Activity> recentActivities = new ArrayList<>();
    private List<Goal> upcomingGoals = new ArrayList<>();
    private List<Project> upcomingProjects = new ArrayList<>();
    private List<Reminder> upcomingReminders = new ArrayList<>();

    public DashboardStatsDto() {
    }

    // Getters and Setters

    public long getTotalGoals() {
        return totalGoals;
    }

    public void setTotalGoals(long totalGoals) {
        this.totalGoals = totalGoals;
    }

    public long getCompletedGoals() {
        return completedGoals;
    }

    public void setCompletedGoals(long completedGoals) {
        this.completedGoals = completedGoals;
    }

    public long getPendingGoals() {
        return pendingGoals;
    }

    public void setPendingGoals(long pendingGoals) {
        this.pendingGoals = pendingGoals;
    }

    public long getInProgressGoals() {
        return inProgressGoals;
    }

    public void setInProgressGoals(long inProgressGoals) {
        this.inProgressGoals = inProgressGoals;
    }

    public int getGoalCompletionRate() {
        return goalCompletionRate;
    }

    public void setGoalCompletionRate(int goalCompletionRate) {
        this.goalCompletionRate = goalCompletionRate;
    }

    public long getTotalDsaProblems() {
        return totalDsaProblems;
    }

    public void setTotalDsaProblems(long totalDsaProblems) {
        this.totalDsaProblems = totalDsaProblems;
    }

    public long getSolvedDsaProblems() {
        return solvedDsaProblems;
    }

    public void setSolvedDsaProblems(long solvedDsaProblems) {
        this.solvedDsaProblems = solvedDsaProblems;
    }

    public long getAttemptedDsaProblems() {
        return attemptedDsaProblems;
    }

    public void setAttemptedDsaProblems(long attemptedDsaProblems) {
        this.attemptedDsaProblems = attemptedDsaProblems;
    }

    public long getRevisitDsaProblems() {
        return revisitDsaProblems;
    }

    public void setRevisitDsaProblems(long revisitDsaProblems) {
        this.revisitDsaProblems = revisitDsaProblems;
    }

    public long getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(long totalProjects) {
        this.totalProjects = totalProjects;
    }

    public long getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(long activeProjects) {
        this.activeProjects = activeProjects;
    }

    public long getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(long completedProjects) {
        this.completedProjects = completedProjects;
    }

    public long getPlanningProjects() {
        return planningProjects;
    }

    public void setPlanningProjects(long planningProjects) {
        this.planningProjects = planningProjects;
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

    public long getWeeklyProductivityCount() {
        return weeklyProductivityCount;
    }

    public void setWeeklyProductivityCount(long weeklyProductivityCount) {
        this.weeklyProductivityCount = weeklyProductivityCount;
    }

    public long getUpcomingDeadlinesCount() {
        return upcomingDeadlinesCount;
    }

    public void setUpcomingDeadlinesCount(long upcomingDeadlinesCount) {
        this.upcomingDeadlinesCount = upcomingDeadlinesCount;
    }

    public long getOverdueGoalsCount() {
        return overdueGoalsCount;
    }

    public void setOverdueGoalsCount(long overdueGoalsCount) {
        this.overdueGoalsCount = overdueGoalsCount;
    }

    public long getPendingRemindersCount() {
        return pendingRemindersCount;
    }

    public void setPendingRemindersCount(long pendingRemindersCount) {
        this.pendingRemindersCount = pendingRemindersCount;
    }

    public List<Activity> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<Activity> recentActivities) {
        this.recentActivities = recentActivities;
    }

    public List<Goal> getUpcomingGoals() {
        return upcomingGoals;
    }

    public void setUpcomingGoals(List<Goal> upcomingGoals) {
        this.upcomingGoals = upcomingGoals;
    }

    public List<Project> getUpcomingProjects() {
        return upcomingProjects;
    }

    public void setUpcomingProjects(List<Project> upcomingProjects) {
        this.upcomingProjects = upcomingProjects;
    }

    public List<Reminder> getUpcomingReminders() {
        return upcomingReminders;
    }

    public void setUpcomingReminders(List<Reminder> upcomingReminders) {
        this.upcomingReminders = upcomingReminders;
    }
}
