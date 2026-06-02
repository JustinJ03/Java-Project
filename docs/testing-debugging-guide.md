# Testing & Debugging Guide — EduSDG

SUGGESTION (AI-generated). Use as a reference; adapt to your team's needs.

---

## Quick start

After renaming your suggestion files to their proper names and implementing your TODOs:

```bat
REM 1. Compile everything including tests
javac -d out src\main\Main.java src\app\*.java src\interfaces\*.java ^
      src\learning\*.java src\quiz\*.java src\gamification\*.java ^
      src\data\*.java src\data\exceptions\*.java ^
      tests\TestSuite.java tests\DebugHelper.java tests\ManualTests.java

REM 2. Run environment diagnostics (run this FIRST if anything seems wrong)
java -cp out tests.DebugHelper

REM 3. Run the full test suite
java -cp out tests.TestSuite

REM 4. Run the original basic tests
java -cp out tests.ManualTests

REM 5. Launch the app
java -cp out main.Main
```

---

## Understanding test labels

| Label | Meaning |
|---|---|
| `[STRUCTURAL]` | Tests class/interface structure — should PASS before you write any logic |
| `[LOGIC]` | Tests runtime behaviour — RED until you implement your `// TODO(Member N)` |

When all `[LOGIC]` tests for your module pass, your core logic is correct.

---

## Debugging workflow (systematic)

Follow these steps in order — do not skip to "try a fix" before completing step 1.

1. **Run DebugHelper first** — it checks your environment (Java version, CWD, file paths, navigation). Most "it doesn't work" problems are setup issues, not logic bugs.
2. **Read the full error** — stack traces tell you the exact file and line. Never ignore line numbers.
3. **Reproduce it** — can you trigger it reliably? If not, gather more information before guessing.
4. **Find root cause** — trace the error backward through the call stack to where the bad value originates.
5. **Form one hypothesis** — write down "I think X fails because Y" before touching code.
6. **Make the smallest possible change** — one thing at a time.
7. **Re-run tests** — did the specific test pass? Did anything else break?

---

## Per-member debugging checklist

### Member 1 — LearningModule, ContentPage (IModule)

**Common problems:**

| Symptom | Root cause | Fix |
|---|---|---|
| `loadPages()` returns empty list | `content.txt` not renamed or path wrong | Rename `content (suggestion).txt` → `content.txt`. Run from project root. |
| `getView()` shows placeholder but not real content | `loadPages()` not called before building UI | Call `loadPages()` inside `getView()` or in constructor before building the panel |
| `NullPointerException` on `pages.get(index)` | `loadPages()` returned empty, then Prev/Next was clicked | Guard: `if (pages.isEmpty()) return;` before accessing by index |
| `IndexOutOfBoundsException` on Prev/Next | `index` goes below 0 or above `pages.size()-1` | Clamp: `index = Math.max(0, Math.min(index, pages.size()-1))` |
| `ContentLoadException` not caught | File read wrapped in exception but caller ignores it | Wrap `loadPages()` call in `try { } catch (ContentLoadException e) { e.printStackTrace(); }` |

**Test to run:** `[STRUCTURAL]` tests in TestSuite should all pass immediately. If they fail, your `ContentPage` constructor or getters have a bug.

**Debug tip:** add `DebugHelper.printPages(loadPages());` at the start of `getView()` to dump the parsed page list to the console.

---

### Member 2 — QuizModule, Question, MCQQuestion, TrueFalseQuestion (IQuiz)

**Common problems:**

| Symptom | Root cause | Fix |
|---|---|---|
| `loadQuestions()` returns empty list | `questions.txt` not renamed or wrong path | Rename `questions (suggestion).txt` → `questions.txt`. CWD must be project root. |
| MCQ answer never accepted | `checkAnswer()` not implemented (returns `false`) | Implement: `return response.trim().equalsIgnoreCase(options.get(correctIndex))` |
| `motivationalMessage()` returns `""` | Method not implemented | Implement the five `if/else if` branches exactly matching the spec scale |
| Off-by-one in score band | `>=80` written as `>80` etc. | Use TestSuite boundary values: 19/20, 39/40, 59/60, 79/80 are the cut-points |
| `scorePercent()` returns 0 even after answers | `correctCount` not incremented | In `getView()` logic, call `correctCount++` when `question.checkAnswer(response)` is `true` |
| `InvalidScoreException` at save | Score out of range, or DataManager not implemented | Ensure `scorePercent()` returns 0–100; catch exception around `ctx.storage().saveScore(...)` |
| Questions parsed as wrong type | Format in questions.txt wrong | Check: MCQ lines start `MCQ|`, TF lines start `TF|`. Split on `|`, not `,` |

**Boundary test — motivationalMessage:** the TestSuite tests ALL 10 boundary values. Run them before finalising:
```
0   → Don't give up!      (lower bound)
19  → Don't give up!      (upper bound of band)
20  → You can do better!  (lower bound of next band)
...
80  → Outstanding!
100 → Outstanding!
```

**Debug tip:** add `System.out.println("Loaded " + questions.size() + " questions");` at the end of `loadQuestions()` to confirm parsing works.

---

### Member 3 — GamificationModule, Badge (IGamification)

**Common problems:**

