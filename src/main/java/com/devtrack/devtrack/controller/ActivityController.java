package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.entity.Activity;
import com.devtrack.devtrack.enums.ActivityType;
import com.devtrack.devtrack.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public String listActivities(@RequestParam(required = false) ActivityType type, Model model) {
        List<Activity> activities;
        if (type != null) {
            activities = activityService.getActivitiesByType(type);
        } else {
            activities = activityService.getAllActivities();
        }

        model.addAttribute("activities", activities);
        model.addAttribute("activityTypes", ActivityType.values());
        model.addAttribute("selectedType", type);
        model.addAttribute("totalActivities", activities.size());
        model.addAttribute("activeNav", "activities");

        return "activities/index";
    }
}
