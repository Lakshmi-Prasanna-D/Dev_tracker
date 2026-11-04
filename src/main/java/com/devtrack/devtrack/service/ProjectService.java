package com.devtrack.devtrack.service;

import com.devtrack.devtrack.entity.Project;
import com.devtrack.devtrack.entity.ProjectMilestone;
import com.devtrack.devtrack.entity.User;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.enums.ProjectStatus;
import com.devtrack.devtrack.exception.ResourceNotFoundException;
import com.devtrack.devtrack.repository.ProjectMilestoneRepository;
import com.devtrack.devtrack.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMilestoneRepository milestoneRepository;
    private final ActivityService activityService;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository,
                          ProjectMilestoneRepository milestoneRepository,
                          ActivityService activityService,
                          UserService userService) {
        this.projectRepository = projectRepository;
        this.milestoneRepository = milestoneRepository;
        this.activityService = activityService;
        this.userService = userService;
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    public List<Project> getAllProjects() {
        User user = currentUser();
        return user != null ? projectRepository.findByUserOrderByIdDesc(user)
                            : projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        User user = currentUser();
        if (user != null) {
            return projectRepository.findByIdAndUser(id, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        }
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    @Transactional
    public Project createProject(Project project) {
        User user = currentUser();
        if (user != null) {
            project.setUser(user);
        }
        Project saved = projectRepository.save(project);
        activityService.recordActivity(
                ActivityType.PROJECT_CREATED,
                "Created project: \"" + saved.getProjectName() + "\"",
                "PROJECT",
                saved.getId()
        );
        return saved;
    }

    @Transactional
    public Project updateProject(Long id, Project updated) {
        Project existing = getProjectById(id);
        existing.setProjectName(updated.getProjectName());
        existing.setDescription(updated.getDescription());
        existing.setTechnologies(updated.getTechnologies());
        existing.setGithubUrl(updated.getGithubUrl());
        existing.setDeploymentUrl(updated.getDeploymentUrl());
        existing.setStartDate(updated.getStartDate());
        existing.setExpectedCompletionDate(updated.getExpectedCompletionDate());
        existing.setProgressPercentage(updated.getProgressPercentage());

        ProjectStatus oldStatus = existing.getStatus();
        existing.setStatus(updated.getStatus());

        if (updated.getStatus() == ProjectStatus.COMPLETED && oldStatus != ProjectStatus.COMPLETED) {
            existing.setProgressPercentage(100);
            activityService.recordActivity(
                    ActivityType.PROJECT_COMPLETED,
                    "Completed project: \"" + existing.getProjectName() + "\"",
                    "PROJECT",
                    existing.getId()
            );
        } else {
            activityService.recordActivity(
                    ActivityType.PROJECT_UPDATED,
                    "Updated project: \"" + existing.getProjectName() + "\" (" + existing.getProgressPercentage() + "% progress)",
                    "PROJECT",
                    existing.getId()
            );
        }

        return projectRepository.save(existing);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }

    @Transactional
    public Project updateProgress(Long id, Integer progress) {
        Project project = getProjectById(id);
        project.setProgressPercentage(progress);
        if (progress >= 100) {
            project.setStatus(ProjectStatus.COMPLETED);
            activityService.recordActivity(
                    ActivityType.PROJECT_COMPLETED,
                    "Completed project: \"" + project.getProjectName() + "\" (100% completed)",
                    "PROJECT",
                    project.getId()
            );
        } else if (project.getStatus() == ProjectStatus.COMPLETED && progress < 100) {
            project.setStatus(ProjectStatus.IN_PROGRESS);
        }
        return projectRepository.save(project);
    }

    @Transactional
    public ProjectMilestone addMilestone(Long projectId, ProjectMilestone milestone) {
        Project project = getProjectById(projectId);
        milestone.setProject(project);
        ProjectMilestone saved = milestoneRepository.save(milestone);
        project.getMilestones().add(saved);

        updateProjectProgressFromMilestones(project);
        return saved;
    }

    @Transactional
    public ProjectMilestone toggleMilestone(Long milestoneId) {
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));

        // Validate project ownership
        getProjectById(milestone.getProject().getId());

        boolean nowCompleted = !milestone.isCompleted();
        milestone.setCompleted(nowCompleted);
        if (nowCompleted) {
            milestone.setCompletedDate(LocalDateTime.now());
            activityService.recordActivity(
                    ActivityType.MILESTONE_COMPLETED,
                    "Completed milestone: \"" + milestone.getTitle() + "\" in project " + milestone.getProject().getProjectName(),
                    "MILESTONE",
                    milestone.getId()
            );
        } else {
            milestone.setCompletedDate(null);
        }

        ProjectMilestone saved = milestoneRepository.save(milestone);
        updateProjectProgressFromMilestones(milestone.getProject());
        return saved;
    }

    @Transactional
    public void deleteMilestone(Long milestoneId) {
        ProjectMilestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Milestone", "id", milestoneId));
        Project project = getProjectById(milestone.getProject().getId());
        project.getMilestones().remove(milestone);
        milestoneRepository.delete(milestone);
        updateProjectProgressFromMilestones(project);
    }

    private void updateProjectProgressFromMilestones(Project project) {
        List<ProjectMilestone> list = project.getMilestones();
        if (list != null && !list.isEmpty()) {
            long completedCount = list.stream().filter(ProjectMilestone::isCompleted).count();
            int newPercentage = (int) Math.round(((double) completedCount / list.size()) * 100);
            project.setProgressPercentage(newPercentage);
            if (newPercentage == 100 && project.getStatus() != ProjectStatus.COMPLETED) {
                project.setStatus(ProjectStatus.COMPLETED);
            }
            projectRepository.save(project);
        }
    }

    public List<Project> filterProjects(String keyword, ProjectStatus status) {
        User user = currentUser();
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        if (user != null) {
            return projectRepository.filterProjectsByUser(user, cleanKeyword, status);
        }
        return projectRepository.filterProjects(cleanKeyword, status);
    }

    public List<Project> getUpcomingProjects() {
        User user = currentUser();
        return user != null ? projectRepository.findUpcomingProjectsByUser(user, LocalDate.now())
                            : projectRepository.findUpcomingProjects(LocalDate.now());
    }

    public long countTotalProjects() {
        User user = currentUser();
        return user != null ? projectRepository.countByUser(user) : projectRepository.count();
    }

    public long countByStatus(ProjectStatus status) {
        User user = currentUser();
        return user != null ? projectRepository.countByUserAndStatus(user, status) : projectRepository.countByStatus(status);
    }
}
