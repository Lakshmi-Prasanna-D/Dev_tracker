package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.entity.DsaProblem;
import com.devtrack.devtrack.enums.Difficulty;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.DsaTopic;
import com.devtrack.devtrack.enums.Platform;
import com.devtrack.devtrack.service.DsaProblemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dsa")
public class DsaController {

    private final DsaProblemService dsaProblemService;

    public DsaController(DsaProblemService dsaProblemService) {
        this.dsaProblemService = dsaProblemService;
    }

    @GetMapping
    public String listProblems(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) DsaTopic topic,
                               @RequestParam(required = false) Difficulty difficulty,
                               @RequestParam(required = false) DsaStatus status,
                               @RequestParam(required = false) Platform platform,
                               Model model) {
        List<DsaProblem> problems = dsaProblemService.filterProblems(keyword, topic, difficulty, status, platform);

        long totalCount = dsaProblemService.countTotalProblems();
        long solvedCount = dsaProblemService.countByStatus(DsaStatus.SOLVED);
        long attemptedCount = dsaProblemService.countByStatus(DsaStatus.ATTEMPTED);
        long revisitCount = dsaProblemService.countByStatus(DsaStatus.REVISIT);

        long easyCount = dsaProblemService.countByDifficulty(Difficulty.EASY);
        long mediumCount = dsaProblemService.countByDifficulty(Difficulty.MEDIUM);
        long hardCount = dsaProblemService.countByDifficulty(Difficulty.HARD);

        model.addAttribute("problems", problems);
        model.addAttribute("topics", DsaTopic.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("statuses", DsaStatus.values());
        model.addAttribute("platforms", Platform.values());

        model.addAttribute("selectedTopic", topic);
        model.addAttribute("selectedDifficulty", difficulty);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPlatform", platform);
        model.addAttribute("keyword", keyword);

        model.addAttribute("totalCount", totalCount);
        model.addAttribute("solvedCount", solvedCount);
        model.addAttribute("attemptedCount", attemptedCount);
        model.addAttribute("revisitCount", revisitCount);
        model.addAttribute("easyCount", easyCount);
        model.addAttribute("mediumCount", mediumCount);
        model.addAttribute("hardCount", hardCount);
        model.addAttribute("activeNav", "dsa");

        return "dsa/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("problem", new DsaProblem());
        model.addAttribute("topics", DsaTopic.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("statuses", DsaStatus.values());
        model.addAttribute("platforms", Platform.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("activeNav", "dsa");
        return "dsa/form";
    }

    @PostMapping
    public String createProblem(@Valid @ModelAttribute("problem") DsaProblem problem,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("topics", DsaTopic.values());
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("statuses", DsaStatus.values());
            model.addAttribute("platforms", Platform.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("activeNav", "dsa");
            return "dsa/form";
        }

        dsaProblemService.createProblem(problem);
        redirectAttributes.addFlashAttribute("successMessage", "DSA Problem logged successfully!");
        return "redirect:/dsa";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        DsaProblem problem = dsaProblemService.getProblemById(id);
        model.addAttribute("problem", problem);
        model.addAttribute("topics", DsaTopic.values());
        model.addAttribute("difficulties", Difficulty.values());
        model.addAttribute("statuses", DsaStatus.values());
        model.addAttribute("platforms", Platform.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("activeNav", "dsa");
        return "dsa/form";
    }

    @PostMapping("/edit/{id}")
    public String updateProblem(@PathVariable Long id,
                                @Valid @ModelAttribute("problem") DsaProblem problem,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("topics", DsaTopic.values());
            model.addAttribute("difficulties", Difficulty.values());
            model.addAttribute("statuses", DsaStatus.values());
            model.addAttribute("platforms", Platform.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("activeNav", "dsa");
            return "dsa/form";
        }

        dsaProblemService.updateProblem(id, problem);
        redirectAttributes.addFlashAttribute("successMessage", "DSA Problem updated successfully!");
        return "redirect:/dsa";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam DsaStatus status,
                               RedirectAttributes redirectAttributes) {
        dsaProblemService.updateStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Problem marked as " + status.getDisplayName());
        return "redirect:/dsa";
    }

    @PostMapping("/delete/{id}")
    public String deleteProblem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        dsaProblemService.deleteProblem(id);
        redirectAttributes.addFlashAttribute("successMessage", "DSA Problem deleted successfully.");
        return "redirect:/dsa";
    }
}
