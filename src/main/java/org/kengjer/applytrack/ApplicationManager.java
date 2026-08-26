package org.kengjer.applytrack;

import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {
    private final List<JobApplication> applications = new ArrayList<>();
    private int nextId = 1;
    private String selectedStatusFilter = "All statuses";
    private String selectedCategoryFilter = "All categories";
    private String selectedFollowUpFilter = "All follow-ups";
    private boolean starredOnlyFilter = false;

    public JobApplication addApplication(String company,
                                         String position,
                                         JobCategory category,
                                         LocalDate applicationDate,
                                         String source) {
        JobApplication application = new JobApplication(
                nextId,
                company,
                position,
                category,
                applicationDate,
                source
        );

        applications.add(application);
        nextId++;

        return application;
    }

    public void loadApplication(JobApplication application) {
        applications.add(application);

        if (application.getId() >= nextId) {
            nextId = application.getId() + 1;
        }
    }

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void removeApplication(JobApplication application) {
        applications.remove(application);
    }

    public String getSelectedStatusFilter() {
        return selectedStatusFilter;
    }

    public void setSelectedStatusFilter(String selectedStatusFilter) {
        this.selectedStatusFilter = selectedStatusFilter;
    }

    public String getSelectedCategoryFilter() {
        return selectedCategoryFilter;
    }

    public void setSelectedCategoryFilter(String selectedCategoryFilter) {
        this.selectedCategoryFilter = selectedCategoryFilter;
    }

    public String getSelectedFollowUpFilter() {
        return selectedFollowUpFilter;
    }

    public void setSelectedFollowUpFilter(String selectedFollowUpFilter) {
        this.selectedFollowUpFilter = selectedFollowUpFilter;
    }

    public boolean isStarredOnlyFilter() {
        return starredOnlyFilter;
    }

    public void setStarredOnlyFilter(boolean starredOnlyFilter) {
        this.starredOnlyFilter = starredOnlyFilter;
    }
}
