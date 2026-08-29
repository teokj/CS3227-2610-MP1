# Follow-up Tracking and Java 25 Migration

## Objective

To add follow-up tracking to job applications and update the project to use Java 25 and JavaFX 25.

## AI Assistance

I used the AI to help design how follow-up dates should be interpreted and displayed. The AI suggested separating follow-up states into categories such as no follow-up, overdue, due today, upcoming and future.

The AI also helped me implement and test the date logic using `LocalDate`, including checking how many days ahead should count as upcoming.

Later in the same development period, I used the AI to guide the migration of the Gradle project from the earlier Java and JavaFX versions to Java 25 and JavaFX 25. This included updating the Gradle toolchain and plugin versions and resolving compatibility issues.

## My Decisions

I decided that follow-ups occurring within the next seven days should be treated as upcoming.

I kept the follow-up calculation based on the current date instead of storing a separate follow-up status permanently, because the status can change automatically as time passes.

I also decided to show the most important follow-up states directly in the main application list, such as overdue, due today and upcoming applications.

For the Java migration, I followed the project requirement to use Java 25 and updated JavaFX to version 25 so that the project versions were consistent.

## Issues and Iteration

Date-based logic required careful testing because the result depends on the boundary between overdue, today, upcoming and future dates.

Instead of testing only with the real current date, the follow-up method accepted a `LocalDate` parameter. This allowed fixed dates to be supplied in automated tests and made the tests deterministic.

The Java 25 migration also required several Gradle-related updates. Some older plugin versions were not appropriate for the newer Java version, so the build configuration had to be adjusted before the project could compile and run successfully again.

I relied on the AI for much of the migration guidance, but I verified each change by rebuilding the project and checking whether the existing application and tests still worked.

## Verification

I tested follow-up dates that were before today, equal to today, within the next seven days and more than seven days in the future.

Automated tests were used to verify the different follow-up states and important date boundaries.

After migrating to Java 25 and JavaFX 25, I ran the full Gradle test suite and manually launched the application to confirm that the existing functionality still worked.

## Outcome

ApplyTrack gained date-based follow-up tracking and could highlight applications requiring attention.

The project was also successfully migrated to Java 25 and JavaFX 25 without breaking the existing features or automated tests.