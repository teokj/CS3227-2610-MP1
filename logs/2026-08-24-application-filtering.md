# Application Filtering

## Objective

To allow users to narrow the application list using useful filters instead of having to inspect every application manually.

## AI Assistance

I used the AI to guide me through adding filters for application status, job category and starred applications.

The AI suggested storing the selected filter state in `ApplicationManager` so that the chosen filters would remain selected while navigating between screens during the same application session.

I also asked the AI how to combine multiple filters so that an application would only be shown when it satisfied all active conditions.

## My Decisions

I decided to include an `All statuses` option and an `All categories` option so that users could disable individual filters easily.

I also used a checkbox for the starred-only filter because it represented a simple on/off condition.

I chose to apply the filters together rather than treating them independently, so users could combine conditions such as a specific status, category and starred state.

## Issues and Iteration

The filtering logic was developed incrementally and checked after each addition.

I relied on the AI for much of the detailed conditional logic, but I manually checked whether the selected filter values were restored correctly after navigating away from the main screen and returning to it.

I also checked the Reset Filters behaviour to make sure all filter controls and the stored filter state returned to their default values.

## Verification

I manually tested different combinations of status, category and starred filters and checked that only matching applications were displayed.

I also navigated to other screens and returned to the main screen to confirm that the selected filters remained consistent during the session.

The Reset Filters button was tested to confirm that it restored the full application list.

Automated tests for the filter state stored in `ApplicationManager` were also run.

## Outcome

ApplyTrack could filter applications by status, category and starred state, and the filters could be combined.

The selected filter state was preserved during navigation within the same session, and users could restore the default view using Reset Filters.