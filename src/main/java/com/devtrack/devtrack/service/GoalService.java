package com.devtrack.devtrack.service;

import com.devtrack.devtrack.entity.Goal;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.enums.GoalCategory;
import com.devtrack.devtrack.enums.GoalStatus;
import com.devtrack.devtrack.exception.ResourceNotFoundException;
import com.devtrack.devtrack.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;
    private final ActivityService activityService;
    private final UserService userService;

    public GoalService(GoalRepository goalRepository, ActivityService activityService, UserService userService) {
        this.goalRepository = goalRepository;
        this.activityService = activityService;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public List<Goal> getAllGoals() {
        User user = currentUser();
        return user != null ? goalRepository.findByUser(user) : goalRepository.findAll();
    }

    public Goal getGoalById(Long id) {
        User user = currentUser();
        if (user != null) {
            return goalRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
        }
        return goalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Goal", "id", id));
    }

    @Transactional
    public Goal createGoal(Goal goal) {
        User user = currentUser();
        if (user != null) {
            goal.setUser(user);
        }
        Goal savedGoal = goalRepository.save(goal);
        activityService.recordActivity(
                ActivityType.GOAL_CREATED,
                "Created new learning goal: \"" + savedGoal.getTitle() + "\" in " + savedGoal.getCategory().getDisplayName(),
                "GOAL",
                savedGoal.getId()
        );
        return savedGoal;
    }

    @Transactional
    public Goal updateGoal(Long id, Goal updatedGoal) {
        Goal existing = getGoalById(id);
        existing.setTitle(updatedGoal.getTitle());
        existing.setDescription(updatedGoal.getDescription());
        existing.setCategory(updatedGoal.getCategory());
        existing.setTargetDate(updatedGoal.getTargetDate());

        GoalStatus oldStatus = existing.getStatus();
        existing.setStatus(updatedGoal.getStatus());

        if (updatedGoal.getStatus() == GoalStatus.COMPLETED && oldStatus != GoalStatus.COMPLETED) {
            existing.setCompletedDate(LocalDateTime.now());
            activityService.recordActivity(
                    ActivityType.GOAL_COMPLETED,
                    "Completed goal: \"" + existing.getTitle() + "\"",
                    "GOAL",
                    existing.getId()
            );
        } else if (updatedGoal.getStatus() != GoalStatus.COMPLETED) {
            existing.setCompletedDate(null);
        }

        return goalRepository.save(existing);
    }

    @Transactional
    public void deleteGoal(Long id) {
        Goal goal = getGoalById(id);
        goalRepository.delete(goal);
    }

    @Transactional
    public Goal markCompleted(Long id) {
        Goal goal = getGoalById(id);
        if (goal.getStatus() != GoalStatus.COMPLETED) {
            goal.setStatus(GoalStatus.COMPLETED);
            goal.setCompletedDate(LocalDateTime.now());
            goal = goalRepository.save(goal);
            activityService.recordActivity(
                    ActivityType.GOAL_COMPLETED,
                    "Completed goal: \"" + goal.getTitle() + "\"",
                    "GOAL",
                    goal.getId()
            );
        }
        return goal;
    }

    @Transactional
    public Goal updateStatus(Long id, GoalStatus status) {
        Goal goal = getGoalById(id);
        GoalStatus oldStatus = goal.getStatus();
        goal.setStatus(status);
        if (status == GoalStatus.COMPLETED && oldStatus != GoalStatus.COMPLETED) {
            goal.setCompletedDate(LocalDateTime.now());
            activityService.recordActivity(
                    ActivityType.GOAL_COMPLETED,
                    "Completed goal: \"" + goal.getTitle() + "\"",
                    "GOAL",
                    goal.getId()
            );
        } else if (status != GoalStatus.COMPLETED) {
            goal.setCompletedDate(null);
        }
        return goalRepository.save(goal);
    }

    public List<Goal> filterGoals(GoalCategory category, GoalStatus status, String keyword) {
        User user = currentUser();
        if (user == null) {
            return goalRepository.findAll();
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            return goalRepository.searchGoalsByUser(user, keyword.trim());
        }
        if (category != null && status != null) {
            return goalRepository.findByUserAndCategoryAndStatus(user, category, status);
        }
        if (category != null) {
            return goalRepository.findByUserAndCategory(user, category);
        }
        if (status != null) {
            return goalRepository.findByUserAndStatus(user, status);
        }
        return goalRepository.findByUser(user);
    }

    public List<Goal> getUpcomingGoals() {
        User user = currentUser();
        return user != null ? goalRepository.findUpcomingGoalsByUser(user, LocalDate.now())
                            : goalRepository.findUpcomingGoals(LocalDate.now());
    }

    public List<Goal> getOverdueGoals() {
        User user = currentUser();
        return user != null ? goalRepository.findOverdueGoalsByUser(user, LocalDate.now())
                            : goalRepository.findOverdueGoals(LocalDate.now());
    }

    public long countTotalGoals() {
        User user = currentUser();
        return user != null ? goalRepository.countByUser(user) : goalRepository.count();
    }

    public long countByStatus(GoalStatus status) {
        User user = currentUser();
        return user != null ? goalRepository.countByUserAndStatus(user, status) : goalRepository.countByStatus(status);
    }
}
