package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.Project;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByUserOrderByIdDesc(User user);

    Optional<Project> findByIdAndUser(Long id, User user);

    List<Project> findByUserAndStatus(User user, ProjectStatus status);

    long countByUser(User user);

    long countByUserAndStatus(User user, ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.user = :user AND " +
           "(:keyword IS NULL OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.technologies) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.id DESC")
    List<Project> filterProjectsByUser(@Param("user") User user, @Param("keyword") String keyword, @Param("status") ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.user = :user AND p.expectedCompletionDate >= :today AND p.status != 'COMPLETED' ORDER BY p.expectedCompletionDate ASC")
    List<Project> findUpcomingProjectsByUser(@Param("user") User user, @Param("today") LocalDate today);

    List<Project> findByUserAndExpectedCompletionDateBetween(User user, LocalDate start, LocalDate end);

    @Query("SELECT p.status, COUNT(p) FROM Project p WHERE p.user = :user GROUP BY p.status")
    List<Object[]> countProjectsByStatusGroupAndUser(@Param("user") User user);

    // Global / fallback methods
    List<Project> findByStatus(ProjectStatus status);
    long countByStatus(ProjectStatus status);
    List<Project> findByExpectedCompletionDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT p FROM Project p WHERE " +
           "(:keyword IS NULL OR LOWER(p.projectName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.technologies) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:status IS NULL OR p.status = :status) " +
           "ORDER BY p.id DESC")
    List<Project> filterProjects(@Param("keyword") String keyword, @Param("status") ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.expectedCompletionDate >= :today AND p.status != 'COMPLETED' ORDER BY p.expectedCompletionDate ASC")
    List<Project> findUpcomingProjects(@Param("today") LocalDate today);

    @Query("SELECT p.status, COUNT(p) FROM Project p GROUP BY p.status")
    List<Object[]> countProjectsByStatusGroup();
}
