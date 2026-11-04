package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.dto.AnalyticsDto;
import com.devtrack.devtrack.service.AnalyticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/analytics")
    public String analyticsPage(Model model) {
        AnalyticsDto analytics = analyticsService.getAnalyticsData();
        model.addAttribute("analytics", analytics);
        model.addAttribute("activeNav", "analytics");
        return "analytics/index";
    }

    @GetMapping("/api/analytics/data")
    @ResponseBody
    public AnalyticsDto getAnalyticsData() {
        return analyticsService.getAnalyticsData();
    }
}
