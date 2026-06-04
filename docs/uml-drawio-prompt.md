# Prompt: Generate EduSDG UML Diagram for draw.io

> **Instructions for Claude:**
> Using the class diagram data below, output **draw.io-compatible XML** (`<mxGraph>` format)
> for a UML class diagram that can be imported directly into draw.io (diagrams.net).
>
> **How the user will import it:**
> 1. Open https://app.diagrams.net (draw.io)
> 2. Go to **Extras → Edit Diagram**
> 3. Delete any existing content, paste the XML, click **OK**
>
> **Diagram requirements:**
> - Use draw.io's built-in UML class shapes (`shape=mxgraph.uml.interface` for interfaces,
>   `shape=mxgraph.uml.abstract_class` for abstract, plain class rectangles for concrete classes)
> - Apply colour-coded header fills per group member:
>   - **Member 1 (Learning):** header `#A5D6A7`, body `#E8F5E9` (green tones)
>   - **Member 2 (Quiz):** header `#90CAF9`, body `#E3F2FD` (blue tones)
>   - **Member 3 (Gamification):** header `#FFE082`, body `#FFF8E1` (amber tones)
>   - **Member 4 (Data/Exceptions):** header `#FFCC80`, body `#FFF3E0` (orange tones)
>   - **Interfaces:** header `#CE93D8`, body `#F3E5F5` (purple tones)
>   - **Abstract class:** header `#EF9A9A`, body `#FFEBEE` (red tones)
>   - **Main / Entry point:** header `#B0BEC5`, body `#F5F5F5` (grey tones)
> - Show `<<interface>>` and `<<abstract>>` stereotypes in class headers
> - Use dashed lines for `implements`, solid lines for `extends`, solid with filled diamond for composition, solid arrows for association, dashed arrows for throws
> - Arrange classes in this layout (left → right, top → bottom):
>   - **Row 1:** IModule — IQuiz — IGamification — IDataStorage (interfaces, full width)
>   - **Row 2:** LearningModule — QuizModule — GamificationModule — DataManager
>   - **Row 3:** LearningModulePanel / ContentPage — Question(abstract) / MCQ / TrueFalse — Badge — UserProfile / ProfilePanel
>   - **Row 4 (bottom):** ContentLoadException — InvalidScoreException — DataNotFoundException
>   - **Right side:** Main (vertical)

---

## All Classes — Exact Fields and Methods

### Interfaces

| Class | Stereotype | Methods |
|---|---|---|
| `IModule` | `<<interface>>` | `loadContent():void`, `displayPage(int):void`, `nextPage():void`, `prevPage():void`, `getTotalPages():int`, `getCurrentPage():int`, `getProgress():double`, `isCompleted():bool`, `reset():void` |
| `IQuiz` | `<<interface>>` | `loadQuestions():List<Question>`, `scorePercent():int`, `motivationalMessage(int):String` |
| `IGamification` | `<<interface>>` | `addPoints(int):void`, `addPoints(int,String):void`, `totalPoints():int`, `awardedBadges():List<Badge>`, `leaderboard():List<String>` |
| `IDataStorage` | `<<interface>>` | `saveScore(String,int):void`, `readScores():List<String[]>` |

### Abstract Class

| Class | Stereotype | Fields | Methods |
|---|---|---|---|
| `Question` | `<<abstract>>` | `# prompt: String` | `+getPrompt():String`, `+checkAnswer(String):boolean {abstract}`, `+render():JComponent {abstract}` |

### Member 1 — Learning (green)

