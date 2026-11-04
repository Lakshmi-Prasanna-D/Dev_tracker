package com.devtrack.devtrack.entity;

import com.devtrack.devtrack.enums.Difficulty;
import com.devtrack.devtrack.enums.DsaStatus;
import com.devtrack.devtrack.enums.DsaTopic;
import com.devtrack.devtrack.enums.Platform;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dsa_problems")
public class DsaProblem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Problem title is required")
    @Column(nullable = false, length = 250)
    private String title;

    @NotNull(message = "Platform is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Platform platform;

    @Column(name = "problem_url", length = 500)
    private String problemUrl;

    @NotNull(message = "Topic is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DsaTopic topic;

    @NotNull(message = "Difficulty is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Difficulty difficulty;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DsaStatus status = DsaStatus.ATTEMPTED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "attempted_date")
    private LocalDate attemptedDate;

    @Column(name = "solved_date")
    private LocalDate solvedDate;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public DsaProblem() {
    }

    public DsaProblem(String title, Platform platform, String problemUrl, DsaTopic topic,
                      Difficulty difficulty, DsaStatus status, String notes) {
        this.title = title;
        this.platform = platform;
        this.problemUrl = problemUrl;
        this.topic = topic;
        this.difficulty = difficulty;
        this.status = status != null ? status : DsaStatus.ATTEMPTED;
        this.notes = notes;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
        if (this.attemptedDate == null) {
            this.attemptedDate = LocalDate.now();
        }
        if (this.status == DsaStatus.SOLVED && this.solvedDate == null) {
            this.solvedDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (this.status == DsaStatus.SOLVED && this.solvedDate == null) {
            this.solvedDate = LocalDate.now();
        } else if (this.status != DsaStatus.SOLVED) {
            this.solvedDate = null;
        }
    }

    // Getters and Setters

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

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getProblemUrl() {
        return problemUrl;
    }

    public void setProblemUrl(String problemUrl) {
        this.problemUrl = problemUrl;
    }

    public DsaTopic getTopic() {
        return topic;
    }

    public void setTopic(DsaTopic topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public DsaStatus getStatus() {
        return status;
    }

    public void setStatus(DsaStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getAttemptedDate() {
        return attemptedDate;
    }

    public void setAttemptedDate(LocalDate attemptedDate) {
        this.attemptedDate = attemptedDate;
    }

    public LocalDate getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(LocalDate solvedDate) {
        this.solvedDate = solvedDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
