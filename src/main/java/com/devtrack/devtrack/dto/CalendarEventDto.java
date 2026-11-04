package com.devtrack.devtrack.dto;

import java.time.LocalDate;

public class CalendarEventDto {

    private Long id;
    private String title;
    private LocalDate date;
    private String type; // GOAL, PROJECT, MILESTONE, REMINDER, ACTIVITY
    private String badgeClass; // success, warning, primary, info, danger, secondary
    private String url;
    private String description;
    private boolean completed;

    public CalendarEventDto() {
    }

    public CalendarEventDto(Long id, String title, LocalDate date, String type, String badgeClass, String url, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.badgeClass = badgeClass;
        this.url = url;
        this.description = description;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBadgeClass() {
        return badgeClass;
    }

    public void setBadgeClass(String badgeClass) {
        this.badgeClass = badgeClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
