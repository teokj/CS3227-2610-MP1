# Edit Application

## Objective

To allow users to modify an existing job application after it had already been created.

## AI Assistance

I used the AI to guide me through reusing the existing Add Application form for editing instead of creating a completely separate edit screen.

The AI suggested passing the selected `JobApplication` object into `AddApplicationController`, pre-filling the form fields with the application's current values and changing the screen title to indicate that the user was editing an existing application.

It also helped explain how Save should behave differently depending on whether the form was creating a new application or editing an existing one.

## My Decisions

I decided to reuse the same FXML form for both adding and editing applications to avoid duplicating layout and validation logic.

I kept the application ID unchanged during editing because it is an internal identifier for the same record.

I also allowed fields such as status, follow-up date and notes to be updated while editing.

## Issues and Iteration

One important issue appeared when both the application date and follow-up date were changed.

The model validates that the follow-up date cannot be before the application date. When the application date was updated first, the old follow-up date could temporarily become invalid even if the user's final pair of dates was valid.

The solution was to temporarily clear the existing follow-up date, update the application date and then apply the new follow-up date.

I did not assume the first implementation was correct. I tested a case where both dates moved forward and used the observed failure to refine the update order.

## Verification

I manually tested editing applications by changing different fields and confirming that the updated values were shown correctly after saving.

I specifically tested changing both the application date and follow-up date to confirm that valid date combinations no longer failed because of the temporary intermediate state.

Existing automated tests were also rerun to ensure the model behaviour remained correct.

## Outcome

Existing applications could be opened in the same form used for adding applications, edited and saved without changing their IDs.

The date-update issue was resolved by updating the fields in a way that respected the model's validation rules.