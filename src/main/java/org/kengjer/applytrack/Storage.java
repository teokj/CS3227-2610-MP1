package org.kengjer.applytrack;

import org.kengjer.applytrack.model.ApplicationStatus;
import org.kengjer.applytrack.model.JobApplication;
import org.kengjer.applytrack.model.JobCategory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Storage {
    private final Path saveFile;

    public Storage() {
        this(Paths.get("data", "applications.txt"));
    }

    public Storage(Path saveFile) {
        this.saveFile = saveFile;
    }

    public void saveApplications(List<JobApplication> applications) throws IOException {
        Files.createDirectories(saveFile.getParent());

        List<String> lines = new ArrayList<>();

        for (JobApplication application : applications) {
            lines.add(application.getId()
                    + "|" + encode(application.getCompany())
                    + "|" + encode(application.getPosition())
                    + "|" + application.getCategory()
                    + "|" + application.getApplicationDate()
                    + "|" + encode(application.getSource())
                    + "|" + application.getStatus()
                    + "|" + application.getFollowUpDate()
                    + "|" + application.isStarred()
                    + "|" + encode(application.getNotes()));
        }

        Files.write(saveFile, lines);
    }

    public void loadApplications(ApplicationManager manager) throws IOException {
        if (!Files.exists(saveFile)) {
            return;
        }

        List<String> lines = Files.readAllLines(saveFile);

        for (String line : lines) {
            String[] parts = line.split("\\|", -1);

            if (parts.length != 10) {
                continue;
            }

            try {
                int id = Integer.parseInt(parts[0]);
                String company = decode(parts[1]);
                String position = decode(parts[2]);
                JobCategory category = JobCategory.valueOf(parts[3]);
                LocalDate applicationDate = LocalDate.parse(parts[4]);
                String source = decode(parts[5]);
                ApplicationStatus status = ApplicationStatus.valueOf(parts[6]);

                LocalDate followUpDate = parts[7].equals("null")
                        ? null
                        : LocalDate.parse(parts[7]);

                boolean starred = Boolean.parseBoolean(parts[8]);
                String notes = decode(parts[9]);

                JobApplication application = new JobApplication(
                        id,
                        company,
                        position,
                        category,
                        applicationDate,
                        source
                );

                application.setStatus(status);
                application.setFollowUpDate(followUpDate);
                application.setStarred(starred);
                application.setNotes(notes);

                manager.loadApplication(application);

            } catch (IllegalArgumentException exception) {
                // Skip invalid saved records.
            }
        }
    }

    private String encode(String text) {
        return Base64.getEncoder()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private String decode(String text) {
        return new String(
                Base64.getDecoder().decode(text),
                StandardCharsets.UTF_8
        );
    }
}