| Class | Fields | Methods |
|---|---|---|
| `ContentPage` | `- pageNumber:int`, `- title:String`, `- bodyText:String`, `- imagePath:String`, `- keyFact:String`, `- viewed:boolean` | `+ContentPage(int,String,String,String,String)`, `+ContentPage(int,String,String,String)`, `+ContentPage(int,String,String)`, `+markViewed():void`, `+resetViewed():void`, `+isViewed():boolean`, `+getFormattedContent():String`, `+getPageNumber():int`, `+getTitle():String`, `+getBodyText():String`, `+getImagePath():String`, `+getKeyFact():String` |
| `LearningModule` | `- moduleTitle:String`, `- pages:List<ContentPage>`, `- currentPage:int`, `- moduleTopic:String`, `- finishedByUser:boolean` | `+LearningModule(String,String)`, `+loadContent():void`, `+displayPage(int):void`, `+nextPage():void`, `+prevPage():void`, `+getTotalPages():int`, `+getCurrentPage():int`, `+getProgress():double`, `+isCompleted():boolean`, `+reset():void`, `+markFinished():void`, `+getPage(int):ContentPage`, `+getCurrentContentPage():ContentPage`, `+getAllPages():List<ContentPage>` |
| `LearningModulePanel` | `- module:LearningModule` | `+LearningModulePanel(LearningModule)`, `+refresh():void`, `+getModule():LearningModule` |

### Member 2 — Quiz (blue)

| Class | Fields | Methods |
|---|---|---|
| `MCQQuestion` | `- options:List<String>`, `- correctIndex:int`, `- selectedAnswer:String` | `+MCQQuestion(String,List<String>,int)`, `+checkAnswer(String):boolean`, `+render():JComponent`, `+getOptions():List<String>`, `+getSelectedAnswer():String` |
| `TrueFalseQuestion` | `- correct:boolean`, `- selectedAnswer:String` | `+TrueFalseQuestion(String,boolean)`, `+checkAnswer(String):boolean`, `+render():JComponent`, `+getSelectedAnswer():String` |
| `QuizModule` | `- questions:List<Question>`, `- correctCount:int`, `- currentQuestionIndex:int`, `- playerName:String`, `- remainingSeconds:int`, `- gamifModule:GamificationModule`, `- goToLeaderboardCallback:Runnable` | `+QuizModule()`, `+loadQuestions():List<Question>`, `+scorePercent():int`, `+motivationalMessage(int):String`, `+setGamificationModule(GamificationModule):void`, `+setGoToLeaderboardCallback(Runnable):void`, `+onShow():void`, `+getView():JComponent` |

### Member 3 — Gamification (amber)

| Class | Fields | Methods |
|---|---|---|
| `Badge` | `- name:String`, `- description:String`, `- icon:String` | `+Badge(String,String)`, `+Badge(String,String,String)`, `+getName():String`, `+getDescription():String`, `+getIcon():String`, `+equals(Object):boolean`, `+hashCode():int` |
| `GamificationModule` | `- points:int`, `- badges:List<Badge>`, `- dataManager:DataManager` | `+GamificationModule()`, `+addPoints(int):void`, `+addPoints(int,String):void`, `+totalPoints():int`, `+awardedBadges():List<Badge>`, `+leaderboard():List<String>`, `+onQuizComplete(String,int,int,int):void`, `+getView():JComponent`, `+onShow():void` |

### Member 4 — Data (orange)

| Class | Fields | Methods |
|---|---|---|
| `UserProfile` | `- name:String`, `- lastScore:int`, `- bestScore:int`, `- quizzesTaken:int` | `+UserProfile(String)`, `+recordScore(int):void`, `+getName():String`, `+getLastScore():int`, `+getBestScore():int`, `+getQuizzesTaken():int` |
| `DataManager` | `- filePath:String` | `+DataManager(String)`, `+saveScore(String,int):void`, `+saveScore(UserProfile):void`, `+readScores():List<String[]>` |
| `ProfilePanel` | `- dataManager:DataManager` | `+ProfilePanel()`, `+onShow():void` |
| `ContentLoadException` | *(none)* | `+ContentLoadException(String)`, `+ContentLoadException(String,Throwable)` |
| `InvalidScoreException` | *(none)* | `+InvalidScoreException(String)` |
| `DataNotFoundException` | *(none)* | `+DataNotFoundException(String)`, `+DataNotFoundException(String,Throwable)` |

### Entry Point (grey)

| Class | Fields | Methods |
|---|---|---|
| `Main` | `- learningModule:LearningModule`, `- quizModule:QuizModule`, `- gamifModule:GamificationModule`, `- profilePanel:ProfilePanel` | `+main(String[]):void {static}`, `+showTab(int):void` |

