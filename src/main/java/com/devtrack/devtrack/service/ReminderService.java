package com.devtrack.devtrack.service;

import com.devtrack.devtrack.entity.Reminder;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ReminderStatus;
import com.devtrack.devtrack.exception.ResourceNotFoundException;
import com.devtrack.devtrack.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserService userService;

    public ReminderService(ReminderRepository reminderRepository, UserService userService) {
        this.reminderRepository = reminderRepository;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public List<Reminder> getAllReminders() {
        User user = currentUser();
        return user != null ? reminderRepository.findByUserOrderByDueDateTimeAsc(user)
                            : reminderRepository.findAllByOrderByDueDateTimeAsc();
    }

    public List<Reminder> getPendingReminders() {
        User user = currentUser();
        return user != null ? reminderRepository.findByUserAndStatusOrderByDueDateTimeAsc(user, ReminderStatus.PENDING)
                            : reminderRepository.findByStatusOrderByDueDateTimeAsc(ReminderStatus.PENDING);
    }

    public Reminder getReminderById(Long id) {
        User user = currentUser();
        if (user != null) {
            return reminderRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Reminder", "id", id));
        }
        return reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder", "id", id));
    }

    @Transactional
    public Reminder createReminder(Reminder reminder) {
        User user = currentUser();
        if (user != null) {
            reminder.setUser(user);
        }
        return reminderRepository.save(reminder);
    }

    @Transactional
    public Reminder updateReminder(Long id, Reminder updated) {
        Reminder existing = getReminderById(id);
        existing.setTitle(updated.getTitle());
        existing.setMessage(updated.getMessage());
        existing.setDueDateTime(updated.getDueDateTime());
        existing.setStatus(updated.getStatus());
        existing.setGoal(updated.getGoal());
        existing.setProject(updated.getProject());
        return reminderRepository.save(existing);
    }

    @Transactional
    public void deleteReminder(Long id) {
        Reminder reminder = getReminderById(id);
        reminderRepository.delete(reminder);
    }

    @Transactional
    public Reminder markCompleted(Long id) {
        Reminder reminder = getReminderById(id);
        reminder.setStatus(ReminderStatus.COMPLETED);
        return reminderRepository.save(reminder);
    }

    @Transactional
    public Reminder dismissReminder(Long id) {
        Reminder reminder = getReminderById(id);
        reminder.setStatus(ReminderStatus.DISMISSED);
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getUpcomingReminders() {
        User user = currentUser();
        return user != null ? reminderRepository.findUpcomingRemindersByUser(user, LocalDateTime.now())
                            : reminderRepository.findUpcomingReminders(LocalDateTime.now());
    }

    public List<Reminder> getOverdueReminders() {
        User user = currentUser();
        return user != null ? reminderRepository.findOverdueRemindersByUser(user, LocalDateTime.now())
                            : reminderRepository.findOverdueReminders(LocalDateTime.now());
    }

    public long countPending() {
        User user = currentUser();
        return user != null ? reminderRepository.countByUserAndStatus(user, ReminderStatus.PENDING)
                            : reminderRepository.countByStatus(ReminderStatus.PENDING);
    }
}
