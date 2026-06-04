# Prompt: Render EduSDG UML Diagram as Claude Artifact

> **Instructions for Claude:**
> Wrap the Mermaid class diagram code below inside an HTML page that uses the **Mermaid.js CDN**
> so it renders as a live visual artifact in this chat window.
>
> Requirements for the HTML artifact:
> - Use Mermaid.js from CDN: `https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.min.js`
> - Centre the diagram on the page with a white background
> - Add a title at the top: **"EduSDG – SDG 4 Quality Education | Class Diagram"**
> - Add a colour-coded legend below the title (4 coloured chips):
>   - 🟢 Member 1 – Learning Module
>   - 🔵 Member 2 – Quiz Module
>   - 🟡 Member 3 – Gamification Module
>   - 🟠 Member 4 – Data & Exceptions
> - Set `theme: 'default'` and `classDiagram` font size to 13px
> - Do NOT modify the Mermaid diagram code — paste it exactly as given below

---

## Mermaid Class Diagram Code (paste as-is into the HTML)

```mermaid
classDiagram
    %% ── INTERFACES ──────────────────────────────────────────
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
        +totalPoints() int
        +awardedBadges() List~Badge~
        +leaderboard() List~String~
    }

    class IDataStorage {
        <<interface>>
        +saveScore(String, int) void
        +readScores() List~String[]~
    }

    %% ── ABSTRACT ─────────────────────────────────────────────
    class Question {
        <<abstract>>
        #prompt String
        +getPrompt() String
        +checkAnswer(String) bool*
        +render() JComponent*
    }

    %% ── MEMBER 1 — Learning ──────────────────────────────────
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
        +resetViewed() void
        +isViewed() bool
        +getFormattedContent() String
        +getPageNumber() int
        +getTitle() String
        +getBodyText() String
        +getImagePath() String
        +getKeyFact() String
    }

    class LearningModule {
        -moduleTitle String
        -pages List~ContentPage~
        -currentPage int
        -moduleTopic String
        -finishedByUser bool
        +LearningModule(String, String)
        +loadContent() void
        +displayPage(int) void
        +nextPage() void
        +prevPage() void
        +getTotalPages() int
        +getCurrentPage() int
        +getProgress() double
        +isCompleted() bool
        +reset() void
        +markFinished() void
        +getPage(int) ContentPage
        +getCurrentContentPage() ContentPage
        +getAllPages() List~ContentPage~
    }

    class LearningModulePanel {
        -module LearningModule
        +LearningModulePanel(LearningModule)
        +refresh() void
        +getModule() LearningModule
    }

    %% ── MEMBER 2 — Quiz ─────────────────────────────────────
    class MCQQuestion {
        -options List~String~
        -correctIndex int
        -selectedAnswer String
        +MCQQuestion(String, List, int)
        +checkAnswer(String) bool
        +render() JComponent
        +getOptions() List~String~
        +getSelectedAnswer() String
    }

    class TrueFalseQuestion {
        -correct bool
        -selectedAnswer String
        +TrueFalseQuestion(String, bool)
        +checkAnswer(String) bool
        +render() JComponent
        +getSelectedAnswer() String
    }

    class QuizModule {
        -questions List~Question~
        -correctCount int
        -currentQuestionIndex int
        -playerName String
        -remainingSeconds int
        -gamifModule GamificationModule
        -goToLeaderboardCallback Runnable
        +QuizModule()
        +loadQuestions() List~Question~
        +scorePercent() int
        +motivationalMessage(int) String
        +setGamificationModule(GamificationModule) void
        +setGoToLeaderboardCallback(Runnable) void
        +onShow() void
        +getView() JComponent
    }

    %% ── MEMBER 3 — Gamification ─────────────────────────────
    class Badge {
        -name String
        -description String
        -icon String
        +Badge(String, String)
        +Badge(String, String, String)
        +getName() String
        +getDescription() String
        +getIcon() String
        +equals(Object) bool
        +hashCode() int
    }

    class GamificationModule {
        -points int
        -badges List~Badge~
        -dataManager DataManager
        +GamificationModule()
        +addPoints(int) void
        +addPoints(int, String) void
        +totalPoints() int
        +awardedBadges() List~Badge~
        +leaderboard() List~String~
        +onQuizComplete(String,int,int,int) void
        +getView() JComponent
        +onShow() void
    }

    %% ── MEMBER 4 — Data ─────────────────────────────────────
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
        +saveScore(String, int) void
        +saveScore(UserProfile) void
        +readScores() List~String[]~
    }

    class ProfilePanel {
        -dataManager DataManager
        +ProfilePanel()
        +onShow() void
    }

    %% ── EXCEPTIONS ───────────────────────────────────────────
    class ContentLoadException {
        +ContentLoadException(String)
        +ContentLoadException(String, Throwable)
    }

    class InvalidScoreException {
        +InvalidScoreException(String)
    }

    class DataNotFoundException {
        +DataNotFoundException(String)
        +DataNotFoundException(String, Throwable)
    }

    %% ── ENTRY POINT ──────────────────────────────────────────
    class Main {
        -learningModule LearningModule
        -quizModule QuizModule
        -gamifModule GamificationModule
        -profilePanel ProfilePanel
        +main(String[]) void$
        +showTab(int) void
    }

    %% ── INTERFACE IMPLEMENTATIONS (dashed + hollow triangle) ─
    LearningModule      ..|> IModule
    QuizModule          ..|> IQuiz
    GamificationModule  ..|> IGamification
    DataManager         ..|> IDataStorage

    %% ── INHERITANCE (solid + hollow triangle) ────────────────
    MCQQuestion         --|> Question
    TrueFalseQuestion   --|> Question
    ContentLoadException --|> Exception
    InvalidScoreException --|> Exception
    DataNotFoundException --|> Exception

    %% ── COMPOSITION (filled diamond) ─────────────────────────
    LearningModule "1" *-- "*" ContentPage
    QuizModule     "1" *-- "*" Question
    GamificationModule "1" *-- "*" Badge

    %% ── ASSOCIATION (open arrow) ─────────────────────────────
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

    %% ── THROWS (dashed open arrow) ───────────────────────────
    QuizModule   ..> ContentLoadException  : throws
    DataManager  ..> InvalidScoreException : throws
    DataManager  ..> DataNotFoundException : throws
```

---

> Output only the HTML artifact. No explanation needed.
