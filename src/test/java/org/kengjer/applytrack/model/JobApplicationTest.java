package org.kengjer.applytrack.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class JobApplicationTest {
    @Test
    void constructor_validValues_fieldsStoredCorrectly() {
        JobApplication application = new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        );

        assertEquals(1, application.getId());
        assertEquals("Google", application.getCompany());
        assertEquals("Software Engineer Intern", application.getPosition());
        assertEquals(JobCategory.TECHNOLOGY, application.getCategory());
        assertEquals(LocalDate.of(2026, 8, 19), application.getApplicationDate());
        assertEquals("LinkedIn", application.getSource());
    }

    @Test
    void constructor_validValues_defaultValuesSetCorrectly() {
        JobApplication application = new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        );

        assertEquals(ApplicationStatus.APPLIED, application.getStatus());
        assertNull(application.getFollowUpDate());
        assertFalse(application.isStarred());
        assertEquals("", application.getNotes());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void constructor_invalidCompany_throwsIllegalArgumentException(String company) {
        assertThrows(IllegalArgumentException.class, () -> new JobApplication(
                1,
                company,
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void constructor_invalidPosition_throwsIllegalArgumentException(String position) {
        assertThrows(IllegalArgumentException.class, () -> new JobApplication(
                1,
                "Google",
                position,
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void constructor_invalidSource_throwsIllegalArgumentException(String source) {
        assertThrows(IllegalArgumentException.class, () -> new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                source
        ));
    }

    @Test
    void constructor_nullCategory_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                null,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        ));
    }

    @Test
    void constructor_nullApplicationDate_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                null,
                "LinkedIn"
        ));
    }

    @Test
    void setters_validRequiredValuesAndStatus_fieldsUpdatedCorrectly() {
        JobApplication application = createValidApplication();
        LocalDate updatedDate = LocalDate.of(2026, 8, 20);

        application.setCompany("OpenAI");
        application.setPosition("Software Engineer");
        application.setCategory(JobCategory.ENGINEERING);
        application.setApplicationDate(updatedDate);
        application.setSource("Company Website");
        application.setStatus(ApplicationStatus.INTERVIEW);

        assertAll(
                () -> assertEquals("OpenAI", application.getCompany()),
                () -> assertEquals("Software Engineer", application.getPosition()),
                () -> assertEquals(JobCategory.ENGINEERING, application.getCategory()),
                () -> assertEquals(updatedDate, application.getApplicationDate()),
                () -> assertEquals("Company Website", application.getSource()),
                () -> assertEquals(ApplicationStatus.INTERVIEW, application.getStatus())
        );
    }

    @Test
    void setStarred_trueThenFalse_updatesValue() {
        JobApplication application = createValidApplication();

        application.setStarred(true);
        assertTrue(application.isStarred());

        application.setStarred(false);
        assertFalse(application.isStarred());
    }

    @Test
    void setFollowUpDate_dateThenNull_updatesAndClearsValue() {
        JobApplication application = createValidApplication();
        LocalDate followUpDate = LocalDate.of(2026, 8, 26);

        application.setFollowUpDate(followUpDate);
        assertEquals(followUpDate, application.getFollowUpDate());

        application.setFollowUpDate(null);
        assertNull(application.getFollowUpDate());
    }

    @Test
    void setFollowUpDate_beforeApplicationDate_throwsAndPreservesExistingValue() {
        JobApplication application = createValidApplication();
        LocalDate validFollowUpDate = LocalDate.of(2026, 8, 26);

        application.setFollowUpDate(validFollowUpDate);

        assertThrows(
                IllegalArgumentException.class,
                () -> application.setFollowUpDate(LocalDate.of(2026, 8, 18))
        );

        assertEquals(validFollowUpDate, application.getFollowUpDate());
    }

    @Test
    void setNotes_normalValue_storesValueCorrectly() {
        JobApplication application = createValidApplication();

        application.setNotes("Follow up with the recruiter next week.");

        assertEquals("Follow up with the recruiter next week.", application.getNotes());
    }

    @Test
    void setNotes_null_normalizesToEmptyString() {
        JobApplication application = createValidApplication();
        application.setNotes("Existing notes");

        application.setNotes(null);

        assertEquals("", application.getNotes());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void setCompany_invalidValue_throwsAndPreservesExistingValue(String company) {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setCompany(company));
        assertEquals("Google", application.getCompany());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void setPosition_invalidValue_throwsAndPreservesExistingValue(String position) {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setPosition(position));
        assertEquals("Software Engineer Intern", application.getPosition());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t"})
    void setSource_invalidValue_throwsAndPreservesExistingValue(String source) {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setSource(source));
        assertEquals("LinkedIn", application.getSource());
    }

    @Test
    void setCategory_null_throwsAndPreservesExistingValue() {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setCategory(null));
        assertEquals(JobCategory.TECHNOLOGY, application.getCategory());
    }

    @Test
    void setApplicationDate_null_throwsAndPreservesExistingValue() {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setApplicationDate(null));
        assertEquals(LocalDate.of(2026, 8, 19), application.getApplicationDate());
    }

    @Test
    void setStatus_null_throwsAndPreservesExistingValue() {
        JobApplication application = createValidApplication();

        assertThrows(IllegalArgumentException.class, () -> application.setStatus(null));
        assertEquals(ApplicationStatus.APPLIED, application.getStatus());
    }

    @Test
    void setApplicationDate_afterFollowUpDate_throwsAndPreservesExistingValue() {
        JobApplication application = createValidApplication();
        LocalDate followUpDate = LocalDate.of(2026, 8, 26);

        application.setFollowUpDate(followUpDate);

        assertThrows(
                IllegalArgumentException.class,
                () -> application.setApplicationDate(LocalDate.of(2026, 8, 27))
        );

        assertEquals(LocalDate.of(2026, 8, 19), application.getApplicationDate());
    }

    @Test
    void setStatus_validStatus_updatesValue() {
        JobApplication application = createValidApplication();

        application.setStatus(ApplicationStatus.INTERVIEW);

        assertEquals(ApplicationStatus.INTERVIEW, application.getStatus());
    }


    private JobApplication createValidApplication() {
        return new JobApplication(
                1,
                "Google",
                "Software Engineer Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 19),
                "LinkedIn"
        );
    }
}
