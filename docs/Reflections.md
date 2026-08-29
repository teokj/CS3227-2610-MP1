# Reflections on AI-Assisted Software Engineering

## Introduction

AI was used throughout the development of ApplyTrack as a source of guidance, explanation, implementation suggestions and code review.

In practice, I often relied on the AI for much of the detailed implementation logic, especially when I was unsure how to structure a feature or how JavaFX and Gradle components should interact. I usually worked in small steps by asking whether my current code or approach was correct, then implementing or adapting the AI's suggestions before testing the result.

This project showed me that AI assistance can significantly speed up development, but it does not remove the need to understand and verify the generated suggestions. Several parts of ApplyTrack required further debugging or redesign after the first proposed approach did not fully work.

The following reflections describe three examples where the interaction with AI was particularly useful for understanding the strengths and limitations of AI-assisted software engineering.

## Reflection 1: Editing Applications with Date Validation

### Prompt and Context

While implementing application editing, I encountered a problem when both the application date and follow-up date were changed.

A representative prompt I gave the AI was: "Can you help me check whether my edit logic is correct? When I change both the application date and follow-up date, it sometimes fails even though the final dates should be valid."

The relevant logic initially updated the application date while the existing follow-up date was still stored in the object.

### AI Response and Suggested Approach

The AI explained that the problem was caused by validation occurring during each setter call rather than only after all edited fields had been applied.

For example, an application might originally have:

- Application date: 1 August
- Follow-up date: 10 August

If the user changed both dates to:

- Application date: 15 August
- Follow-up date: 20 August

updating the application date first would temporarily create an invalid state because the old follow-up date of 10 August was earlier than the new application date of 15 August.

The AI suggested temporarily clearing the existing follow-up date before updating the application date, and then setting the new follow-up date afterwards.

The resulting order was:

```java
applicationToEdit.setFollowUpDate(null);
applicationToEdit.setApplicationDate(newApplicationDate);
applicationToEdit.setFollowUpDate(newFollowUpDate);
```

### My Evaluation and Verification

This suggestion made sense after I considered the intermediate state of the object rather than only the final values entered by the user.

I manually tested cases where both dates were moved forward and confirmed that the edit could now be saved successfully.

I also reran the automated tests to ensure that the validation rules were still enforced for genuinely invalid date combinations.

### What I Learned

This example showed me that code can be logically correct when considering only the final intended state but still fail because objects are modified one operation at a time.

It also showed a useful role for AI as a debugging partner. I initially focused on whether the final pair of dates was valid, while the AI helped me consider the temporary state created between setter calls.

However, I still needed to verify that the proposed ordering did not weaken the validation rules. The solution was useful because it preserved the model validation instead of removing it simply to make the edit operation succeed.

In future, I would try to reason about the possible intermediate states of an object before asking the AI for a solution, rather than only checking whether the final intended values are valid.

## Reflection 2: Designing Robust File Persistence

### Prompt and Context

When implementing persistence, I wanted ApplyTrack to save job applications to a text file and restore them when the application was reopened.

A representative prompt I gave the AI was: "Can you help me check whether this storage format is safe? I am separating the fields using `|`, but the notes field can contain text entered by the user."

The initial approach stored each application as one line and separated the fields using the `|` character.

### AI Response and Suggested Approach

The AI initially suggested a delimiter-based format because it was simple and suitable for a small application.

However, when I considered what would happen if the user entered `|` inside a field such as notes, the format became unsafe. Splitting the saved line by `|` could produce more fields than expected and cause the record to be rejected when loading.

Multiline text could also interfere with the assumption that one application should occupy one line in the file.

The AI suggested encoding the free-text fields before saving them. I used Base64 encoding for the company, position, source and notes fields.

For example, instead of saving the raw text directly, the storage logic uses:

```java
private String encode(String text) {
    return Base64.getEncoder()
            .encodeToString(text.getBytes(StandardCharsets.UTF_8));
}
```

The value is decoded again when the application is loaded.

### My Evaluation and Verification

I understood that Base64 was not being used for security or encryption. Its purpose was to convert the free-text values into a representation that would not contain characters that interfered with the delimiter-based file structure.

I manually tested the persistence logic using notes containing a pipe character and multiple lines. I closed the application, reopened it and confirmed that the original text was restored correctly.

I also added automated storage tests using temporary files. These tested normal save and load behaviour, multiple applications, ID continuation, empty and missing files, malformed records and invalid saved values.

### What I Learned

This example showed me that a storage format that appears correct for normal input can fail when users enter unexpected characters.

