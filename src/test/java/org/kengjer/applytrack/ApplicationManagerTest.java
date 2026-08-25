package org.kengjer.applytrack;

import org.junit.jupiter.api.Test;
import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationManagerTest {

    @Test
    void addApplication_firstApplication_storedWithIdOne() {
        ApplicationManager manager = new ApplicationManager();

        JobApplication application = manager.addApplication(
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 21),
                "LinkedIn"
        );

        assertEquals(1, application.getId());
        assertEquals(1, manager.getApplications().size());
        assertEquals(application, manager.getApplications().get(0));
    }

    @Test
    void addApplication_multipleApplications_assignsSequentialIds() {
        ApplicationManager manager = new ApplicationManager();

        JobApplication first = addValidApplication(manager, "Google");
        JobApplication second = addValidApplication(manager, "Microsoft");
        JobApplication third = addValidApplication(manager, "OpenAI");

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
        assertEquals(3, third.getId());
    }

    @Test
    void addApplication_multipleApplications_preservesInsertionOrder() {
        ApplicationManager manager = new ApplicationManager();

        JobApplication first = addValidApplication(manager, "Google");
        JobApplication second = addValidApplication(manager, "Microsoft");
        JobApplication third = addValidApplication(manager, "OpenAI");

        assertIterableEquals(List.of(first, second, third), manager.getApplications());
    }

    @Test
    void addApplication_suppliedValues_preservedInStoredApplication() {
        ApplicationManager manager = new ApplicationManager();
        LocalDate applicationDate = LocalDate.of(2026, 8, 22);

        JobApplication application = manager.addApplication(
                "OpenAI",
                "Backend Engineer",
                JobCategory.ENGINEERING,
                applicationDate,
                "Company Website"
        );

        JobApplication storedApplication = manager.getApplications().get(0);
        assertAll(
                () -> assertEquals("OpenAI", storedApplication.getCompany()),
                () -> assertEquals("Backend Engineer", storedApplication.getPosition()),
                () -> assertEquals(JobCategory.ENGINEERING, storedApplication.getCategory()),
                () -> assertEquals(applicationDate, storedApplication.getApplicationDate()),
                () -> assertEquals("Company Website", storedApplication.getSource()),
                () -> assertSame(application, storedApplication)
        );
    }

    @Test
    void addApplication_invalidInput_throwsAndDoesNotStoreApplication() {
        ApplicationManager manager = new ApplicationManager();

        assertThrows(IllegalArgumentException.class, () -> manager.addApplication(
                " ",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 21),
                "LinkedIn"
        ));

        assertTrue(manager.getApplications().isEmpty());
    }

    @Test
    void addApplication_invalidInput_doesNotConsumeId() {
        ApplicationManager manager = new ApplicationManager();

        assertThrows(IllegalArgumentException.class, () -> manager.addApplication(
                null,
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 21),
                "LinkedIn"
        ));

        JobApplication application = addValidApplication(manager, "Google");
        assertEquals(1, application.getId());
    }

    @Test
    void newManager_hasEmptyApplicationList() {
        ApplicationManager manager = new ApplicationManager();

        assertTrue(manager.getApplications().isEmpty());
    }

    @Test
    void separateManagers_generateIdsIndependently() {
        ApplicationManager firstManager = new ApplicationManager();
        ApplicationManager secondManager = new ApplicationManager();

        JobApplication firstApplication = addValidApplication(firstManager, "Google");
        JobApplication secondApplication = addValidApplication(secondManager, "Microsoft");

        assertEquals(1, firstApplication.getId());
        assertEquals(1, secondApplication.getId());
    }

    @Test
    void removeApplication_existingApplication_removesItFromList() {
        ApplicationManager manager = new ApplicationManager();

        JobApplication first = manager.addApplication(
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 23),
                "LinkedIn"
        );

        JobApplication second = manager.addApplication(
                "DBS",
                "Technology Intern",
                JobCategory.FINANCE,
                LocalDate.of(2026, 8, 23),
                "Company Website"
        );

        manager.removeApplication(first);

        assertEquals(1, manager.getApplications().size());
        assertEquals(second, manager.getApplications().get(0));
    }

    @Test
    void removeApplication_thenAddNewApplication_doesNotReuseDeletedId() {
        ApplicationManager manager = new ApplicationManager();

        JobApplication first = manager.addApplication(
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 23),
                "LinkedIn"
        );

        manager.removeApplication(first);

        JobApplication second = manager.addApplication(
                "DBS",
                "Technology Intern",
                JobCategory.FINANCE,
                LocalDate.of(2026, 8, 23),
                "Company Website"
        );

        assertEquals(2, second.getId());
    }

    @Test
    void filterState_defaultValues_areAllFilters() {
        ApplicationManager manager = new ApplicationManager();

        assertEquals("All statuses", manager.getSelectedStatusFilter());
        assertEquals("All categories", manager.getSelectedCategoryFilter());
    }

    @Test
    void setSelectedStatusFilter_validValue_updatesValue() {
        ApplicationManager manager = new ApplicationManager();

        manager.setSelectedStatusFilter("INTERVIEW");

        assertEquals("INTERVIEW", manager.getSelectedStatusFilter());
    }

    @Test
    void setSelectedCategoryFilter_validValue_updatesValue() {
        ApplicationManager manager = new ApplicationManager();

        manager.setSelectedCategoryFilter("TECHNOLOGY");

        assertEquals("TECHNOLOGY", manager.getSelectedCategoryFilter());
    }

    @Test
    void starredOnlyFilter_defaultValue_isFalse() {
        ApplicationManager manager = new ApplicationManager();

        assertFalse(manager.isStarredOnlyFilter());
    }

    @Test
    void setStarredOnlyFilter_trueThenFalse_updatesValue() {
        ApplicationManager manager = new ApplicationManager();

        manager.setStarredOnlyFilter(true);
        assertTrue(manager.isStarredOnlyFilter());

        manager.setStarredOnlyFilter(false);
        assertFalse(manager.isStarredOnlyFilter());
    }

    @Test
    void followUpFilter_defaultValue_isAllFollowUps() {
        ApplicationManager manager = new ApplicationManager();

        assertEquals("All follow-ups", manager.getSelectedFollowUpFilter());
    }

    @Test
    void setSelectedFollowUpFilter_validValue_updatesValue() {
        ApplicationManager manager = new ApplicationManager();

        manager.setSelectedFollowUpFilter("OVERDUE");

        assertEquals("OVERDUE", manager.getSelectedFollowUpFilter());
    }

    private JobApplication addValidApplication(ApplicationManager manager, String company) {
        return manager.addApplication(
                company,
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 21),
                "LinkedIn"
        );
    }
}
