package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.entity.Reminder;
import com.devtrack.devtrack.enums.ReminderStatus;
import com.devtrack.devtrack.service.GoalService;
import com.devtrack.devtrack.service.ProjectService;
import com.devtrack.devtrack.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService reminderService;
    private final GoalService goalService;
    private final ProjectService projectService;

    public ReminderController(ReminderService reminderService,
                              GoalService goalService,
                              ProjectService projectService) {
        this.reminderService = reminderService;
        this.goalService = goalService;
        this.projectService = projectService;
    }

    @GetMapping
    public String listReminders(Model model) {
        List<Reminder> reminders = reminderService.getAllReminders();
        List<Reminder> upcoming = reminderService.getUpcomingReminders();
        List<Reminder> overdue = reminderService.getOverdueReminders();

        model.addAttribute("reminders", reminders);
        model.addAttribute("upcomingReminders", upcoming);
        model.addAttribute("overdueReminders", overdue);
        model.addAttribute("pendingCount", reminderService.countPending());
        model.addAttribute("activeNav", "reminders");

        return "reminders/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("reminder", new Reminder());
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("goals", goalService.getAllGoals());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "reminders");
        return "reminders/form";
    }

    @PostMapping
    public String createReminder(@Valid @ModelAttribute("reminder") Reminder reminder,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ReminderStatus.values());
            model.addAttribute("goals", goalService.getAllGoals());
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "reminders");
            return "reminders/form";
        }

        reminderService.createReminder(reminder);
        redirectAttributes.addFlashAttribute("successMessage", "Reminder scheduled successfully!");
        return "redirect:/reminders";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Reminder reminder = reminderService.getReminderById(id);
        model.addAttribute("reminder", reminder);
        model.addAttribute("statuses", ReminderStatus.values());
        model.addAttribute("goals", goalService.getAllGoals());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "reminders");
        return "reminders/form";
    }

    @PostMapping("/edit/{id}")
    public String updateReminder(@PathVariable Long id,
                                 @Valid @ModelAttribute("reminder") Reminder reminder,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("statuses", ReminderStatus.values());
            model.addAttribute("goals", goalService.getAllGoals());
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "reminders");
            return "reminders/form";
        }

        reminderService.updateReminder(id, reminder);
        redirectAttributes.addFlashAttribute("successMessage", "Reminder updated successfully!");
        return "redirect:/reminders";
    }

    @PostMapping("/complete/{id}")
    public String markCompleted(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reminderService.markCompleted(id);
        redirectAttributes.addFlashAttribute("successMessage", "Reminder marked as completed!");
        return "redirect:/reminders";
    }

    @PostMapping("/dismiss/{id}")
    public String dismissReminder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reminderService.dismissReminder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Reminder dismissed.");
        return "redirect:/reminders";
    }

    @PostMapping("/delete/{id}")
    public String deleteReminder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reminderService.deleteReminder(id);
        redirectAttributes.addFlashAttribute("successMessage", "Reminder deleted successfully.");
        return "redirect:/reminders";
    }
}
