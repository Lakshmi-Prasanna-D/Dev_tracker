package com.devtrack.devtrack.service;

import com.devtrack.devtrack.dto.DashboardStatsDto;
import com.devtrack.devtrack.dto.StreakStatsDto;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.GoalStatus;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.ProjectStatus;
import com.devtrack.devtrack.enums.ReminderStatus;
import com.devtrack.devtrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final GoalRepository goalRepository;
    private final DsaProblemRepository dsaProblemRepository;
    private final ProjectRepository projectRepository;
    private final ActivityRepository activityRepository;
    private final ReminderRepository reminderRepository;
    private final StreakService streakService;
    private final UserService userService;

    public DashboardService(GoalRepository goalRepository,
                            DsaProblemRepository dsaProblemRepository,
                            ProjectRepository projectRepository,
                            ActivityRepository activityRepository,
                            ReminderRepository reminderRepository,
                            StreakService streakService,
                            UserService userService) {
        this.goalRepository = goalRepository;
        this.dsaProblemRepository = dsaProblemRepository;
        this.projectRepository = projectRepository;
        this.activityRepository = activityRepository;
        this.reminderRepository = reminderRepository;
        this.streakService = streakService;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public DashboardStatsDto getDashboardStatistics() {
        DashboardStatsDto dto = new DashboardStatsDto();
        User user = currentUser();

        // 1. Goal Stats
        long totalGoals = user != null ? goalRepository.countByUser(user) : goalRepository.count();
        long completedGoals = user != null ? goalRepository.countByUserAndStatus(user, GoalStatus.COMPLETED) : goalRepository.countByStatus(GoalStatus.COMPLETED);
        long pendingGoals = user != null ? goalRepository.countByUserAndStatus(user, GoalStatus.PENDING) : goalRepository.countByStatus(GoalStatus.PENDING);
        long inProgressGoals = user != null ? goalRepository.countByUserAndStatus(user, GoalStatus.IN_PROGRESS) : goalRepository.countByStatus(GoalStatus.IN_PROGRESS);

        dto.setTotalGoals(totalGoals);
        dto.setCompletedGoals(completedGoals);
        dto.setPendingGoals(pendingGoals);
        dto.setInProgressGoals(inProgressGoals);
        int goalCompletionRate = totalGoals > 0 ? (int) Math.round(((double) completedGoals / totalGoals) * 100) : 0;
        dto.setGoalCompletionRate(goalCompletionRate);

        // 2. DSA Stats
        dto.setTotalDsaProblems(user != null ? dsaProblemRepository.countByUser(user) : dsaProblemRepository.count());
        dto.setSolvedDsaProblems(user != null ? dsaProblemRepository.countByUserAndStatus(user, DsaStatus.SOLVED) : dsaProblemRepository.countByStatus(DsaStatus.SOLVED));
        dto.setAttemptedDsaProblems(user != null ? dsaProblemRepository.countByUserAndStatus(user, DsaStatus.ATTEMPTED) : dsaProblemRepository.countByStatus(DsaStatus.ATTEMPTED));
        dto.setRevisitDsaProblems(user != null ? dsaProblemRepository.countByUserAndStatus(user, DsaStatus.REVISIT) : dsaProblemRepository.countByStatus(DsaStatus.REVISIT));

        // 3. Project Stats
        dto.setTotalProjects(user != null ? projectRepository.countByUser(user) : projectRepository.count());
        dto.setActiveProjects(user != null ? projectRepository.countByUserAndStatus(user, ProjectStatus.IN_PROGRESS) : projectRepository.countByStatus(ProjectStatus.IN_PROGRESS));
        dto.setCompletedProjects(user != null ? projectRepository.countByUserAndStatus(user, ProjectStatus.COMPLETED) : projectRepository.countByStatus(ProjectStatus.COMPLETED));
        dto.setPlanningProjects(user != null ? projectRepository.countByUserAndStatus(user, ProjectStatus.PLANNING) : projectRepository.countByStatus(ProjectStatus.PLANNING));

        // 4. Streaks
        StreakStatsDto streakStats = streakService.calculateStreakStats();
        dto.setCurrentStreak(streakStats.getCurrentStreak());
        dto.setLongestStreak(streakStats.getLongestStreak());
        dto.setTotalActiveDays(streakStats.getTotalActiveDays());

        // 5. Weekly Productivity (activities in the last 7 days)
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        long weeklyCount = user != null ? activityRepository.countByUserAndTimestampBetween(user, sevenDaysAgo, LocalDateTime.now())
                                        : activityRepository.countByTimestampBetween(sevenDaysAgo, LocalDateTime.now());
        dto.setWeeklyProductivityCount(weeklyCount);

        // 6. Upcoming Deadlines & Overdue
        LocalDate today = LocalDate.now();
        var upcomingGoals = user != null ? goalRepository.findUpcomingGoalsByUser(user, today) : goalRepository.findUpcomingGoals(today);
        var upcomingProjects = user != null ? projectRepository.findUpcomingProjectsByUser(user, today) : projectRepository.findUpcomingProjects(today);
        var upcomingReminders = user != null ? reminderRepository.findUpcomingRemindersByUser(user, LocalDateTime.now()) : reminderRepository.findUpcomingReminders(LocalDateTime.now());
        var overdueGoals = user != null ? goalRepository.findOverdueGoalsByUser(user, today) : goalRepository.findOverdueGoals(today);

        dto.setUpcomingDeadlinesCount(upcomingGoals.size() + upcomingProjects.size() + upcomingReminders.size());
        dto.setOverdueGoalsCount(overdueGoals.size());
        dto.setPendingRemindersCount(user != null ? reminderRepository.countByUserAndStatus(user, ReminderStatus.PENDING) : reminderRepository.countByStatus(ReminderStatus.PENDING));

        // 7. Recent Items
        dto.setRecentActivities(user != null ? activityRepository.findTop10ByUserOrderByTimestampDesc(user) : activityRepository.findTop10ByOrderByTimestampDesc());
        dto.setUpcomingGoals(upcomingGoals.stream().limit(5).toList());
        dto.setUpcomingProjects(upcomingProjects.stream().limit(5).toList());
        dto.setUpcomingReminders(upcomingReminders.stream().limit(5).toList());

        return dto;
    }
}
