package org.kengjer.applytrack.model;

import java.time.LocalDate;

public class JobApplication {
    private final int id;
    private String company;
    private String position;
    private JobCategory category;
    private LocalDate applicationDate;
    private String source;
    private ApplicationStatus status;
    private LocalDate followUpDate;
    private boolean starred;
    private String notes;

    public JobApplication(int id, String company, String position,
                          JobCategory category, LocalDate applicationDate,
                          String source) {

        if (company == null || company.isBlank()) {
            throw new IllegalArgumentException("Company cannot be blank.");
        }

        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Position cannot be blank.");
        }

        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        if (applicationDate == null) {
            throw new IllegalArgumentException("Application date cannot be null.");
        }

        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source cannot be blank.");
        }

        this.id = id;
        this.company = company;
        this.position = position;
        this.category = category;
        this.applicationDate = applicationDate;
        this.source = source;

        this.status = ApplicationStatus.APPLIED;
        this.followUpDate = null;
        this.starred = false;
        this.notes = "";
    }

    public int getId() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public String getPosition() {
        return position;
    }

    public JobCategory getCategory() {
        return category;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getSource() {
        return source;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDate getFollowUpDate() {
        return followUpDate;
    }

    public boolean isStarred() {
        return starred;
    }

    public String getNotes() {
        return notes;
    }

    public void setCompany(String company) {
        if (company == null || company.isBlank()) {
            throw new IllegalArgumentException("Company cannot be blank.");
        }
        this.company = company;
    }

    public void setPosition(String position) {
        if (position == null || position.isBlank()) {
            throw new IllegalArgumentException("Position cannot be blank.");
        }
        this.position = position;
    }

    public void setCategory(JobCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }
        this.category = category;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        if (applicationDate == null) {
            throw new IllegalArgumentException("Application date cannot be null.");
        }

        if (followUpDate != null && followUpDate.isBefore(applicationDate)) {
            throw new IllegalArgumentException(
                    "Application date cannot be after follow-up date."
            );
        }

        this.applicationDate = applicationDate;
    }

    public void setSource(String source) {
        if (source == null || source.isBlank()) {
            throw new IllegalArgumentException("Source cannot be blank.");
        }
        this.source = source;
    }

    public void setStatus(ApplicationStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = status;
    }

    public void setFollowUpDate(LocalDate followUpDate) {
        if (followUpDate != null && followUpDate.isBefore(applicationDate)) {
            throw new IllegalArgumentException(
                    "Follow-up date cannot be before application date."
            );
        }
        this.followUpDate = followUpDate;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public void setNotes(String notes) {
        if (notes == null) {
            this.notes = "";
        } else {
            this.notes = notes;
        }
    }

    public FollowUpStatus getFollowUpStatus(LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("Today cannot be null.");
        }

        if (followUpDate == null) {
            return FollowUpStatus.NONE;
        }

        if (followUpDate.isBefore(today)) {
            return FollowUpStatus.OVERDUE;
        }

        if (followUpDate.isEqual(today)) {
            return FollowUpStatus.DUE_TODAY;
        }

        if (!followUpDate.isAfter(today.plusDays(7))) {
            return FollowUpStatus.UPCOMING;
        }

        return FollowUpStatus.FUTURE;
    }
}

