package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.config.DataInitializer;
import com.devtrack.devtrack.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SettingsController {

    private final GoalRepository goalRepository;
    private final DsaProblemRepository dsaProblemRepository;
    private final ProjectRepository projectRepository;
    private final ReminderRepository reminderRepository;
    private final ActivityRepository activityRepository;
    private final DataInitializer dataInitializer;

    public SettingsController(GoalRepository goalRepository,
                              DsaProblemRepository dsaProblemRepository,
                              ProjectRepository projectRepository,
                              ReminderRepository reminderRepository,
                              ActivityRepository activityRepository,
                              DataInitializer dataInitializer) {
        this.goalRepository = goalRepository;
        this.dsaProblemRepository = dsaProblemRepository;
        this.projectRepository = projectRepository;
        this.reminderRepository = reminderRepository;
        this.activityRepository = activityRepository;
        this.dataInitializer = dataInitializer;
    }

    @GetMapping("/settings")
    public String showSettings(Model model) {
        model.addAttribute("javaVersion", System.getProperty("java.version"));
        model.addAttribute("osName", System.getProperty("os.name"));
        model.addAttribute("activePort", 8085);
        model.addAttribute("totalGoals", goalRepository.count());
        model.addAttribute("totalDsa", dsaProblemRepository.count());
        model.addAttribute("totalProjects", projectRepository.count());
        model.addAttribute("totalReminders", reminderRepository.count());
        model.addAttribute("totalActivities", activityRepository.count());
        model.addAttribute("activeNav", "settings");
        return "settings/index";
    }

    @PostMapping("/settings/reseed")
    public String reseedData(RedirectAttributes redirectAttributes) {
        // Clear all tables
        reminderRepository.deleteAll();
        activityRepository.deleteAll();
        projectRepository.deleteAll();
        dsaProblemRepository.deleteAll();
        goalRepository.deleteAll();

        // Run data initializer
        dataInitializer.run();

        redirectAttributes.addFlashAttribute("successMessage", "Database re-seeded successfully with demo data!");
        return "redirect:/dashboard";
    }
}
