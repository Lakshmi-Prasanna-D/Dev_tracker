package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.Goal;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.GoalCategory;
import com.devtrack.devtrack.enums.GoalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findByUser(User user);

    Optional<Goal> findByIdAndUser(Long id, User user);

    List<Goal> findByUserAndCategory(User user, GoalCategory category);

    List<Goal> findByUserAndStatus(User user, GoalStatus status);

    List<Goal> findByUserAndCategoryAndStatus(User user, GoalCategory category, GoalStatus status);

    long countByUser(User user);

    long countByUserAndStatus(User user, GoalStatus status);

    @Query("SELECT g FROM Goal g WHERE g.user = :user AND (LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Goal> searchGoalsByUser(@Param("user") User user, @Param("keyword") String keyword);

    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.targetDate >= :today AND g.status != 'COMPLETED' ORDER BY g.targetDate ASC")
    List<Goal> findUpcomingGoalsByUser(@Param("user") User user, @Param("today") LocalDate today);

    @Query("SELECT g FROM Goal g WHERE g.user = :user AND g.targetDate < :today AND g.status != 'COMPLETED' ORDER BY g.targetDate ASC")
    List<Goal> findOverdueGoalsByUser(@Param("user") User user, @Param("today") LocalDate today);

    List<Goal> findByUserAndTargetDateBetween(User user, LocalDate start, LocalDate end);

    @Query("SELECT g.category, COUNT(g) FROM Goal g WHERE g.user = :user GROUP BY g.category")
    List<Object[]> countGoalsByCategoryAndUser(@Param("user") User user);

    @Query("SELECT g.status, COUNT(g) FROM Goal g WHERE g.user = :user GROUP BY g.status")
    List<Object[]> countGoalsByStatusGroupAndUser(@Param("user") User user);

    // Global / fallback methods
    List<Goal> findByCategory(GoalCategory category);
    List<Goal> findByStatus(GoalStatus status);
    List<Goal> findByCategoryAndStatus(GoalCategory category, GoalStatus status);
    long countByStatus(GoalStatus status);
    List<Goal> findByTargetDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT g FROM Goal g WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Goal> searchGoals(@Param("keyword") String keyword);

    @Query("SELECT g FROM Goal g WHERE g.targetDate >= :today AND g.status != 'COMPLETED' ORDER BY g.targetDate ASC")
    List<Goal> findUpcomingGoals(@Param("today") LocalDate today);

    @Query("SELECT g FROM Goal g WHERE g.targetDate < :today AND g.status != 'COMPLETED' ORDER BY g.targetDate ASC")
    List<Goal> findOverdueGoals(@Param("today") LocalDate today);

    @Query("SELECT g.category, COUNT(g) FROM Goal g GROUP BY g.category")
    List<Object[]> countGoalsByCategory();

    @Query("SELECT g.status, COUNT(g) FROM Goal g GROUP BY g.status")
    List<Object[]> countGoalsByStatusGroup();
}