---

## All Relationships

### Implements (dashed line + hollow triangle)
```
LearningModule      implements  IModule
QuizModule          implements  IQuiz
GamificationModule  implements  IGamification
DataManager         implements  IDataStorage
```

### Extends / Inheritance (solid line + hollow triangle)
```
MCQQuestion          extends  Question
TrueFalseQuestion    extends  Question
ContentLoadException  extends  Exception
InvalidScoreException extends  Exception
DataNotFoundException  extends  Exception
```

### Composition (solid line + filled diamond on owner side, with multiplicity)
```
LearningModule     1 ◆── * ContentPage
QuizModule         1 ◆── * Question
GamificationModule 1 ◆── * Badge
```

### Association / Uses (solid line + open arrow)
```
Main               ──>  LearningModule
Main               ──>  QuizModule
Main               ──>  GamificationModule
Main               ──>  ProfilePanel
LearningModulePanel ──>  LearningModule
GamificationModule  ──>  DataManager
QuizModule          ──>  DataManager
QuizModule          ──>  GamificationModule
DataManager         ──>  UserProfile
ProfilePanel        ──>  DataManager
```

### Throws (dashed line + open arrow, labelled)
```
QuizModule   - - >  ContentLoadException   label: "throws"
DataManager  - - >  InvalidScoreException  label: "throws"
DataManager  - - >  DataNotFoundException  label: "throws"
```

---

## OOP Annotation Notes to Add

Add draw.io **floating text labels** (yellow sticky note boxes) near the relevant classes:

| Near class | Note text |
|---|---|
| `ContentPage` | `Constructor Overloading: 3 constructors` |
| `Badge` | `Constructor Overloading: 2 constructors` |
| `DataManager` | `Method Overloading: saveScore(String,int) and saveScore(UserProfile)` |
| `GamificationModule` | `Method Overloading: addPoints(int) and addPoints(int,String)` |
| `MCQQuestion` + `TrueFalseQuestion` | `Method Overriding: checkAnswer() and render()` |
| `UserProfile` | `Encapsulation: all fields private; recordScore() abstracts stat updates` |

---

## Output Instructions

1. Output the complete `<mxGraphModel>` XML block
2. Use reasonable automatic positions — draw.io will allow manual re-arrangement after import
3. The XML must be importable via **Extras → Edit Diagram** in draw.io without errors
4. After the XML block, add a short **"How to import"** section with numbered steps for draw.io

---

## Alternative: If draw.io XML is too long

If the full `<mxGraphModel>` XML would exceed your context limit, instead output the
**Mermaid classDiagram** block below and add these import instructions:

> **To import Mermaid into draw.io:**
> 1. Open https://app.diagrams.net
> 2. Click **Extras → Edit Diagram**
> 3. In the format dropdown at the top of the dialog, select **"Mermaid"**
> 4. Paste the Mermaid code below and click **OK**
> 5. draw.io auto-generates the diagram — rearrange as needed

