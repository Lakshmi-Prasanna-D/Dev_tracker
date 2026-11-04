package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.dto.StreakStatsDto;
import com.devtrack.devtrack.service.StreakService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StreakController {

    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }

    @GetMapping("/streaks")
    public String streakPage(Model model) {
        StreakStatsDto stats = streakService.calculateStreakStats();
        model.addAttribute("stats", stats);
        model.addAttribute("activeNav", "streaks");
        return "streaks/index";
    }

    @GetMapping("/api/streaks/data")
    @ResponseBody
    public StreakStatsDto getStreakData() {
        return streakService.calculateStreakStats();
    }
}
