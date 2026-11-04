package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.entity.Goal;
import com.devtrack.devtrack.enums.GoalCategory;
import com.devtrack.devtrack.enums.GoalStatus;
import com.devtrack.devtrack.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/goals")
public class GoalController {

    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public String listGoals(@RequestParam(required = false) GoalCategory category,
                            @RequestParam(required = false) GoalStatus status,
                            @RequestParam(required = false) String keyword,
                            Model model) {
        List<Goal> goals = goalService.filterGoals(category, status, keyword);

        model.addAttribute("goals", goals);
        model.addAttribute("categories", GoalCategory.values());
        model.addAttribute("statuses", GoalStatus.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalGoals", goalService.countTotalGoals());
        model.addAttribute("completedGoals", goalService.countByStatus(GoalStatus.COMPLETED));
        model.addAttribute("inProgressGoals", goalService.countByStatus(GoalStatus.IN_PROGRESS));
        model.addAttribute("pendingGoals", goalService.countByStatus(GoalStatus.PENDING));
        model.addAttribute("overdueGoals", goalService.getOverdueGoals().size());
        model.addAttribute("activeNav", "goals");

        return "goals/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("goal", new Goal());
        model.addAttribute("categories", GoalCategory.values());
        model.addAttribute("statuses", GoalStatus.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "goals");
        return "goals/form";
    }

    @PostMapping
    public String createGoal(@Valid @ModelAttribute("goal") Goal goal,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", GoalCategory.values());
            model.addAttribute("statuses", GoalStatus.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "goals");
            return "goals/form";
        }

        goalService.createGoal(goal);
        redirectAttributes.addFlashAttribute("successMessage", "Learning goal created successfully!");
        return "redirect:/goals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Goal goal = goalService.getGoalById(id);
        model.addAttribute("goal", goal);
        model.addAttribute("categories", GoalCategory.values());
        model.addAttribute("statuses", GoalStatus.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "goals");
        return "goals/form";
    }

    @PostMapping("/edit/{id}")
    public String updateGoal(@PathVariable Long id,
                             @Valid @ModelAttribute("goal") Goal goal,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", GoalCategory.values());
            model.addAttribute("statuses", GoalStatus.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "goals");
            return "goals/form";
        }

        goalService.updateGoal(id, goal);
        redirectAttributes.addFlashAttribute("successMessage", "Learning goal updated successfully!");
        return "redirect:/goals";
    }

    @PostMapping("/complete/{id}")
    public String markCompleted(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        goalService.markCompleted(id);
        redirectAttributes.addFlashAttribute("successMessage", "Goal marked as completed! Keep up the momentum!");
        return "redirect:/goals";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam GoalStatus status, RedirectAttributes redirectAttributes) {
        goalService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Goal status updated to " + status.getDisplayName());
        return "redirect:/goals";
    }

    @PostMapping("/delete/{id}")
    public String deleteGoal(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        goalService.deleteGoal(id);
        redirectAttributes.addFlashAttribute("successMessage", "Goal deleted successfully.");
        return "redirect:/goals";
    }
}
