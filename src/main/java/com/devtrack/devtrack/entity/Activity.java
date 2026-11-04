package com.devtrack.devtrack.entity;

import com.devtrack.devtrack.enums.ActivityType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activities", indexes = {
    @Index(name = "idx_activity_timestamp", columnList = "timestamp")
})
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false, length = 50)
    private ActivityType activityType;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "reference_type", length = 50)
    private String referenceType;

    @Column(name = "reference_id")
    private Long referenceId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Activity() {
    }

    public Activity(User user, ActivityType activityType, String description, String referenceType, Long referenceId) {
        this.user = user;
        this.activityType = activityType;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.timestamp = LocalDateTime.now();
    }

    public Activity(User user, ActivityType activityType, String description, String referenceType, Long referenceId, LocalDateTime timestamp) {
        this.user = user;
        this.activityType = activityType;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    public Activity(ActivityType activityType, String description, String referenceType, Long referenceId) {
        this.activityType = activityType;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.timestamp = LocalDateTime.now();
    }

    public Activity(ActivityType activityType, String description, String referenceType, Long referenceId, LocalDateTime timestamp) {
        this.activityType = activityType;
        this.description = description;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
