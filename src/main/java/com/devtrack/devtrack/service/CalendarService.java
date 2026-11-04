package com.devtrack.devtrack.service;

import com.devtrack.devtrack.dto.CalendarEventDto;
import com.devtrack.devtrack.entity.*;
import com.devtrack.devtrack.enums.GoalStatus;
import com.devtrack.devtrack.enums.ProjectStatus;
import com.devtrack.devtrack.enums.ReminderStatus;
import com.devtrack.devtrack.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CalendarService {

    private final GoalRepository goalRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository milestoneRepository;
    private final ReminderRepository reminderRepository;
    private final ActivityRepository activityRepository;
    private final UserService userService;

    public CalendarService(GoalRepository goalRepository,
                           ProjectRepository projectRepository,
                           ProjectMilestoneRepository milestoneRepository,
                           ReminderRepository reminderRepository,
                           ActivityRepository activityRepository,
                           UserService userService) {
        this.goalRepository = goalRepository;
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.reminderRepository = reminderRepository;
        this.activityRepository = activityRepository;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public List<CalendarEventDto> getEventsForMonth(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        User user = currentUser();

        List<CalendarEventDto> events = new ArrayList<>();

        // 1. Goal Deadlines
        List<Goal> goals = user != null ? goalRepository.findByUserAndTargetDateBetween(user, start, end)
                                        : goalRepository.findByTargetDateBetween(start, end);
        for (Goal g : goals) {
            String badgeClass = g.getStatus() == GoalStatus.COMPLETED ? "success" : (g.isOverdue() ? "danger" : "primary");
            events.add(new CalendarEventDto(
                    g.getId(),
                    "[Goal] " + g.getTitle(),
                    g.getTargetDate(),
                    "GOAL",
                    badgeClass,
                    "/goals",
                    g.getDescription(),
                    g.getStatus() == GoalStatus.COMPLETED
            ));
        }

        // 2. Project Deadlines
        List<Project> projects = user != null ? projectRepository.findByUserAndExpectedCompletionDateBetween(user, start, end)
                                              : projectRepository.findByExpectedCompletionDateBetween(start, end);
        for (Project p : projects) {
            String badgeClass = p.getStatus() == ProjectStatus.COMPLETED ? "success" : "info";
            events.add(new CalendarEventDto(
                    p.getId(),
                    "[Project] " + p.getProjectName(),
                    p.getExpectedCompletionDate(),
                    "PROJECT",
                    badgeClass,
                    "/projects/" + p.getId(),
                    p.getDescription(),
                    p.getStatus() == ProjectStatus.COMPLETED
            ));
        }

        // 3. Project Milestones
        List<ProjectMilestone> milestones = user != null ? milestoneRepository.findByProjectUserAndDueDateBetween(user, start, end)
                                                         : milestoneRepository.findByDueDateBetween(start, end);
        for (ProjectMilestone m : milestones) {
            String badgeClass = m.isCompleted() ? "success" : "warning";
            events.add(new CalendarEventDto(
                    m.getId(),
                    "[Milestone] " + m.getTitle() + " (" + m.getProject().getProjectName() + ")",
                    m.getDueDate(),
                    "MILESTONE",
                    badgeClass,
                    "/projects/" + m.getProject().getId(),
                    m.getDescription(),
                    m.isCompleted()
            ));
        }

        // 4. Reminders
        LocalDateTime startDt = start.atStartOfDay();
        LocalDateTime endDt = end.atTime(23, 59, 59);
        List<Reminder> reminders = user != null ? reminderRepository.findByUserAndDueDateTimeBetween(user, startDt, endDt)
                                                : reminderRepository.findByDueDateTimeBetween(startDt, endDt);
        for (Reminder r : reminders) {
            String badgeClass = r.getStatus() == ReminderStatus.COMPLETED ? "success" : (r.isOverdue() ? "danger" : "warning");
            events.add(new CalendarEventDto(
                    r.getId(),
                    "[Reminder] " + r.getTitle(),
                    r.getDueDateTime().toLocalDate(),
                    "REMINDER",
                    badgeClass,
                    "/reminders",
                    r.getMessage(),
                    r.getStatus() == ReminderStatus.COMPLETED
            ));
        }

        // 5. Completed Activities
        List<Activity> activities = user != null ? activityRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(user, startDt, endDt)
                                                 : activityRepository.findByTimestampBetweenOrderByTimestampDesc(startDt, endDt);
        for (Activity a : activities) {
            events.add(new CalendarEventDto(
                    a.getId(),
                    "Activity: " + a.getDescription(),
                    a.getTimestamp().toLocalDate(),
                    "ACTIVITY",
                    "secondary",
                    "/activities",
                    a.getDescription(),
                    true
            ));
        }

        events.sort(Comparator.comparing(CalendarEventDto::getDate));
        return events;
    }
}
