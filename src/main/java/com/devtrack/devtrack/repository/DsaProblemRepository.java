package com.devtrack.devtrack.repository;

import com.devtrack.devtrack.entity.DsaProblem;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.Difficulty;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.DsaTopic;
import com.devtrack.devtrack.enums.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DsaProblemRepository extends JpaRepository<DsaProblem, Long> {

    List<DsaProblem> findByUserOrderByIdDesc(User user);

    Optional<DsaProblem> findByIdAndUser(Long id, User user);

    long countByUser(User user);

    long countByUserAndStatus(User user, DsaStatus status);

    long countByUserAndDifficulty(User user, Difficulty difficulty);

    long countByUserAndTopic(User user, DsaTopic topic);

    @Query("SELECT p FROM DsaProblem p WHERE p.user = :user AND " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:topic IS NULL OR p.topic = :topic) AND " +
           "(:difficulty IS NULL OR p.difficulty = :difficulty) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:platform IS NULL OR p.platform = :platform) " +
           "ORDER BY p.id DESC")
    List<DsaProblem> filterProblemsByUser(@Param("user") User user,
                                          @Param("keyword") String keyword,
                                          @Param("topic") DsaTopic topic,
                                          @Param("difficulty") Difficulty difficulty,
                                          @Param("status") DsaStatus status,
                                          @Param("platform") Platform platform);

    @Query("SELECT p.topic, COUNT(p) FROM DsaProblem p WHERE p.user = :user GROUP BY p.topic ORDER BY COUNT(p) DESC")
    List<Object[]> countProblemsByTopicAndUser(@Param("user") User user);

    @Query("SELECT p.difficulty, COUNT(p) FROM DsaProblem p WHERE p.user = :user GROUP BY p.difficulty")
    List<Object[]> countProblemsByDifficultyAndUser(@Param("user") User user);

    @Query("SELECT p.difficulty, COUNT(p) FROM DsaProblem p WHERE p.user = :user AND p.status = 'SOLVED' GROUP BY p.difficulty")
    List<Object[]> countSolvedProblemsByDifficultyAndUser(@Param("user") User user);

    @Query("SELECT p.topic, COUNT(p) FROM DsaProblem p WHERE p.user = :user AND p.status = 'SOLVED' GROUP BY p.topic")
    List<Object[]> countSolvedProblemsByTopicAndUser(@Param("user") User user);

    @Query("SELECT p.platform, COUNT(p) FROM DsaProblem p WHERE p.user = :user GROUP BY p.platform")
    List<Object[]> countProblemsByPlatformAndUser(@Param("user") User user);

    List<DsaProblem> findByUserAndSolvedDateBetween(User user, LocalDate start, LocalDate end);

    @Query("SELECT p.solvedDate, COUNT(p) FROM DsaProblem p WHERE p.user = :user AND p.status = 'SOLVED' AND p.solvedDate IS NOT NULL GROUP BY p.solvedDate ORDER BY p.solvedDate ASC")
    List<Object[]> countSolvedProblemsOverTimeAndUser(@Param("user") User user);

    // Global / fallback methods
    List<DsaProblem> findByTopic(DsaTopic topic);
    List<DsaProblem> findByDifficulty(Difficulty difficulty);
    List<DsaProblem> findByStatus(DsaStatus status);
    List<DsaProblem> findByPlatform(Platform platform);
    long countByStatus(DsaStatus status);
    long countByDifficulty(Difficulty difficulty);
    long countByTopic(DsaTopic topic);
    List<DsaProblem> findBySolvedDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT p FROM DsaProblem p WHERE " +
           "(:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.notes) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:topic IS NULL OR p.topic = :topic) AND " +
           "(:difficulty IS NULL OR p.difficulty = :difficulty) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:platform IS NULL OR p.platform = :platform) " +
           "ORDER BY p.id DESC")
    List<DsaProblem> filterProblems(@Param("keyword") String keyword,
                                    @Param("topic") DsaTopic topic,
                                    @Param("difficulty") Difficulty difficulty,
                                    @Param("status") DsaStatus status,
                                    @Param("platform") Platform platform);

    @Query("SELECT p.topic, COUNT(p) FROM DsaProblem p GROUP BY p.topic ORDER BY COUNT(p) DESC")
    List<Object[]> countProblemsByTopic();

    @Query("SELECT p.difficulty, COUNT(p) FROM DsaProblem p GROUP BY p.difficulty")
    List<Object[]> countProblemsByDifficulty();

    @Query("SELECT p.difficulty, COUNT(p) FROM DsaProblem p WHERE p.status = 'SOLVED' GROUP BY p.difficulty")
    List<Object[]> countSolvedProblemsByDifficulty();

    @Query("SELECT p.topic, COUNT(p) FROM DsaProblem p WHERE p.status = 'SOLVED' GROUP BY p.topic")
    List<Object[]> countSolvedProblemsByTopic();

    @Query("SELECT p.platform, COUNT(p) FROM DsaProblem p GROUP BY p.platform")
    List<Object[]> countProblemsByPlatform();

    @Query("SELECT p.solvedDate, COUNT(p) FROM DsaProblem p WHERE p.status = 'SOLVED' AND p.solvedDate IS NOT NULL GROUP BY p.solvedDate ORDER BY p.solvedDate ASC")
    List<Object[]> countSolvedProblemsOverTime();
}
