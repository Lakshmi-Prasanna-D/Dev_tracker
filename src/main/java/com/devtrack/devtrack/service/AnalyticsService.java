package com.devtrack.devtrack.service;

import com.devtrack.devtrack.dto.AnalyticsDto;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.Difficulty;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.DsaTopic;
import com.devtrack.devtrack.enums.GoalCategory;
import com.devtrack.devtrack.enums.GoalStatus;
import com.devtrack.devtrack.enums.ProjectStatus;
import com.devtrack.devtrack.repository.ActivityRepository;
import com.devtrack.devtrack.repository.DsaProblemRepository;
import com.devtrack.devtrack.repository.GoalRepository;
import com.devtrack.devtrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class AnalyticsService {

    private final GoalRepository goalRepository;
    private final DsaProblemRepository dsaProblemRepository;
    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;
    private final UserService userService;

    public AnalyticsService(GoalRepository goalRepository,
                            DsaProblemRepository dsaProblemRepository,
                            ProjectRepository projectRepository,
                            ActivityRepository activityRepository,
                            UserService userService) {
        this.goalRepository = goalRepository;
        this.dsaProblemRepository = dsaProblemRepository;
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public AnalyticsDto getAnalyticsData() {
        AnalyticsDto dto = new AnalyticsDto();
        User user = currentUser();

        // 1. Goal completion percentage
        long totalGoals = user != null ? goalRepository.countByUser(user) : goalRepository.count();
        long completedGoals = user != null ? goalRepository.countByUserAndStatus(user, GoalStatus.COMPLETED) : goalRepository.countByStatus(GoalStatus.COMPLETED);
        dto.setTotalGoals(totalGoals);
        dto.setCompletedGoals(completedGoals);
        dto.setGoalCompletionPercentage(totalGoals > 0 ? (int) Math.round(((double) completedGoals / totalGoals) * 100) : 0);

        // Goal Category distribution
        Map<String, Long> categoryMap = new LinkedHashMap<>();
        for (GoalCategory cat : GoalCategory.values()) {
            categoryMap.put(cat.getDisplayName(), 0L);
        }
        List<Object[]> catRows = user != null ? goalRepository.countGoalsByCategoryAndUser(user) : goalRepository.countGoalsByCategory();
        for (Object[] r : catRows) {
            if (r[0] != null) {
                GoalCategory gc = (GoalCategory) r[0];
                categoryMap.put(gc.getDisplayName(), ((Number) r[1]).longValue());
            }
        }
        dto.setGoalCategoryDistribution(categoryMap);

        // 2. DSA Problems solved over time (last 14 days or actual dates)
        long totalDsa = user != null ? dsaProblemRepository.countByUser(user) : dsaProblemRepository.count();
        long solvedDsa = user != null ? dsaProblemRepository.countByUserAndStatus(user, DsaStatus.SOLVED) : dsaProblemRepository.countByStatus(DsaStatus.SOLVED);
        dto.setTotalDsaProblems(totalDsa);
        dto.setSolvedDsaProblems(solvedDsa);

        Map<String, Long> dsaTimeline = new LinkedHashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("MMM dd");
        for (int i = 13; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            dsaTimeline.put(d.format(dayFormatter), 0L);
        }
        List<Object[]> timelineRows = user != null ? dsaProblemRepository.countSolvedProblemsOverTimeAndUser(user) : dsaProblemRepository.countSolvedProblemsOverTime();
        for (Object[] row : timelineRows) {
            if (row[0] != null) {
                LocalDate d = (LocalDate) row[0];
                String label = d.format(dayFormatter);
                if (dsaTimeline.containsKey(label)) {
                    dsaTimeline.put(label, ((Number) row[1]).longValue());
                } else if (!d.isBefore(today.minusDays(14))) {
                    dsaTimeline.put(label, ((Number) row[1]).longValue());
                }
            }
        }
        dto.setDsaProblemsSolvedOverTime(dsaTimeline);

        // 3. Topic-wise DSA distribution
        Map<String, Long> topicMap = new LinkedHashMap<>();
        List<Object[]> topicRows = user != null ? dsaProblemRepository.countProblemsByTopicAndUser(user) : dsaProblemRepository.countProblemsByTopic();
        for (Object[] r : topicRows) {
            if (r[0] != null) {
                DsaTopic t = (DsaTopic) r[0];
                topicMap.put(t.getDisplayName(), ((Number) r[1]).longValue());
            }
        }
        if (topicMap.isEmpty()) {
            for (DsaTopic t : DsaTopic.values()) {
                topicMap.put(t.getDisplayName(), 0L);
            }
        }
        dto.setTopicWiseDsaDistribution(topicMap);

        // 4. Difficulty-wise distribution
        Map<String, Long> diffMap = new LinkedHashMap<>();
        for (Difficulty d : Difficulty.values()) {
            diffMap.put(d.getDisplayName(), 0L);
        }
        List<Object[]> diffRows = user != null ? dsaProblemRepository.countProblemsByDifficultyAndUser(user) : dsaProblemRepository.countProblemsByDifficulty();
        for (Object[] r : diffRows) {
            if (r[0] != null) {
                Difficulty d = (Difficulty) r[0];
                diffMap.put(d.getDisplayName(), ((Number) r[1]).longValue());
            }
        }
        dto.setDifficultyWiseDistribution(diffMap);

        // 5. Weekly productivity (activities per day for the last 7 days)
        Map<String, Long> weeklyMap = new LinkedHashMap<>();
        DateTimeFormatter dowFormatter = DateTimeFormatter.ofPattern("EEE (MMM dd)");
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            weeklyMap.put(d.format(dowFormatter), 0L);
        }
        List<Object[]> dailyCounts = user != null ? activityRepository.countActivitiesPerDayByUser(user) : activityRepository.countActivitiesPerDay();
        for (Object[] r : dailyCounts) {
            if (r[0] != null) {
                LocalDate d = (LocalDate) r[0];
                String label = d.format(dowFormatter);
                if (weeklyMap.containsKey(label)) {
                    weeklyMap.put(label, ((Number) r[1]).longValue());
                }
            }
        }
        dto.setWeeklyProductivity(weeklyMap);

        // 6. Monthly productivity (activities per month for the last 6 months)
        Map<String, Long> monthlyMap = new LinkedHashMap<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy");
        for (int i = 5; i >= 0; i--) {
            LocalDate m = today.minusMonths(i);
            monthlyMap.put(m.format(monthFormatter), 0L);
        }
        for (Object[] r : dailyCounts) {
            if (r[0] != null) {
                LocalDate d = (LocalDate) r[0];
                String mLabel = d.format(monthFormatter);
                if (monthlyMap.containsKey(mLabel)) {
                    monthlyMap.put(mLabel, monthlyMap.get(mLabel) + ((Number) r[1]).longValue());
                }
            }
        }
        dto.setMonthlyProductivity(monthlyMap);

        // 7. Project completion statistics
        long totalProj = user != null ? projectRepository.countByUser(user) : projectRepository.count();
        long completedProj = user != null ? projectRepository.countByUserAndStatus(user, ProjectStatus.COMPLETED) : projectRepository.countByStatus(ProjectStatus.COMPLETED);
        dto.setTotalProjects(totalProj);
        dto.setCompletedProjects(completedProj);

        Map<String, Long> projMap = new LinkedHashMap<>();
        for (ProjectStatus s : ProjectStatus.values()) {
            projMap.put(s.getDisplayName(), 0L);
        }
        List<Object[]> projRows = user != null ? projectRepository.countProjectsByStatusGroupAndUser(user) : projectRepository.countProjectsByStatusGroup();
        for (Object[] r : projRows) {
            if (r[0] != null) {
                ProjectStatus s = (ProjectStatus) r[0];
                projMap.put(s.getDisplayName(), ((Number) r[1]).longValue());
            }
        }
        dto.setProjectCompletionStats(projMap);

        // 8. Daily activity heatmap
        Map<String, Long> heatmap = new LinkedHashMap<>();
        DateTimeFormatter ymd = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (Object[] r : dailyCounts) {
            if (r[0] != null) {
                LocalDate d = (LocalDate) r[0];
                heatmap.put(d.format(ymd), ((Number) r[1]).longValue());
            }
        }
        dto.setDailyActivityHeatmap(heatmap);

        return dto;
    }
}
