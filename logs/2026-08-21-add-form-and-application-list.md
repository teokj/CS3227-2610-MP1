# Add Application Form and Application List

## Objective

To allow users to create job applications through the JavaFX interface and display the created applications in the main screen.

## AI Assistance

I used the AI to guide me through creating the Add Application screen, including the FXML layout, controller structure and navigation between the main screen and the form.

The AI also suggested how to connect the form to an `ApplicationManager` that stores applications in memory and generates sequential IDs.

I asked the AI for help deciding which controls were appropriate for each field, such as using `TextField`, `ComboBox`, `DatePicker` and `TextArea`, and how the Save and Cancel buttons should behave.

## My Decisions

I chose to keep the application flow simple by using a single window and switching between screens instead of opening multiple windows.

I decided that the user should provide the company, position, category, application date and source, while follow-up date and notes could be optional.

I also decided that the main screen should display each stored application as a clickable button showing the ID, company and position.

## Issues and Iteration

I implemented the feature incrementally instead of building the entire form at once. I added fields and controller logic in small steps and repeatedly checked whether each part worked before moving on.

I relied on the AI for much of the detailed navigation and controller logic, but I verified the behaviour by running the application after each change.

The application list initially existed only in memory, so I understood that all data would disappear when the application closed. Persistence was deliberately postponed until the core application-management workflow was working.

## Verification

I manually tested the form by adding applications with different values and checking that they appeared correctly in the main application list.

I also checked that the generated IDs increased sequentially and that navigating between the main screen and Add Application screen worked as expected.

Existing automated tests for the model and application manager were rerun to ensure the new feature did not break previous functionality.

## Outcome

Users could add new job applications through the JavaFX form, and the applications were stored in memory and displayed on the main screen.

This completed the first end-to-end workflow in ApplyTrack which is entering application information, saving it and viewing the resulting application in the list.