```
classDiagram
    class IModule {
        <<interface>>
        +loadContent() void
        +displayPage(int) void
        +nextPage() void
        +prevPage() void
        +getTotalPages() int
        +getCurrentPage() int
        +getProgress() double
        +isCompleted() bool
        +reset() void
    }
    class IQuiz {
        <<interface>>
        +loadQuestions() List~Question~
        +scorePercent() int
        +motivationalMessage(int) String
    }
    class IGamification {
        <<interface>>
        +addPoints(int) void
        +addPoints(int,String) void
        +totalPoints() int
        +awardedBadges() List~Badge~
        +leaderboard() List~String~
    }
    class IDataStorage {
        <<interface>>
        +saveScore(String,int) void
        +readScores() List~String[]~
    }
    class Question {
        <<abstract>>
        #prompt String
        +getPrompt() String
        +checkAnswer(String) bool*
        +render() JComponent*
    }
    class ContentPage {
        -pageNumber int
        -title String
        -bodyText String
        -imagePath String
        -keyFact String
        -viewed bool
        +ContentPage(int,String,String,String,String)
        +ContentPage(int,String,String,String)
        +ContentPage(int,String,String)
        +markViewed() void
        +isViewed() bool
        +getFormattedContent() String
    }
    class LearningModule {
        -moduleTitle String
        -pages List~ContentPage~
        -currentPage int
        -finishedByUser bool
        +LearningModule(String,String)
        +loadContent() void
        +getTotalPages() int
        +getCurrentPage() int
        +getProgress() double
        +isCompleted() bool
        +markFinished() void
        +getPage(int) ContentPage
    }
    class LearningModulePanel {
        -module LearningModule
        +LearningModulePanel(LearningModule)
        +refresh() void
    }
    class MCQQuestion {
        -options List~String~
        -correctIndex int
        -selectedAnswer String
        +MCQQuestion(String,List,int)
        +checkAnswer(String) bool
        +render() JComponent
        +getSelectedAnswer() String
    }
    class TrueFalseQuestion {
        -correct bool
        -selectedAnswer String
        +TrueFalseQuestion(String,bool)
        +checkAnswer(String) bool
        +render() JComponent
        +getSelectedAnswer() String
    }
    class QuizModule {
        -questions List~Question~
        -correctCount int
        -playerName String
        -remainingSeconds int
        +QuizModule()
        +loadQuestions() List~Question~
        +scorePercent() int
        +motivationalMessage(int) String
        +setGamificationModule(GamificationModule) void
        +onShow() void
    }
    class Badge {
        -name String
        -description String
        -icon String
        +Badge(String,String)
        +Badge(String,String,String)
        +getName() String
        +getDescription() String
        +getIcon() String
        +equals(Object) bool
    }
    class GamificationModule {
        -points int
        -badges List~Badge~
        -dataManager DataManager
        +GamificationModule()
        +addPoints(int) void
        +addPoints(int,String) void
        +totalPoints() int
        +awardedBadges() List~Badge~
        +leaderboard() List~String~
        +onQuizComplete(String,int,int,int) void
        +onShow() void
    }
    class UserProfile {
        -name String
        -lastScore int
        -bestScore int
        -quizzesTaken int
        +UserProfile(String)
        +recordScore(int) void
        +getName() String
        +getLastScore() int
        +getBestScore() int
        +getQuizzesTaken() int
    }
    class DataManager {
        -filePath String
        +DataManager(String)
        +saveScore(String,int) void
        +saveScore(UserProfile) void
        +readScores() List~String[]~
    }
    class ProfilePanel {
        -dataManager DataManager
        +ProfilePanel()
        +onShow() void
    }
    class ContentLoadException {
        +ContentLoadException(String)
        +ContentLoadException(String,Throwable)
    }
    class InvalidScoreException {
        +InvalidScoreException(String)
    }
    class DataNotFoundException {
        +DataNotFoundException(String)
        +DataNotFoundException(String,Throwable)
    }
    class Main {
        -learningModule LearningModule
        -quizModule QuizModule
        -gamifModule GamificationModule
        -profilePanel ProfilePanel
        +main(String[]) void$
        +showTab(int) void
    }

    LearningModule      ..|> IModule
    QuizModule          ..|> IQuiz
    GamificationModule  ..|> IGamification
    DataManager         ..|> IDataStorage
    MCQQuestion         --|> Question
    TrueFalseQuestion   --|> Question
    ContentLoadException --|> Exception
    InvalidScoreException --|> Exception
    DataNotFoundException --|> Exception
    LearningModule      "1" *-- "*" ContentPage
    QuizModule          "1" *-- "*" Question
    GamificationModule  "1" *-- "*" Badge
    Main               --> LearningModule
    Main               --> QuizModule
    Main               --> GamificationModule
    Main               --> ProfilePanel
    LearningModulePanel --> LearningModule
    GamificationModule  --> DataManager
    QuizModule          --> DataManager
    QuizModule          --> GamificationModule
    DataManager         --> UserProfile
    ProfilePanel        --> DataManager
    QuizModule   ..> ContentLoadException  : throws
    DataManager  ..> InvalidScoreException : throws
    DataManager  ..> DataNotFoundException : throws
```
