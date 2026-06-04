# Prompt: Generate EduSDG UML Class Diagram

> **Instructions for Claude:**
> Generate a **PlantUML class diagram** for the EduSDG Java project described below.
> All class details, field types, method signatures, and relationships are provided — use them exactly.
> Do not invent or add anything not listed here.
>
> **Primary output:** Valid PlantUML code (wrapped in `@startuml` / `@enduml`) that can be pasted directly into https://plantuml.com/plantuml to render the diagram.
>
> **Secondary output (optional):** After the PlantUML block, also produce an equivalent **Mermaid `classDiagram`** block as a backup for GitHub/VS Code rendering.
>
> Apply colour-coded backgrounds by group member using PlantUML `skinparam`:
> - Member 1 (Learning) → light green `#E8F5E9`
> - Member 2 (Quiz) → light blue `#E3F2FD`
> - Member 3 (Gamification) → light amber `#FFF8E1`
> - Member 4 (Data) → light orange `#FFF3E0`
> - Shared/entry point → light grey `#F5F5F5`

---

## Project Context

**Project:** EduSDG – SDG 4 Quality Education Desktop App  
**Language:** Java (JDK 11+)  
**UI Framework:** Java Swing  
**Pattern:** Interface-based modular design — 4 members, each owns 1 interface + 1–4 classes  

---

## Packages and Their Contents

| Package | Contents |
|---|---|
| `learning` | `IModule` *(interface)*, `ContentPage`, `LearningModule`, `LearningModulePanel` |
| `interfaces` | `IQuiz` *(interface)*, `IGamification` *(interface)*, `IDataStorage` *(interface)* |
| `quiz` | `Question` *(abstract)*, `MCQQuestion`, `TrueFalseQuestion`, `QuizModule` |
| `gamification` | `Badge`, `GamificationModule` |
| `data` | `UserProfile`, `DataManager`, `ProfilePanel` |
| `data.exceptions` | `ContentLoadException`, `InvalidScoreException`, `DataNotFoundException` |
| `main` | `Main` |

---

## All Classes, Interfaces, and Signatures

### INTERFACES

```
<<interface>> IModule                          [package: learning]
─────────────────────────────────────────────
+ loadContent() : void
+ displayPage(pageNumber : int) : void
+ nextPage() : void
+ prevPage() : void
+ getTotalPages() : int
+ getCurrentPage() : int
+ getProgress() : double
+ isCompleted() : boolean
+ reset() : void

<<interface>> IQuiz                            [package: interfaces]
─────────────────────────────────────────────
+ loadQuestions() : List<Question>
+ scorePercent() : int
+ motivationalMessage(percent : int) : String

<<interface>> IGamification                    [package: interfaces]
─────────────────────────────────────────────
+ addPoints(points : int) : void
+ totalPoints() : int
+ awardedBadges() : List<Badge>
+ leaderboard() : List<String>

<<interface>> IDataStorage                     [package: interfaces]
─────────────────────────────────────────────
+ saveScore(user : String, score : int) : void
+ readScores() : List<String[]>
```

---

### ABSTRACT CLASS

```
<<abstract>> Question                          [package: quiz]
─────────────────────────────────────────────
# prompt : String
─────────────────────────────────────────────
+ getPrompt() : String
+ {abstract} checkAnswer(response : String) : boolean
+ {abstract} render() : JComponent
```

---

### MEMBER 1 — Learning Module (colour: light green)

```
ContentPage                                    [package: learning]
─────────────────────────────────────────────
- pageNumber : int
- title : String
- bodyText : String
- imagePath : String
- keyFact : String
- viewed : boolean
─────────────────────────────────────────────
+ ContentPage(pageNumber, title, bodyText, imagePath, keyFact)    ← constructor overloading
+ ContentPage(pageNumber, title, bodyText, imagePath)
+ ContentPage(pageNumber, title, bodyText)
+ markViewed() : void
+ resetViewed() : void
+ isViewed() : boolean
+ getFormattedContent() : String
+ getPageNumber() : int
+ getTitle() : String
+ getBodyText() : String
+ getImagePath() : String
+ getKeyFact() : String

LearningModule                                 [package: learning]
─────────────────────────────────────────────
- moduleTitle : String
- pages : List<ContentPage>
- currentPage : int
- moduleTopic : String
- finishedByUser : boolean
─────────────────────────────────────────────
+ LearningModule(moduleTitle : String, moduleTopic : String)
+ loadContent() : void
+ displayPage(pageNumber : int) : void
+ nextPage() : void
+ prevPage() : void
+ getTotalPages() : int
+ getCurrentPage() : int
+ getProgress() : double
+ isCompleted() : boolean
+ reset() : void
+ markFinished() : void
+ getPage(pageNumber : int) : ContentPage
+ getCurrentContentPage() : ContentPage
+ getAllPages() : List<ContentPage>
+ getModuleTitle() : String
+ getModuleTopic() : String

LearningModulePanel                            [package: learning]  [extends JPanel]
─────────────────────────────────────────────
- module : LearningModule
─────────────────────────────────────────────
+ LearningModulePanel(module : LearningModule)
+ refresh() : void
+ getModule() : LearningModule
```

