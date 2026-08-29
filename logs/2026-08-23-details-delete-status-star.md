# Application Details, Delete, Status and Star

## Objective

To expand ApplyTrack beyond adding and editing applications by allowing users to view full application details, delete records, update status and star important applications.

## AI Assistance

I used the AI to guide me through creating an application details screen and connecting each application in the main list to that screen.

The AI suggested passing the selected `JobApplication` object into `ApplicationDetailsController` so that the controller could display the application fields directly.

I also asked for help implementing deletion with a confirmation dialog, adding application status support and adding a starred state that could be toggled from the details screen.

## My Decisions

I chose to show the full application information on a separate details screen rather than placing all information directly in the main list.

I decided that deletion should require confirmation so that an application could not be removed accidentally.

For starred applications, I chose a simple visual indicator using a star symbol and allowed users to toggle the state from the details screen.

I also kept status as a predefined enum rather than allowing free-text status values.

## Issues and Iteration

The feature was built incrementally rather than all at once. I first added the details view and navigation, then deletion, followed by status and starred functionality.

I relied on the AI for much of the controller and navigation logic, but I repeatedly ran the application and checked whether each action changed the correct `JobApplication`.

I also checked that deleting an application removed only the selected record and that the remaining applications were still displayed correctly.

## Verification

I manually tested opening different applications from the main list and confirmed that the correct details were displayed.

I tested editing from the details screen, deleting applications with both Yes and No responses in the confirmation dialog, and starring and unstarring applications.

I also reran the existing automated tests to make sure the changes did not break the model or application-management logic.

## Outcome

Users could view the full details of each application, edit or delete it, change its status and mark important applications as starred.

These features made the main application workflow more complete and prepared the project for filtering and follow-up tracking.