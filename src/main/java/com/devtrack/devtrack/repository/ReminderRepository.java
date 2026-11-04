package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.Reminder;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByUserOrderByDueDateTimeAsc(User user);

    Optional<Reminder> findByIdAndUser(Long id, User user);

    List<Reminder> findByUserAndStatusOrderByDueDateTimeAsc(User user, ReminderStatus status);

    List<Reminder> findTop5ByUserAndStatusOrderByDueDateTimeAsc(User user, ReminderStatus status);

    @Query("SELECT r FROM Reminder r WHERE r.user = :user AND r.status = 'PENDING' AND r.dueDateTime >= :now ORDER BY r.dueDateTime ASC")
    List<Reminder> findUpcomingRemindersByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    @Query("SELECT r FROM Reminder r WHERE r.user = :user AND r.status = 'PENDING' AND r.dueDateTime < :now ORDER BY r.dueDateTime ASC")
    List<Reminder> findOverdueRemindersByUser(@Param("user") User user, @Param("now") LocalDateTime now);

    List<Reminder> findByUserAndDueDateTimeBetween(User user, LocalDateTime start, LocalDateTime end);

    long countByUserAndStatus(User user, ReminderStatus status);

    // Backward-compatible
    List<Reminder> findByStatusOrderByDueDateTimeAsc(ReminderStatus status);
    List<Reminder> findAllByOrderByDueDateTimeAsc();
    List<Reminder> findTop5ByStatusOrderByDueDateTimeAsc(ReminderStatus status);
    @Query("SELECT r FROM Reminder r WHERE r.status = 'PENDING' AND r.dueDateTime >= :now ORDER BY r.dueDateTime ASC")
    List<Reminder> findUpcomingReminders(@Param("now") LocalDateTime now);
    @Query("SELECT r FROM Reminder r WHERE r.status = 'PENDING' AND r.dueDateTime < :now ORDER BY r.dueDateTime ASC")
    List<Reminder> findOverdueReminders(@Param("now") LocalDateTime now);
    List<Reminder> findByDueDateTimeBetween(LocalDateTime start, LocalDateTime end);
    long countByStatus(ReminderStatus status);
}
