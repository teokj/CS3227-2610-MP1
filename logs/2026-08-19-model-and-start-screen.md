# Job Application Model and Start Screen

## Objective

To create the initial ApplyTrack user interface and define the core job application data model that later features would use.

## AI Assistance

I used the AI to guide me through implementing the first static ApplyTrack screen and then designing the `JobApplication` model.

For the model, I asked questions about which fields should be required or optional, how application IDs should work, how enums such as application status and job category should be represented, and how constructor validation should be handled.

The AI suggested validation rules such as rejecting blank company, position and source values, and requiring a category and application date. It also suggested writing JUnit tests for both valid and invalid cases.

## My Decisions

I decided that each job application would have an internal numeric ID and fields for company, position, category, application date, source, status, follow-up date, starred state and notes.

I kept the ID immutable and used sequential IDs managed by the application rather than asking the user to enter an ID.

I also decided that newly created applications should default to `APPLIED`, have no follow-up date, not be starred, and use an empty string for notes.

## Issues and Iteration

I relied on the AI for much of the detailed model structure, but I asked follow-up questions when I was unsure about concepts such as the difference between `null` and an empty string, and why certain validation checks were needed.

The testing approach also evolved. Instead of only testing successful construction, more invalid-input and setter cases were added so that the model behaviour was verified more thoroughly.

## Verification

I ran the JUnit tests in IntelliJ after implementing the model and validation rules.

I checked that valid applications stored their fields correctly, default values were assigned as expected, invalid required values were rejected, and setters behaved correctly.

I also manually launched the application to confirm that the initial ApplyTrack start screen displayed correctly.

## Outcome

The initial ApplyTrack interface was created, and the core `JobApplication` model was implemented with validation, default values and automated tests. This provided a stable foundation for adding application-management features later.