package com.devtrack.devtrack.service;

import com.devtrack.devtrack.entity.Activity;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.repository.ActivityRepository;
import com.devtrack.devtrack.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(ActivityRepository activityRepository, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            return userRepository.findByUsername(username)
                    .or(() -> userRepository.findByEmail(username))
                    .orElse(null);
        }
        return userRepository.findByUsername("alex").orElse(null);
    }

    @Transactional
    public Activity recordActivity(ActivityType type, String description, String referenceType, Long referenceId) {
        User user = currentUser();
        Activity activity = new Activity(user, type, description, referenceType, referenceId, LocalDateTime.now());
        return activityRepository.save(activity);
    }

    @Transactional
    public Activity recordActivityWithTimestamp(ActivityType type, String description, String referenceType, Long referenceId, LocalDateTime timestamp) {
        User user = currentUser();
        Activity activity = new Activity(user, type, description, referenceType, referenceId, timestamp);
        return activityRepository.save(activity);
    }

    @Transactional
    public Activity recordActivityForUser(User user, ActivityType type, String description, String referenceType, Long referenceId, LocalDateTime timestamp) {
        Activity activity = new Activity(user, type, description, referenceType, referenceId, timestamp);
        return activityRepository.save(activity);
    }

    public List<Activity> getRecentActivities(int limit) {
        User user = currentUser();
        if (user != null) {
            return limit <= 10 ? activityRepository.findTop10ByUserOrderByTimestampDesc(user)
                               : activityRepository.findTop50ByUserOrderByTimestampDesc(user);
        }
        return limit <= 10 ? activityRepository.findTop10ByOrderByTimestampDesc()
                           : activityRepository.findTop50ByOrderByTimestampDesc();
    }

    public List<Activity> getAllActivities() {
        User user = currentUser();
        return user != null ? activityRepository.findByUserOrderByTimestampDesc(user)
                            : activityRepository.findAllByOrderByTimestampDesc();
    }

    public List<Activity> getActivitiesByType(ActivityType type) {
        User user = currentUser();
        return user != null ? activityRepository.findByUserAndActivityTypeOrderByTimestampDesc(user, type)
                            : activityRepository.findByActivityTypeOrderByTimestampDesc(type);
    }

    public List<Activity> getActivitiesBetween(LocalDateTime start, LocalDateTime end) {
        User user = currentUser();
        return user != null ? activityRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(user, start, end)
                            : activityRepository.findByTimestampBetweenOrderByTimestampDesc(start, end);
    }

    public List<LocalDate> getDistinctActivityDates() {
        User user = currentUser();
        return user != null ? activityRepository.findDistinctActivityDatesByUser(user)
                            : activityRepository.findDistinctActivityDates();
    }

    public List<Object[]> countActivitiesPerDay() {
        User user = currentUser();
        return user != null ? activityRepository.countActivitiesPerDayByUser(user)
                            : activityRepository.countActivitiesPerDay();
    }

    public List<Object[]> countActivitiesByType() {
        User user = currentUser();
        return user != null ? activityRepository.countActivitiesByTypeByUser(user)
                            : activityRepository.countActivitiesByType();
    }

    public long getActivitiesCountBetween(LocalDateTime start, LocalDateTime end) {
        User user = currentUser();
        return user != null ? activityRepository.countByUserAndTimestampBetween(user, start, end)
                            : activityRepository.countByTimestampBetween(start, end);
    }
}
