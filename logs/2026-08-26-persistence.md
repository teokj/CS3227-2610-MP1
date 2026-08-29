# Persistence and Storage

## Objective

To make ApplyTrack save application data between sessions instead of losing all applications whenever the program was closed.

## AI Assistance

I used the AI to help design a simple file-based persistence approach using a `Storage` class.

The AI suggested storing each `JobApplication` as one line in a text file and separating the fields using a delimiter. It also helped me implement loading so that saved applications could be reconstructed when the program started.

I asked the AI how IDs should behave after loading existing applications, and it suggested updating the next generated ID based on the largest loaded ID.

The AI also helped me design automated tests using temporary files so that storage tests would not affect my real application data.

## My Decisions

I chose to save the data in `data/applications.txt` and keep the persistence format simple rather than introducing a database.

I decided that the application should load saved data when it starts and save the current application list when it closes normally.

I also chose to exclude the real application data file from Git using `.gitignore`.

## Issues and Iteration

An important issue appeared with the original delimiter-based storage format.

The fields were separated using the `|` character, but free-text fields such as notes could also contain `|`. Multiline notes could create similar parsing problems. This meant that storing the text directly could cause one saved record to be split into the wrong number of fields.

After discussing the issue with the AI, I changed the free-text fields such as company, position, source and notes to Base64 encoding before saving them. This preserves the original text while preventing delimiter characters and newlines from interfering with the file format.

The loading logic was also designed to skip malformed or invalid saved records rather than crashing the entire application.

## Verification

I created automated storage tests using temporary directories and files.

The tests covered saving and loading complete applications, preserving optional fields, loading multiple applications, continuing the ID sequence after loading, missing and empty files, malformed records and invalid field values.

I also manually tested persistence by creating applications, including notes containing a pipe character and multiple lines, closing the application and reopening it to confirm that the data was restored correctly.

After completing the persistence feature, the full Gradle test suite passed.

## Outcome

ApplyTrack could save applications to disk and restore them during the next session.

The storage format was made more robust by encoding free-text values, and automated tests were added to cover both normal and malformed saved data.