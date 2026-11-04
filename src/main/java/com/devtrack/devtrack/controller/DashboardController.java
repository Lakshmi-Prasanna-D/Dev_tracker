package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.dto.DashboardStatsDto;
import com.devtrack.devtrack.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        DashboardStatsDto stats = dashboardService.getDashboardStatistics();
        model.addAttribute("stats", stats);
        model.addAttribute("activeNav", "dashboard");
        return "dashboard/index";
    }
}