| Symptom | Root cause | Fix |
|---|---|---|
| Points don't accumulate | `addPoints(int)` TODO not implemented | Implement: `points += p;` then check badge thresholds |
| `addPoints(int, String)` has no effect | Overloaded method doesn't delegate to `addPoints(int)` | Implement: call `addPoints(p);` then handle the `reason` |
| No badges awarded | Threshold logic missing | Inside `addPoints(int)`, after `points += p`, check `if (points >= 50 && !hasBadge("Rising Star")) badges.add(new Badge("Rising Star"))` |
| `leaderboard()` always empty | `readScores()` called but DataManager TODO not implemented | Implement Member 4's DataManager first, then test leaderboard |
| `leaderboard()` not sorted | Rows read but not sorted | After `readScores()`, use `rows.sort((a,b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]))` |
| `ClassCastException` in score sort | Score field is a String | Use `Integer.parseInt(row[1])` to convert before comparing |

**Helper method for badge deduplication:**
```java
private boolean hasBadge(String name) {
    return badges.stream().anyMatch(b -> b.getName().equals(name));
}
```

**Debug tip:** add `System.out.println("Points: " + points + " Badges: " + badges.size());` inside `addPoints()` to trace accumulation.

---

### Member 4 — DataManager, UserProfile, exceptions (IDataStorage)

**Common problems:**

| Symptom | Root cause | Fix |
|---|---|---|
| `readScores()` throws `DataNotFoundException` when file missing | Should return empty list instead | Check `if (!new File(filePath).exists()) return new ArrayList<>();` before reading |
| `saveScore()` silently does nothing | TODO not implemented | Implement with `FileWriter(filePath, true)` (append mode) + `BufferedWriter` |
| `scores.txt` not created | `data/` directory doesn't exist | Add `new File("data").mkdirs();` before writing |
| `saveScore()` doesn't throw for invalid scores | Guard not implemented | Add `if (score < 0 || score > 100) throw new InvalidScoreException("Score out of range: " + score);` |
| Overloaded `saveScore(UserProfile)` has no effect | Doesn't delegate | Implement: `saveScore(profile.getName(), profile.getLastScore());` |
| `readScores()` returns malformed rows | Line format wrong | Write as `user + "," + score + "," + System.currentTimeMillis()`, read with `line.split(",")` |
| `ContentLoadException` unused | Caller doesn't know to use it | Use it in Member 1/2 when `BufferedReader` throws `IOException` reading content/questions: `throw new ContentLoadException("...", ioe)` |

**Template for `saveScore(String, int)`:**
```java
public void saveScore(String user, int score) throws InvalidScoreException {
    if (score < 0 || score > 100) throw new InvalidScoreException("Score out of range: " + score);
    new File("data").mkdirs();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
        bw.write(user + "," + score + "," + System.currentTimeMillis());
        bw.newLine();
    } catch (IOException e) {
        // log but don't let the app crash on a save failure
        System.err.println("Warning: could not save score: " + e.getMessage());
    }
}
```

**Template for `readScores()`:**
```java
public List<String[]> readScores() throws DataNotFoundException {
    List<String[]> result = new ArrayList<>();
    File f = new File(filePath);
    if (!f.exists()) return result;
    try (BufferedReader br = new BufferedReader(new FileReader(f))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) result.add(line.split(","));
        }
    } catch (IOException e) {
        throw new DataNotFoundException("Could not read scores from " + filePath, e);
    }
    return result;
}
```

**Debug tip:** run `java -cp out tests.DebugHelper` — it calls `dumpScores()` which prints all saved rows and reports if `readScores()` throws when it shouldn't.

---

## Common compilation errors

| Error | Likely cause | Fix |
|---|---|---|
| `package X does not exist` | Suggestion file not renamed or `src/` subtree not on compile path | Check `javac` command includes all packages; rename suggestion file |
| `cannot find symbol: class DataManager` | Compiling in wrong order or missing file | Compile all files in one command (use `build.bat`) |
| `class X is public, should be in file X.java` | File still named `X (suggestion).java` | Rename the file — remove ` (suggestion)` from the filename |
| `method does not override` on `@Override` | Interface method signature changed | Match the exact signature in `IModule` / `IQuiz` / `IGamification` / `IDataStorage` |
| `unreported exception` | Checked exception not caught or declared | Wrap in `try-catch` or add `throws` to the method signature |
| `incompatible types: void cannot be converted` | Returning `void` where a value is expected | Add `return result;` or change return type |

---

## Common runtime errors

| Error | Likely cause | Fix |
|---|---|---|
| `NullPointerException` in `getView()` | List not initialised before use | Initialise with `new ArrayList<>()` at field level, not inside the method |
| `IndexOutOfBoundsException` in Prev/Next | `index` out of range | Clamp: `index = Math.max(0, Math.min(index, pages.size()-1))` |
| `NumberFormatException` in leaderboard sort | Score field in file is empty or non-numeric | Guard: `try { Integer.parseInt(row[1]) } catch (NumberFormatException e) { continue; }` |
| `FileNotFoundException` for resources | Running `java` from wrong directory | Run from project root, not from `src/` or `out/` |
| Quiz shows wrong motivational message | Off-by-one in band boundaries | Use boundary table above; test with TestSuite |
| App window too small / too large | `Theme.APP_WIDTH/HEIGHT` changed | Keep at 360×720 as set in `Theme.java` |

---

## File path reference

All paths are **relative to the project root** (the directory containing `src/`, `resources/`, `data/`):

| File | Used by |
|---|---|
| `resources/quiz/questions.txt` | `QuizModule.loadQuestions()` |
| `resources/learning/content.txt` | `LearningModule.loadPages()` |
| `data/scores.txt` | `DataManager` (created at runtime) |

If you run `java` from inside `out/` the paths break. Always run from the project root:
```
cd C:\path\to\Java-Project
java -cp out main.Main
```