It also showed that AI-generated solutions still need to be examined for edge cases. A simple delimiter format worked initially, but thinking about realistic user input exposed a weakness that needed to be addressed.

I also learned that Base64 encoding and encryption serve different purposes. In this case, encoding was useful for preserving the file structure, but it does not protect the contents from being read.

In future, I would think about delimiter collisions, multiline input and malformed data earlier when designing a custom text-file format instead of only testing normal values first.

## Reflection 3: Release Packaging and Cross-Platform Troubleshooting

### Prompt and Context

Near the end of the project, I needed to produce a runnable JAR under the `release` folder as required by the assignment.

A representative prompt I gave the AI was: "How do I make my JavaFX application run as a JAR with the required JavaFX libraries included?"

This became more complicated than I expected because several different problems appeared during packaging.

### AI Response and Suggested Approach

The AI first suggested creating a normal Gradle JAR and adding a `Main-Class` entry to the manifest.

After I tried this, running the JAR exposed further issues. One attempt failed because I was using Java 17 to run classes compiled for Java 25. After switching to Java 25, the application still could not launch because the ordinary JAR did not contain the JavaFX runtime components that it needed.

The AI then suggested using additional packaging approaches, including JLink and the Shadow plugin.

JLink introduced another issue because the Java installation path contained a space in my Windows username. I eventually configured the Java home explicitly so that JLink could complete successfully.

For the required release JAR, I used the Shadow plugin to create a fat JAR containing the JavaFX dependencies. However, launching the JavaFX `Application` subclass directly still caused a JavaFX runtime error.

The AI suggested introducing a small `Launcher` class:

```java
public class Launcher {
    public static void main(String[] args) {
        HelloApplication.main(args);
    }
}
```

The JAR manifest then used this launcher as its entry point, which allowed the packaged application to start successfully.

### My Evaluation and Verification

This troubleshooting process showed me that an AI suggestion can be reasonable but still not solve the entire problem because the real environment contains additional constraints.

Instead of assuming each suggested fix was correct, I ran the generated JAR after every change and used the resulting error message to decide what to investigate next.

I successfully launched the final `release/applytrack.jar` using Java 25 on Windows.

I also investigated whether the JavaFX JAR was truly platform-independent. I found that JavaFX includes operating-system-specific native libraries. I temporarily generated a Linux-targeted JAR and tested it using OpenJDK 25 under Ubuntu in WSL2 with WSLg. The application launched successfully there as well.

I then restored the automatic JavaFX platform configuration and rebuilt the final Windows release JAR. I did not have access to macOS for direct testing.

### What I Learned

In my opinion, this was the clearest example in the project of why AI-assisted debugging needs to be iterative.

There was not one prompt that immediately produced the final packaging solution. The process moved through several stages: fixing the JAR entry point, identifying the Java version mismatch, discovering the missing JavaFX dependencies, trying JLink, fixing the Java path issue, using Shadow, and finally introducing a separate launcher.

I learned to treat error messages as evidence rather than simply asking the AI for another piece of code. Giving the AI the specific error and the current configuration made the following suggestions more relevant.

I also learned that the term "cross-platform" can be more complicated for desktop applications using native libraries. Although the Java source is portable, a JavaFX package may still contain platform-specific native components.

In future, I would investigate packaging requirements earlier in development instead of leaving them until most of the application features were complete.

## Overall Reflection

The biggest benefit of using AI during ApplyTrack development was that it allowed me to make progress even when I did not immediately know how to implement a feature or diagnose an error. Asking questions in small steps was more useful for me than requesting the entire application at once because I could test each change before continuing.

At the same time, I became aware that I relied on the AI heavily for detailed implementation logic. Although I sometimes tried to reason about the logic myself before asking whether an approach was correct, this was not consistent throughout the project. In many cases, the AI proposed most of the detailed solution and I concentrated on understanding, implementing and verifying it.

This is an area I would improve in future projects. I would first spend more time writing down my intended logic or pseudocode before asking the AI for an implementation. I could then compare my approach with the AI's suggestion instead of using the AI as the starting point as often.

The project also changed how I view verification. A response that looks convincing is not enough to establish that the implementation is correct. I used automated tests, manual UI testing, Gradle builds, persistence tests and release testing to check the suggestions I received. Some problems, particularly persistence and packaging, only became visible after testing realistic or unusual cases.

Overall, I found AI most useful when treated as an interactive software engineering assistant rather than as an unquestioned code generator. Its explanations and suggestions accelerated my development, but understanding the proposed change, testing it and deciding whether it fitted the requirements remained necessary parts of the development process.