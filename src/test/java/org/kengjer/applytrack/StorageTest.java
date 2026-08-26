package org.kengjer.applytrack;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;
import org.kengjer.applytrack.model.ApplicationStatus;

import java.nio.file.Path;
import java.time.LocalDate;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    void saveAndLoadApplication_validApplication_fieldsRestored(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");
        Storage storage = new Storage(saveFile);

        ApplicationManager originalManager = new ApplicationManager();

        JobApplication original = originalManager.addApplication(
                "NUS",
                "Software Intern",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 1),
                "NUS"
        );

        original.setStatus(ApplicationStatus.INTERVIEW);
        original.setFollowUpDate(LocalDate.of(2026, 8, 26));
        original.setStarred(true);
        original.setNotes("Follow up | next week\nSecond line");

        storage.saveApplications(originalManager.getApplications());

        ApplicationManager loadedManager = new ApplicationManager();
        storage.loadApplications(loadedManager);

        JobApplication loaded = loadedManager.getApplications().get(0);

        assertEquals(original.getId(), loaded.getId());
        assertEquals(original.getCompany(), loaded.getCompany());
        assertEquals(original.getPosition(), loaded.getPosition());
        assertEquals(original.getCategory(), loaded.getCategory());
        assertEquals(original.getApplicationDate(), loaded.getApplicationDate());
        assertEquals(original.getSource(), loaded.getSource());
        assertEquals(original.getStatus(), loaded.getStatus());
        assertEquals(original.getFollowUpDate(), loaded.getFollowUpDate());
        assertEquals(original.isStarred(), loaded.isStarred());
        assertEquals(original.getNotes(), loaded.getNotes());
    }

    @Test
    void loadApplications_multipleApplications_nextIdContinues(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");
        Storage storage = new Storage(saveFile);

        ApplicationManager originalManager = new ApplicationManager();

        originalManager.addApplication(
                "Company A",
                "Role A",
                JobCategory.TECHNOLOGY,
                LocalDate.of(2026, 8, 1),
                "LinkedIn"
        );

        originalManager.addApplication(
                "Company B",
                "Role B",
                JobCategory.ENGINEERING,
                LocalDate.of(2026, 8, 2),
                "Company Website"
        );

        storage.saveApplications(originalManager.getApplications());

        ApplicationManager loadedManager = new ApplicationManager();
        storage.loadApplications(loadedManager);

        JobApplication newApplication = loadedManager.addApplication(
                "Company C",
                "Role C",
                JobCategory.BUSINESS,
                LocalDate.of(2026, 8, 3),
                "NUS"
        );

        assertEquals(3, loadedManager.getApplications().size());
        assertEquals(3, newApplication.getId());
    }

    @Test
    void loadApplications_fileDoesNotExist_managerRemainsEmpty(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");
        Storage storage = new Storage(saveFile);

        ApplicationManager manager = new ApplicationManager();

        storage.loadApplications(manager);

        assertEquals(0, manager.getApplications().size());
    }

    @Test
    void loadApplications_emptyFile_managerRemainsEmpty(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");
        Files.createFile(saveFile);

        Storage storage = new Storage(saveFile);
        ApplicationManager manager = new ApplicationManager();

        storage.loadApplications(manager);

        assertEquals(0, manager.getApplications().size());
    }

    @Test
    void saveApplications_emptyList_createsEmptyFile(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");
        Storage storage = new Storage(saveFile);

        ApplicationManager manager = new ApplicationManager();

        storage.saveApplications(manager.getApplications());

        assertEquals(0, Files.readAllLines(saveFile).size());
    }

    @Test
    void loadApplications_malformedLine_skipsInvalidRecord(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");

        Files.writeString(saveFile, "this|is|not|a|valid|application");

        Storage storage = new Storage(saveFile);
        ApplicationManager manager = new ApplicationManager();

        storage.loadApplications(manager);

        assertEquals(0, manager.getApplications().size());
    }

    @Test
    void loadApplications_invalidFieldValue_skipsInvalidRecord(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");

        Files.writeString(
                saveFile,
                "1|TlVT|U29mdHdhcmUgSW50ZXJu|INVALID_CATEGORY|2026-08-01|TlVT|APPLIED|null|false|"
        );

        Storage storage = new Storage(saveFile);
        ApplicationManager manager = new ApplicationManager();

        storage.loadApplications(manager);

        assertEquals(0, manager.getApplications().size());
    }

    @Test
    void loadApplications_invalidRecordBeforeValidRecord_loadsValidRecord(@TempDir Path tempDir)
            throws Exception {

        Path saveFile = tempDir.resolve("applications.txt");

        Files.writeString(
                saveFile,
                "1|TlVT|U29mdHdhcmUgSW50ZXJu|INVALID_CATEGORY|2026-08-01|TlVT|APPLIED|null|false|\n"
                        + "2|Q29tcGFueSBC|Um9sZSBC|TECHNOLOGY|2026-08-02|TGlua2VkSW4=|APPLIED|null|false|"
        );

        Storage storage = new Storage(saveFile);
        ApplicationManager manager = new ApplicationManager();

        storage.loadApplications(manager);

        assertEquals(1, manager.getApplications().size());
        assertEquals(2, manager.getApplications().get(0).getId());
        assertEquals("Company B", manager.getApplications().get(0).getCompany());
    }
}
