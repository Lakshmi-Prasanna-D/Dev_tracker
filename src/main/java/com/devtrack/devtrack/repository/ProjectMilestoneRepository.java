package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.ProjectMilestone;
import com.devtrack.devtrack.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectMilestoneRepository extends JpaRepository<ProjectMilestone, Long> {

    List<ProjectMilestone> findByProjectId(Long projectId);

    List<ProjectMilestone> findByProjectUserAndDueDateBetween(User user, LocalDate start, LocalDate end);

    @Query("SELECT m FROM ProjectMilestone m WHERE m.project.user = :user AND m.completed = false AND m.dueDate < :today ORDER BY m.dueDate ASC")
    List<ProjectMilestone> findOverdueMilestonesByUser(@Param("user") User user, @Param("today") LocalDate today);

    // Backward-compatible
    List<ProjectMilestone> findByDueDateBetween(LocalDate start, LocalDate end);
    @Query("SELECT m FROM ProjectMilestone m WHERE m.completed = false AND m.dueDate < :today ORDER BY m.dueDate ASC")
    List<ProjectMilestone> findOverdueMilestones(@Param("today") LocalDate today);
    long countByCompletedTrue();
    long countByCompletedFalse();
}
