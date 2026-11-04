package com.devtrack.devtrack.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnalyticsDto {

    private int goalCompletionPercentage;
    private long totalGoals;
    private long completedGoals;

    private long totalDsaProblems;
    private long solvedDsaProblems;

    private long totalProjects;
    private long completedProjects;

    // Charts data: key-value maps
    private Map<String, Long> goalCategoryDistribution = new LinkedHashMap<>();
    private Map<String, Long> dsaProblemsSolvedOverTime = new LinkedHashMap<>();
    private Map<String, Long> topicWiseDsaDistribution = new LinkedHashMap<>();
    private Map<String, Long> difficultyWiseDistribution = new LinkedHashMap<>();
    private Map<String, Long> weeklyProductivity = new LinkedHashMap<>();
    private Map<String, Long> monthlyProductivity = new LinkedHashMap<>();
    private Map<String, Long> projectCompletionStats = new LinkedHashMap<>();
    private Map<String, Long> dailyActivityHeatmap = new LinkedHashMap<>();

    public AnalyticsDto() {
    }

    public int getGoalCompletionPercentage() {
        return goalCompletionPercentage;
    }

    public void setGoalCompletionPercentage(int goalCompletionPercentage) {
        this.goalCompletionPercentage = goalCompletionPercentage;
    }

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

    public long getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(long totalProjects) {
        this.totalProjects = totalProjects;
    }

    public long getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(long completedProjects) {
        this.completedProjects = completedProjects;
    }

    public Map<String, Long> getGoalCategoryDistribution() {
        return goalCategoryDistribution;
    }

    public void setGoalCategoryDistribution(Map<String, Long> goalCategoryDistribution) {
        this.goalCategoryDistribution = goalCategoryDistribution;
    }

    public Map<String, Long> getDsaProblemsSolvedOverTime() {
        return dsaProblemsSolvedOverTime;
    }

    public void setDsaProblemsSolvedOverTime(Map<String, Long> dsaProblemsSolvedOverTime) {
        this.dsaProblemsSolvedOverTime = dsaProblemsSolvedOverTime;
    }

    public Map<String, Long> getTopicWiseDsaDistribution() {
        return topicWiseDsaDistribution;
    }

    public void setTopicWiseDsaDistribution(Map<String, Long> topicWiseDsaDistribution) {
        this.topicWiseDsaDistribution = topicWiseDsaDistribution;
    }

    public Map<String, Long> getDifficultyWiseDistribution() {
        return difficultyWiseDistribution;
    }

    public void setDifficultyWiseDistribution(Map<String, Long> difficultyWiseDistribution) {
        this.difficultyWiseDistribution = difficultyWiseDistribution;
    }

    public Map<String, Long> getWeeklyProductivity() {
        return weeklyProductivity;
    }

    public void setWeeklyProductivity(Map<String, Long> weeklyProductivity) {
        this.weeklyProductivity = weeklyProductivity;
    }

    public Map<String, Long> getMonthlyProductivity() {
        return monthlyProductivity;
    }

    public void setMonthlyProductivity(Map<String, Long> monthlyProductivity) {
        this.monthlyProductivity = monthlyProductivity;
    }

    public Map<String, Long> getProjectCompletionStats() {
        return projectCompletionStats;
    }

    public void setProjectCompletionStats(Map<String, Long> projectCompletionStats) {
        this.projectCompletionStats = projectCompletionStats;
    }

    public Map<String, Long> getDailyActivityHeatmap() {
        return dailyActivityHeatmap;
    }

    public void setDailyActivityHeatmap(Map<String, Long> dailyActivityHeatmap) {
        this.dailyActivityHeatmap = dailyActivityHeatmap;
    }
}
