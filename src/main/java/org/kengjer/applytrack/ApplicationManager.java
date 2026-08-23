package org.kengjer.applytrack;

import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {
    private final List<JobApplication> applications = new ArrayList<>();
    private int nextId = 1;

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

    public List<JobApplication> getApplications() {
        return applications;
    }

    public void removeApplication(JobApplication application) {
        applications.remove(application);
    }
}
