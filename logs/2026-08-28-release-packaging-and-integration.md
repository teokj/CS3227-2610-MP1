# Release Packaging and Final Integration

## Objective

To produce a runnable release JAR for ApplyTrack, verify the application on different environments, fix the remaining navigation/window behaviour issue and perform final code-quality checks.

## AI Assistance

I used the AI to help investigate how to package the JavaFX application as a runnable JAR containing the required JavaFX libraries.

The process required several iterations. The AI first helped identify why the normal Gradle JAR could not be launched directly, then suggested adding a `Main-Class` manifest entry. When further JavaFX runtime problems appeared, the AI guided me towards using the Shadow plugin to create a fat JAR containing the required dependencies.

The AI also suggested using a separate `Launcher` class to start the JavaFX application because launching the `Application` subclass directly from the fat JAR produced a JavaFX runtime error.

I also used the AI to investigate JLink packaging, Java version issues, Windows path problems and the platform-specific JavaFX native libraries included in the generated JAR.

Finally, I asked the AI to help diagnose a UI issue where navigating between screens caused a maximized window to return to a smaller size.

## My Decisions

I decided to generate `release/applytrack.jar` using the Shadow plugin so that the required JavaFX libraries would be packaged with the application.

I kept the JavaFX platform configuration automatic in the final Gradle configuration instead of permanently hardcoding Windows or Linux. This allows the project to resolve the appropriate JavaFX libraries when it is built on a supported operating system.

For screen navigation, I decided to keep the existing JavaFX `Scene` and replace only its root node instead of creating a new `Scene` every time the user changed screens. This preserved the current window size and maximized state.

## Issues and Iteration

Packaging required multiple rounds of troubleshooting.

The initial JAR did not contain the correct entry point. After adding the main class, running it with Java 17 failed because the project had been compiled using Java 25.

Running it with Java 25 then exposed a JavaFX runtime issue because the ordinary JAR did not contain the required JavaFX components.

I then tried JLink. This introduced another problem because the Java installation path contained a space in the Windows username. After configuring the Java home explicitly, the JLink image could be produced successfully.

For the required JAR release, I used the Shadow plugin to build a fat JAR. Launching the JavaFX `Application` class directly still produced a JavaFX runtime error, so a small `Launcher` class was introduced. The launcher calls the existing application entry point and allowed the packaged JAR to run correctly.

I also investigated cross-platform behaviour. JavaFX contains platform-specific native libraries, so a JAR built with Windows JavaFX dependencies contains Windows native files and is not automatically the same as a Linux-targeted JAR. I temporarily generated a Linux-targeted version and tested it under Ubuntu in WSL2 with WSLg before restoring the automatic platform configuration and rebuilding the final Windows release JAR.

Another issue occurred when navigating while the window was maximized. Earlier implementations created a new `Scene` during navigation, which reset the window dimensions. Attempts to restore the maximized state manually were unreliable. The final solution was to reuse the existing `Scene` and replace its root instead.

## Verification

I ran the complete Gradle automated test suite after the final changes and confirmed that all tests passed.

I launched `release/applytrack.jar` using Java 25 on Windows and manually tested the main application workflows.

I also generated a Linux-targeted JAR and launched it successfully using OpenJDK 25 under Ubuntu on WSL2 with WSLg. This was used as an additional compatibility check, although macOS was not available for direct testing.

I manually tested navigation between the main screen, Add Application screen and Application Details screen while the application window was maximized and confirmed that the window state was preserved.

Before merging the release changes, I also used IntelliJ's code formatting and import optimisation tools and reran the automated tests.

## Outcome

ApplyTrack had a runnable release JAR under `release/applytrack.jar` with the required JavaFX dependencies packaged inside it.

The final navigation implementation preserved the application's window size and maximized state, the automated test suite passed, and the release was successfully tested on Windows and in a Linux WSL2 environment.