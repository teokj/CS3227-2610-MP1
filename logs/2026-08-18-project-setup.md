# Initial Project Setup

## Objective

To set up the initial ApplyTrack Java desktop project and confirm that the development environment was working correctly.

## AI Assistance

I asked the AI for step-by-step guidance on setting up the project in IntelliJ using Java, JavaFX and Gradle. The AI helped explain where the main source files, resources and module configuration should be placed, and how the initial JavaFX application should be launched.

Most of the detailed setup steps were suggested by the AI. I followed the instructions incrementally rather than asking for the entire project structure at once, and I checked each step in IntelliJ before moving on.

## My Decisions

I kept the project structure simple and used a single main package, `org.kengjer.applytrack`, with JavaFX FXML resources under the corresponding resources directory.

I chose Gradle as the build tool and kept the initial application minimal so that later features could be added incrementally.

## Issues and Iteration

During setup, I relied heavily on the AI to explain what each configuration file and project component was for because I had forgotten some of the JavaFX and Gradle setup details from previous coursework.

Rather than treating the generated setup as automatically correct, I checked whether IntelliJ could recognise the project, whether Gradle synced successfully and whether the initial JavaFX window could launch.

## Verification

I verified the setup by running the application and confirming that a JavaFX window opened successfully.

I also checked the project structure in IntelliJ to confirm that the source and resource folders were recognised correctly and that the project could be built using Gradle.

## Outcome

A working JavaFX Gradle project was created for ApplyTrack. The initial project structure was ready for feature development, with the application able to launch successfully.