---

### MEMBER 2 — Quiz Module (colour: light blue)

```
MCQQuestion                                    [package: quiz]  [extends Question]
─────────────────────────────────────────────
- options : List<String>
- correctIndex : int
- selectedAnswer : String
─────────────────────────────────────────────
+ MCQQuestion(prompt : String, options : List<String>, correctIndex : int)
+ checkAnswer(response : String) : boolean
+ render() : JComponent
+ getOptions() : List<String>
+ getSelectedAnswer() : String

TrueFalseQuestion                              [package: quiz]  [extends Question]
─────────────────────────────────────────────
- correct : boolean
- selectedAnswer : String
─────────────────────────────────────────────
+ TrueFalseQuestion(prompt : String, correct : boolean)
+ checkAnswer(response : String) : boolean
+ render() : JComponent
+ getSelectedAnswer() : String

QuizModule                                     [package: quiz]  [extends JPanel, implements IQuiz]
─────────────────────────────────────────────
- questions : List<Question>
- correctCount : int
- currentQuestionIndex : int
- isAnswerSubmitted : boolean
- playerName : String
- remainingSeconds : int
- gamifModule : GamificationModule
- goToLeaderboardCallback : Runnable
─────────────────────────────────────────────
+ QuizModule()
+ loadQuestions() : List<Question>           ← throws ContentLoadException
+ scorePercent() : int
+ motivationalMessage(percent : int) : String
+ setGamificationModule(gm : GamificationModule) : void
+ setGoToLeaderboardCallback(callback : Runnable) : void
+ onShow() : void
+ getView() : JComponent
```

---

### MEMBER 3 — Gamification Module (colour: light amber)

```
Badge                                          [package: gamification]
─────────────────────────────────────────────
- name : String
- description : String
- icon : String
─────────────────────────────────────────────
+ Badge(name : String, description : String)             ← constructor overloading
+ Badge(name : String, description : String, icon : String)
+ getName() : String
+ getDescription() : String
+ getIcon() : String
+ equals(obj : Object) : boolean
+ hashCode() : int
+ toString() : String

GamificationModule                             [package: gamification]  [extends JPanel, implements IGamification]
─────────────────────────────────────────────
- points : int
- badges : List<Badge>
- dataManager : DataManager
─────────────────────────────────────────────
+ GamificationModule()
+ addPoints(p : int) : void                              ← method overloading
+ addPoints(p : int, reason : String) : void
+ totalPoints() : int
+ awardedBadges() : List<Badge>
+ leaderboard() : List<String>
+ onQuizComplete(playerName : String, score : int, total : int, timeLeft : int) : void
+ getView() : JComponent
+ onShow() : void
```

---

### MEMBER 4 — Data & Exceptions (colour: light orange)

```
UserProfile                                    [package: data]
─────────────────────────────────────────────
- name : String
- lastScore : int
- bestScore : int
- quizzesTaken : int
─────────────────────────────────────────────
+ UserProfile(name : String)
+ recordScore(score : int) : void
+ getName() : String
+ getLastScore() : int
+ getBestScore() : int
+ getQuizzesTaken() : int
+ toString() : String

DataManager                                    [package: data]  [implements IDataStorage]
─────────────────────────────────────────────
- filePath : String
─────────────────────────────────────────────
+ DataManager(filePath : String)
+ saveScore(user : String, score : int) : void           ← method overloading; throws InvalidScoreException
+ saveScore(profile : UserProfile) : void                ← method overloading; throws InvalidScoreException
+ readScores() : List<String[]>                          ← throws DataNotFoundException

ProfilePanel                                   [package: data]  [extends JPanel]
─────────────────────────────────────────────
- dataManager : DataManager
─────────────────────────────────────────────
+ ProfilePanel()
+ onShow() : void

ContentLoadException                           [package: data.exceptions]  [extends Exception]
─────────────────────────────────────────────
+ ContentLoadException(message : String)
+ ContentLoadException(message : String, cause : Throwable)

InvalidScoreException                          [package: data.exceptions]  [extends Exception]
─────────────────────────────────────────────
+ InvalidScoreException(message : String)

DataNotFoundException                          [package: data.exceptions]  [extends Exception]
─────────────────────────────────────────────
+ DataNotFoundException(message : String)
+ DataNotFoundException(message : String, cause : Throwable)
```

