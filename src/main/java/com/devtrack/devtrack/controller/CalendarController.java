package com.devtrack.devtrack.controller;

import com.devtrack.devtrack.dto.CalendarEventDto;
import com.devtrack.devtrack.service.CalendarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Controller
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/calendar")
    public String showCalendar(@RequestParam(required = false) Integer year,
                               @RequestParam(required = false) Integer month,
                               Model model) {
        LocalDate today = LocalDate.now();
        int targetYear = (year != null && year >= 2020 && year <= 2035) ? year : today.getYear();
        int targetMonth = (month != null && month >= 1 && month <= 12) ? month : today.getMonthValue();

        YearMonth ym = YearMonth.of(targetYear, targetMonth);
        List<CalendarEventDto> events = calendarService.getEventsForMonth(targetYear, targetMonth);

        // Group events by day of month
        Map<Integer, List<CalendarEventDto>> eventsByDay = new HashMap<>();
        for (CalendarEventDto event : events) {
            int day = event.getDate().getDayOfMonth();
            eventsByDay.computeIfAbsent(day, k -> new ArrayList<>()).add(event);
        }

        // Calendar grid calculation
        LocalDate firstDayOfMonth = ym.atDay(1);
        int dayOfWeekStart = firstDayOfMonth.getDayOfWeek().getValue(); // 1 = Mon, 7 = Sun
        // Adjust for Sunday start if desired, or Monday start: Monday = 1, Sunday = 7
        int daysInMonth = ym.lengthOfMonth();

        YearMonth prevYm = ym.minusMonths(1);
        YearMonth nextYm = ym.plusMonths(1);

        model.addAttribute("currentYear", targetYear);
        model.addAttribute("currentMonth", targetMonth);
        model.addAttribute("monthName", ym.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("startDayOfWeek", dayOfWeekStart); // 1 = Monday
        model.addAttribute("eventsByDay", eventsByDay);
        model.addAttribute("totalEvents", events.size());
        model.addAttribute("allEvents", events);

        model.addAttribute("prevYear", prevYm.getYear());
        model.addAttribute("prevMonth", prevYm.getMonthValue());
        model.addAttribute("nextYear", nextYm.getYear());
        model.addAttribute("nextMonth", nextYm.getMonthValue());

        model.addAttribute("todayYear", today.getYear());
        model.addAttribute("todayMonth", today.getMonthValue());
        model.addAttribute("todayDay", today.getDayOfMonth());

        model.addAttribute("activeNav", "calendar");

        return "calendar/index";
    }
}
