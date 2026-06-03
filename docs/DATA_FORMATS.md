# Data Formats & Storage – EduSDG (Member 4)

## Overview

All persistent data is stored in plain text files under the `data/` directory.
The directory is created automatically at runtime — it is **git-ignored** so test
scores never appear in version control.

No scores are ever hardcoded. All reads and writes go through `DataManager`.

---

## scores.txt — Quiz Score Log

**Location:** `data/scores.txt` (created on first quiz completion)

**Format:** CSV, one row per quiz attempt, no header line.

```
<playerName>,<scorePercent>,<epochMillis>
```

| Field          | Type    | Description                                  |
|----------------|---------|----------------------------------------------|
| `playerName`   | String  | Player name entered at app startup            |
| `scorePercent` | int     | Quiz score as a percentage (0–100)           |
| `epochMillis`  | long    | `System.currentTimeMillis()` at save time    |

**Example:**
```
Alice,85,1717401600000
Bob,42,1717405200000
Alice,91,1717408800000
```

**Written by:** `DataManager.saveScore(String user, int score)`  
**Read by:** `DataManager.readScores()` → `List<String[]>` where each array is `[name, score, timestamp]`

---

## Data Classes (Member 4)

### DataManager  (`src/data/DataManager.java`)

Implements `IDataStorage`. File path is injected via constructor — no hardcoded paths.

```java
DataManager dm = new DataManager("data/scores.txt");
dm.saveScore("Alice", 85);                 // appends one row; throws InvalidScoreException if score < 0 or > 100
dm.saveScore(userProfile);                 // overload: uses profile.getName() + profile.getLastScore()
List<String[]> rows = dm.readScores();     // returns [] if file absent; throws DataNotFoundException if file unreadable
```

### UserProfile  (`src/data/UserProfile.java`)

In-memory model — not written to disk independently (scores persist via DataManager).

```java
UserProfile u = new UserProfile("Alice");
u.recordScore(85);      // updates lastScore, bestScore, quizzesTaken in one call
u.getLastScore();       // 85
u.getBestScore();       // 85
u.getQuizzesTaken();    // 1
u.setLastScore(72);     // manual override (used by DataManager overload)
```

### SessionManager  (`src/data/SessionManager.java`)

Singleton — holds the current player for the whole app session.

```java
SessionManager sm = SessionManager.getInstance();
sm.startSession("Alice");           // call once at app startup (Main.java)
sm.getCurrentPlayer();              // "Alice"
sm.getCurrentUser();                // alias for getCurrentPlayer()
sm.getProfile();                    // the full UserProfile for this session
sm.recordQuizResult(85);            // delegates to profile.recordScore(85)
```

---

## Custom Exceptions  (`src/data/exceptions/`)

| Class                    | Thrown when                                          |
|--------------------------|------------------------------------------------------|
| `InvalidScoreException`  | `saveScore()` receives a score outside 0–100         |
| `DataNotFoundException`  | `readScores()` cannot read an existing scores file   |
| `ContentLoadException`   | A resource file (questions.txt, etc.) fails to load  |

---

## DB-Ready Architecture

The entire storage layer is abstracted behind `IDataStorage`:

```java
public interface IDataStorage {
    void           saveScore(String user, int score) throws InvalidScoreException;
    List<String[]> readScores()                      throws DataNotFoundException;
}
```

To swap in a Java DB (Derby) or any other back-end, implement `IDataStorage` in a
new class (e.g., `DbDataManager`) and change the single construction site in
`DataManager`/`Main.java`. No other module code needs to change — this is the
**Open/Closed Principle** in practice.
