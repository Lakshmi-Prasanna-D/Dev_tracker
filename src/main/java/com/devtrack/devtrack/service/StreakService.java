package com.devtrack.devtrack.service;

import com.devtrack.devtrack.dto.StreakStatsDto;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.repository.ActivityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class StreakService {

    private final ActivityRepository activityRepository;
    private final UserService userService;

    public StreakService(ActivityRepository activityRepository, UserService userService) {
        this.activityRepository = activityRepository;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public StreakStatsDto calculateStreakStats() {
        StreakStatsDto dto = new StreakStatsDto();
        User user = currentUser();

        List<LocalDate> rawDates = user != null ? activityRepository.findDistinctActivityDatesByUser(user)
                                                : activityRepository.findDistinctActivityDates();
        TreeSet<LocalDate> activeDates = new TreeSet<>(rawDates);

        dto.setTotalActiveDays(activeDates.size());

        if (activeDates.isEmpty()) {
            dto.setCurrentStreak(0);
            dto.setLongestStreak(0);
            dto.setActiveToday(false);
            dto.setLastActiveDate(null);
            dto.setRecentActivities(Collections.emptyList());
            dto.setActivityDateMap(Collections.emptyMap());
            return dto;
        }

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        boolean activeToday = activeDates.contains(today);
        dto.setActiveToday(activeToday);
        dto.setLastActiveDate(activeDates.last());

        // 1. Current streak calculation
        int currentStreak = 0;
        LocalDate cursor;
        if (activeToday) {
            cursor = today;
            while (activeDates.contains(cursor)) {
                currentStreak++;
                cursor = cursor.minusDays(1);
            }
        } else if (activeDates.contains(yesterday)) {
            cursor = yesterday;
            while (activeDates.contains(cursor)) {
                currentStreak++;
                cursor = cursor.minusDays(1);
            }
        } else {
            currentStreak = 0;
        }
        dto.setCurrentStreak(currentStreak);

        // 2. Longest streak calculation
        int longestStreak = 0;
        int runningStreak = 0;
        LocalDate prev = null;

        for (LocalDate date : activeDates) {
            if (prev == null) {
                runningStreak = 1;
            } else if (date.equals(prev.plusDays(1))) {
                runningStreak++;
            } else {
                runningStreak = 1;
            }
            if (runningStreak > longestStreak) {
                longestStreak = runningStreak;
            }
            prev = date;
        }
        dto.setLongestStreak(longestStreak);

        // 3. Activity counts per day for heatmap
        List<Object[]> counts = user != null ? activityRepository.countActivitiesPerDayByUser(user)
                                             : activityRepository.countActivitiesPerDay();
        Map<String, Long> dateMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Object[] row : counts) {
            if (row[0] != null && row[1] != null) {
                LocalDate date = (LocalDate) row[0];
                Long count = ((Number) row[1]).longValue();
                dateMap.put(date.format(formatter), count);
            }
        }
        dto.setActivityDateMap(dateMap);
        dto.setRecentActivities(user != null ? activityRepository.findTop10ByUserOrderByTimestampDesc(user)
                                             : activityRepository.findTop10ByOrderByTimestampDesc());

        return dto;
    }
}
