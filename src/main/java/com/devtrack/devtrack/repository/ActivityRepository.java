package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.Activity;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findTop10ByUserOrderByTimestampDesc(User user);

    List<Activity> findTop50ByUserOrderByTimestampDesc(User user);

    List<Activity> findByUserOrderByTimestampDesc(User user);

    List<Activity> findByUserAndActivityTypeOrderByTimestampDesc(User user, ActivityType activityType);

    List<Activity> findByUserAndTimestampBetweenOrderByTimestampDesc(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT DISTINCT CAST(a.timestamp AS LocalDate) FROM Activity a WHERE a.user = :user ORDER BY CAST(a.timestamp AS LocalDate) ASC")
    List<LocalDate> findDistinctActivityDatesByUser(@Param("user") User user);

    @Query("SELECT CAST(a.timestamp AS LocalDate), COUNT(a) FROM Activity a WHERE a.user = :user GROUP BY CAST(a.timestamp AS LocalDate) ORDER BY CAST(a.timestamp AS LocalDate) ASC")
    List<Object[]> countActivitiesPerDayByUser(@Param("user") User user);

    @Query("SELECT a.activityType, COUNT(a) FROM Activity a WHERE a.user = :user GROUP BY a.activityType")
    List<Object[]> countActivitiesByTypeByUser(@Param("user") User user);

    long countByUserAndTimestampBetween(User user, LocalDateTime start, LocalDateTime end);

    // Backward-compatible
    List<Activity> findTop10ByOrderByTimestampDesc();
    List<Activity> findTop50ByOrderByTimestampDesc();
    List<Activity> findAllByOrderByTimestampDesc();
    List<Activity> findByActivityTypeOrderByTimestampDesc(ActivityType activityType);
    List<Activity> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime start, LocalDateTime end);
    @Query("SELECT DISTINCT CAST(a.timestamp AS LocalDate) FROM Activity a ORDER BY CAST(a.timestamp AS LocalDate) ASC")
    List<LocalDate> findDistinctActivityDates();
    @Query("SELECT CAST(a.timestamp AS LocalDate), COUNT(a) FROM Activity a GROUP BY CAST(a.timestamp AS LocalDate) ORDER BY CAST(a.timestamp AS LocalDate) ASC")
    List<Object[]> countActivitiesPerDay();
    @Query("SELECT a.activityType, COUNT(a) FROM Activity a GROUP BY a.activityType")
    List<Object[]> countActivitiesByType();
    long countByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
