package com.devtrack.devtrack.service;

import com.devtrack.devtrack.entity.DsaProblem;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.enums.Difficulty;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.DsaTopic;
import com.devtrack.devtrack.enums.Platform;
import com.devtrack.devtrack.exception.ResourceNotFoundException;
import com.devtrack.devtrack.repository.DsaProblemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DsaProblemService {

    private final DsaProblemRepository dsaProblemRepository;
    private final ActivityService activityService;
    private final UserService userService;

    public DsaProblemService(DsaProblemRepository dsaProblemRepository,
                             ActivityService activityService,
                             UserService userService) {
        this.dsaProblemRepository = dsaProblemRepository;
        this.activityService = activityService;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public List<DsaProblem> getAllProblems() {
        User user = currentUser();
        return user != null ? dsaProblemRepository.findByUserOrderByIdDesc(user)
                            : dsaProblemRepository.findAll();
    }

    public DsaProblem getProblemById(Long id) {
        User user = currentUser();
        if (user != null) {
            return dsaProblemRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("DSA Problem", "id", id));
        }
        return dsaProblemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DSA Problem", "id", id));
    }

    @Transactional
    public DsaProblem createProblem(DsaProblem problem) {
        User user = currentUser();
        if (user != null) {
            problem.setUser(user);
        }
        DsaProblem saved = dsaProblemRepository.save(problem);
        if (saved.getStatus() == DsaStatus.SOLVED) {
            activityService.recordActivity(
                    ActivityType.DSA_SOLVED,
                    "Solved " + saved.getDifficulty().getDisplayName() + " problem: \"" + saved.getTitle() + "\" on " + saved.getPlatform().getDisplayName(),
                    "DSA",
                    saved.getId()
            );
        } else {
            activityService.recordActivity(
                    ActivityType.DSA_ATTEMPTED,
                    "Attempted " + saved.getDifficulty().getDisplayName() + " problem: \"" + saved.getTitle() + "\" on " + saved.getPlatform().getDisplayName(),
                    "DSA",
                    saved.getId()
            );
        }
        return saved;
    }

    @Transactional
    public DsaProblem updateProblem(Long id, DsaProblem updated) {
        DsaProblem existing = getProblemById(id);
        existing.setTitle(updated.getTitle());
        existing.setPlatform(updated.getPlatform());
        existing.setProblemUrl(updated.getProblemUrl());
        existing.setTopic(updated.getTopic());
        existing.setDifficulty(updated.getDifficulty());
        existing.setNotes(updated.getNotes());
        existing.setAttemptedDate(updated.getAttemptedDate());

        DsaStatus oldStatus = existing.getStatus();
        existing.setStatus(updated.getStatus());

        if (updated.getStatus() == DsaStatus.SOLVED && oldStatus != DsaStatus.SOLVED) {
            existing.setSolvedDate(LocalDate.now());
            activityService.recordActivity(
                    ActivityType.DSA_SOLVED,
                    "Solved " + existing.getDifficulty().getDisplayName() + " problem: \"" + existing.getTitle() + "\"",
                    "DSA",
                    existing.getId()
            );
        } else if (updated.getStatus() != DsaStatus.SOLVED) {
            existing.setSolvedDate(null);
        }

        return dsaProblemRepository.save(existing);
    }

    @Transactional
    public void deleteProblem(Long id) {
        DsaProblem problem = getProblemById(id);
        dsaProblemRepository.delete(problem);
    }

    @Transactional
    public DsaProblem updateStatus(Long id, DsaStatus status) {
        DsaProblem problem = getProblemById(id);
        DsaStatus oldStatus = problem.getStatus();
        problem.setStatus(status);

        if (status == DsaStatus.SOLVED && oldStatus != DsaStatus.SOLVED) {
            problem.setSolvedDate(LocalDate.now());
            activityService.recordActivity(
                    ActivityType.DSA_SOLVED,
                    "Solved " + problem.getDifficulty().getDisplayName() + " problem: \"" + problem.getTitle() + "\"",
                    "DSA",
                    problem.getId()
            );
        } else if (status != DsaStatus.SOLVED) {
            problem.setSolvedDate(null);
        }

        return dsaProblemRepository.save(problem);
    }

    public List<DsaProblem> filterProblems(String keyword, DsaTopic topic, Difficulty difficulty, DsaStatus status, Platform platform) {
        User user = currentUser();
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        if (user != null) {
            return dsaProblemRepository.filterProblemsByUser(user, cleanKeyword, topic, difficulty, status, platform);
        }
        return dsaProblemRepository.filterProblems(cleanKeyword, topic, difficulty, status, platform);
    }

    public long countTotalProblems() {
        User user = currentUser();
        return user != null ? dsaProblemRepository.countByUser(user) : dsaProblemRepository.count();
    }

    public long countByStatus(DsaStatus status) {
        User user = currentUser();
        return user != null ? dsaProblemRepository.countByUserAndStatus(user, status) : dsaProblemRepository.countByStatus(status);
    }

    public long countByDifficulty(Difficulty difficulty) {
        User user = currentUser();
        return user != null ? dsaProblemRepository.countByUserAndDifficulty(user, difficulty) : dsaProblemRepository.countByDifficulty(difficulty);
    }
}