---

### ENTRY POINT (colour: light grey)

```
Main                                           [package: main]  [extends JFrame]
─────────────────────────────────────────────
- learningModule : LearningModule
- quizModule : QuizModule
- gamifModule : GamificationModule
- profilePanel : ProfilePanel
─────────────────────────────────────────────
+ {static} main(args : String[]) : void
+ showTab(idx : int) : void
```

---

## All Relationships

### Interface Implementation (dashed arrow with hollow triangle `..▷`)
```
LearningModule      ..|>  IModule
QuizModule          ..|>  IQuiz
GamificationModule  ..|>  IGamification
DataManager         ..|>  IDataStorage
```

### Inheritance / Extension (solid arrow with hollow triangle `──▷`)
```
MCQQuestion         --|>  Question
TrueFalseQuestion   --|>  Question
ContentLoadException  --|>  Exception
InvalidScoreException --|>  Exception
DataNotFoundException  --|>  Exception
```

### Composition (filled diamond `◆──`)
```
LearningModule      "1" *--  "*" ContentPage        (a module owns its pages)
QuizModule          "1" *--  "*" Question            (a quiz owns its questions)
GamificationModule  "1" *--  "*" Badge               (module owns its badge list)
```

### Association / Uses (open arrow `──>`)
```
Main                -->   LearningModule
Main                -->   QuizModule
Main                -->   GamificationModule
Main                -->   ProfilePanel

LearningModulePanel -->   LearningModule

GamificationModule  -->   DataManager
QuizModule          -->   DataManager
QuizModule          -->   GamificationModule           (dependency injection)

DataManager         -->   UserProfile
ProfilePanel        -->   DataManager
```

### Dependency / Throws (dashed open arrow `..>`)
```
QuizModule          ..>   ContentLoadException         : throws
DataManager         ..>   InvalidScoreException        : throws
DataManager         ..>   DataNotFoundException        : throws
```

---

## Diagram Layout Suggestion

Arrange classes in this approximate grid for readability:

```
Row 1 (top):     IModule    IQuiz    IGamification    IDataStorage
Row 2:           LearningModule    QuizModule    GamificationModule    DataManager
Row 3:           ContentPage       Question      Badge                 UserProfile
Row 4:           LearningModulePanel  MCQQuestion  GamificationModule  ProfilePanel
                                   TrueFalseQuestion
Row 5 (bottom):  ContentLoadException  InvalidScoreException  DataNotFoundException
Right side:      Main
```

- Group Member 1 classes together (green background)
- Group Member 2 classes together (blue background)
- Group Member 3 classes together (amber background)
- Group Member 4 classes together (orange background)
- `Exception` subclasses can be placed at the bottom
- `Main` on the far right, connecting to all 4 top-level modules

---

## OOP Annotations to Add as Notes

Add UML notes (`note`) near the relevant class:

| Class | Note text |
|---|---|
| `ContentPage` | `Constructor overloading: 3 constructors` |
| `Badge` | `Constructor overloading: 2 constructors` |
| `DataManager` | `Method overloading: saveScore(String,int) and saveScore(UserProfile)` |
| `GamificationModule` | `Method overloading: addPoints(int) and addPoints(int,String)` |
| `MCQQuestion` / `TrueFalseQuestion` | `Method overriding: checkAnswer() and render()` |
| `LearningModule` | `Method overriding: all IModule methods` |
| `UserProfile` | `Encapsulation: all fields private; recordScore() abstracts stat updates` |

---

## What NOT to Include

- Do **not** include `JPanel`, `JFrame`, `JComponent`, `Exception` (Java standard library) as full class boxes — only show them if needed for an inheritance arrow target
- Do **not** include any class from the old design: `AppModule`, `AppContext`, `Navigator`, `HomeScreen`, `Theme`, `Screen` — these were **never implemented**
- Do **not** include `Runnable` as a class box

---

## Final Request

1. Output the **complete PlantUML code** starting with `@startuml` and ending with `@enduml`
2. Apply the member colour-coding using PlantUML `skinparam class BackgroundColor` or `<style>` blocks
3. Add a diagram title: `EduSDG – SDG 4 Quality Education | Class Diagram`
4. After the PlantUML block, output the equivalent **Mermaid `classDiagram`** block (simplified — key classes and relationships only, since Mermaid has limited syntax for full class diagrams)
5. Briefly note (1–2 sentences) which UML tool is recommended for rendering this diagram for submission
