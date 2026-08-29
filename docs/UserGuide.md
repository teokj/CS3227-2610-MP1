# ApplyTrack User Guide

## 1. Introduction

ApplyTrack is a Java desktop application for managing submitted job applications.

It allows users to record application details, update their application status, track follow-up dates, mark important applications as starred and filter the application list.

Application data is saved locally so that it can be restored when ApplyTrack is opened again.

## 2. System Requirements

ApplyTrack requires:

- Java 25
- A supported Windows, Linux or macOS environment
- JavaFX dependencies are included in the generated release JAR

The project uses Gradle for building and testing.

## 3. Running ApplyTrack

### Running the provided release JAR

The release JAR is located at:

```text
release/applytrack.jar
```

Confirm that Java 25 is being used:

```text
java -version
```

If the command reports an older version such as Java 17, run the JAR using the path to an installed Java 25 executable instead.

Run the application from the project directory using Java 25:

```text
java -jar release/applytrack.jar
```

### Building from source

The release JAR can also be generated from the source code using Gradle.

On Windows:

```text
.\gradlew shadowJar
```

On Linux or macOS:

```text
./gradlew shadowJar
```

The generated JAR will be placed at:

```text
release/applytrack.jar
```

Because JavaFX contains platform-specific native libraries, building the project on the target operating system is recommended.

## 4. Main Screen

The main screen displays all saved job applications.

Each application entry shows:

- Application ID
- Company
- Position
- Starred indicator, if applicable
- Follow-up indicator, if the application is overdue, due today or upcoming

Selecting an application opens its Application Details screen.

The main screen also provides controls for:

- Adding a new application
- Filtering applications by status
- Filtering applications by job category
- Filtering applications by follow-up status
- Showing starred applications only
- Resetting all filters

If the application list exceeds the visible area, users can scroll through the list using the scrollbar or mouse wheel.

## 5. Adding an Application

1. Select **Add Application** from the main screen.
2. Enter the required application information.
3. Optionally enter a follow-up date and notes.
4. Select **Save**.

The required fields are:

- Company
- Position
- Job category
- Application date
- Source

The optional fields are:

- Follow-up date
- Notes

A newly created application has the default status **APPLIED** and is not starred.

The follow-up date cannot be earlier than the application date.

If required information is missing or invalid, ApplyTrack displays an error message and does not create the application.

Select **Cancel** to return to the main screen without adding the application.

## 6. Viewing Application Details

Select an application from the main screen to open the Application Details screen.

The details screen displays the saved information for the selected application, including:

- Application ID
- Company
- Position
- Job category
- Application date
- Source
- Application status
- Follow-up date
- Starred state
- Notes

From this screen, the user can:

- Edit the application
- Delete the application
- Star or unstar the application
- Return to the main screen

## 7. Editing an Application

To edit an existing application:

1. Open the application from the main screen.
2. Select **Edit**.
3. Modify the required fields.
4. Select **Save**.

The existing application details are loaded into the form automatically.

The application ID does not change when the application is edited.

During editing, the user can update fields including:

- Company
- Position
- Job category
- Application date
- Source
- Application status
- Follow-up date
- Notes

The same validation rules used when adding an application also apply during editing.

Select **Cancel** to leave the edit screen without saving the changes.

## 8. Deleting an Application

To delete an application:

1. Open the application from the main screen.
2. Select **Delete**.
3. Confirm the deletion in the confirmation dialog.

If the deletion is confirmed, the application is removed and the user is returned to the main screen.

If the deletion is cancelled, the application remains unchanged.

## 9. Changing Application Status

Application status can be updated while editing an existing application.

The available statuses are:

- APPLIED
- INTERVIEW
- OFFER
- REJECTED
- WITHDRAWN

New applications use **APPLIED** by default.

To change the status:

1. Open the application from the main screen.
2. Select **Edit**.
3. Choose a new status.
4. Select **Save**.

## 10. Starring Applications

Applications can be marked as starred to highlight important applications.

To star or unstar an application:

1. Open the application from the main screen.
2. Select the star button on the Application Details screen.

A starred application is shown with a star indicator in the main application list.

The starred state can also be used together with the starred-only filter.

## 11. Filtering Applications

The main screen supports filtering applications by:

- Application status
- Job category
- Follow-up status
- Starred state

Multiple filters can be applied at the same time.

For example, a user can display only starred applications that have the status **INTERVIEW** and belong to a particular job category.

Selecting an "All" option for a filter removes that filtering condition.

The **Reset Filters** control restores all filters to their default values.

Filter selections are preserved while navigating between screens during the same application session.

## 12. Follow-up Tracking

A follow-up date can be assigned to an application when it is created or edited.

ApplyTrack automatically determines the follow-up status based on the current date.

The possible follow-up states are:

- **None** — no follow-up date has been assigned.
- **Overdue** — the follow-up date is before today.
- **Due today** — the follow-up date is today.
- **Upcoming** — the follow-up date is within the next seven days.
- **Future** — the follow-up date is more than seven days away.

Important follow-up states are displayed directly in the main application list.

Applications can also be filtered according to their follow-up status.

## 13. Data Persistence

ApplyTrack automatically loads saved application data when the program starts.

When the application closes normally, the current application data is saved to:

```text
data/applications.txt
```

Saved data includes:

- Application ID
- Company
- Position
- Job category
- Application date
- Source
- Application status
- Follow-up date
- Starred state
- Notes

Application IDs increase sequentially during a session. Deleting an application does not immediately cause its ID to be reused. When ApplyTrack starts, the next ID is determined from the highest ID among the applications loaded from storage.

Users do not need to edit the save file manually.

Application data is saved when ApplyTrack closes normally. If the application is terminated unexpectedly, recent changes may not be written to the save file.

## 14. Testing Instructions

The automated test suite can be run using Gradle.

On Windows:

```text
.\gradlew test
```

On Linux or macOS:

```text
./gradlew test
```

A successful run should end with:

```text
BUILD SUCCESSFUL
```

The automated tests cover areas including:

- Job application validation
- Default values
- Setter behaviour
- Application management
- Filter state
- Follow-up date logic
- File persistence
- Loading malformed or invalid saved records

The application can also be tested manually by launching it and checking the main workflows:

1. Add an application.
2. View its details.
3. Edit its information.
4. Change its status.
5. Star and unstar it.
6. Apply different filter combinations.
7. Set different follow-up dates.
8. Close and reopen the application to verify persistence.
9. Delete an application and confirm that it is removed.