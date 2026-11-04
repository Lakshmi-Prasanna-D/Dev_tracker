package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.entity.Project;
import com.devtrack.devtrack.entity.ProjectMilestone;
import com.devtrack.devtrack.enums.ProjectStatus;
import com.devtrack.devtrack.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public String listProjects(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) ProjectStatus status,
                               Model model) {
        List<Project> projects = projectService.filterProjects(keyword, status);

        model.addAttribute("projects", projects);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalProjects", projectService.countTotalProjects());
        model.addAttribute("activeProjects", projectService.countByStatus(ProjectStatus.IN_PROGRESS));
        model.addAttribute("completedProjects", projectService.countByStatus(ProjectStatus.COMPLETED));
        model.addAttribute("planningProjects", projectService.countByStatus(ProjectStatus.PLANNING));
        model.addAttribute("activeNav", "projects");

        return "projects/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "projects");
        return "projects/form";
    }

    @PostMapping
    public String createProject(@Valid @ModelAttribute("project") Project project,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "projects");
            return "projects/form";
        }

        projectService.createProject(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project created successfully!");
        return "redirect:/projects";
    }

    @GetMapping("/{id}")
    public String viewProjectDetails(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("newMilestone", new ProjectMilestone());
        model.addAttribute("activeNav", "projects");
        return "projects/detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "projects");
        return "projects/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProject(@PathVariable Long id,
                                @Valid @ModelAttribute("project") Project project,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ProjectStatus.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "projects");
            return "projects/form";
        }

        projectService.updateProject(id, project);
        redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
        return "redirect:/projects/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully.");
        return "redirect:/projects";
    }

    @PostMapping("/{id}/progress")
    public String updateProgress(@PathVariable Long id,
                                 @RequestParam Integer progressPercentage,
                                 RedirectAttributes redirectAttributes) {
        projectService.updateProgress(id, progressPercentage);
        redirectAttributes.addFlashAttribute("successMessage", "Project progress updated to " + progressPercentage + "%");
        return "redirect:/projects/" + id;
    }

    @PostMapping("/{id}/milestones")
    public String addMilestone(@PathVariable Long id,
                               @ModelAttribute("newMilestone") ProjectMilestone milestone,
                               RedirectAttributes redirectAttributes) {
        if (milestone.getTitle() != null && !milestone.getTitle().trim().isEmpty()) {
            projectService.addMilestone(id, milestone);
            redirectAttributes.addFlashAttribute("successMessage", "Milestone added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Milestone title cannot be empty.");
        }
        return "redirect:/projects/" + id;
    }

    @PostMapping("/milestones/{milestoneId}/toggle")
    public String toggleMilestone(@PathVariable Long milestoneId,
                                  @RequestParam Long projectId,
                                  RedirectAttributes redirectAttributes) {
        ProjectMilestone updated = projectService.toggleMilestone(milestoneId);
        String msg = updated.isCompleted() ? "Milestone marked as completed!" : "Milestone reopened.";
        redirectAttributes.addFlashAttribute("successMessage", msg);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/milestones/{milestoneId}/delete")
    public String deleteMilestone(@PathVariable Long milestoneId,
                                  @RequestParam Long projectId,
                                  RedirectAttributes redirectAttributes) {
        projectService.deleteMilestone(milestoneId);
        redirectAttributes.addFlashAttribute("successMessage", "Milestone removed.");
        return "redirect:/projects/" + projectId;
    }
}
