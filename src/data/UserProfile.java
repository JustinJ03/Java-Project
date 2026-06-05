package data;

/**
 * UserProfile.java
 * Holds per-session player information and statistics.
 *
 * OOP concepts:
 *   - Encapsulation: all fields private, accessed via getters/setters
 *   - Abstraction: clients call recordScore() without knowing internal logic
 *
 * Creator : Member 4
 * Tester  : Adriana Umang Anak Nawang (103088)
 */
public class UserProfile {

    // ── Fields ────────────────────────────────────────────────────────────────
    private final String name;          // player name (set at session start)
    private int    lastScore;           // score of the most recent quiz (0-100)
    private int    bestScore;           // highest score achieved this session
    private int    quizzesTaken;        // total number of quizzes completed

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Creates a new profile for the given player.
     * All numeric stats begin at 0.
     *
     * @param name the player's name (cannot be null)
     */
    public UserProfile(String name) {
        // default to 'Guest' if a null or blank name is supplied.
        this.name         = (name != null && !name.trim().isEmpty()) ? name.trim() : "Guest";
        this.lastScore    = 0;
        this.bestScore    = 0;
        this.quizzesTaken = 0;
    }

    // ── recordScore — encapsulates stat-update logic ──────────────────────────

    /**
     * Records the result of a completed quiz.
     * Updates lastScore, increments quizzesTaken, and raises bestScore if the
     * new score is higher.
     *
     * <p>Demonstrates abstraction: the caller just says "record this score" and
     * the object handles all derived-state updates internally.</p>
     *
     * @param score percentage score (0-100)
     */
    public void recordScore(int score) {
        this.lastScore = score;
        this.quizzesTaken++;
        // raise bestScore only when the new score exceeds the previous best.
        if (score > this.bestScore) {
            this.bestScore = score;
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    /** @return the player's name as entered at session start */
    public String getName()          { return name; }

    /** @return score of the last completed quiz */
    public int    getLastScore()     { return lastScore; }

    /** @return highest quiz score achieved in this session */
    public int    getBestScore()     { return bestScore; }

    /** @return total number of quizzes completed in this session */
    public int    getQuizzesTaken()  { return quizzesTaken; }

    // ── toString ──────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return String.format("UserProfile{name='%s', last=%d, best=%d, quizzes=%d}",
                name, lastScore, bestScore, quizzesTaken);
    }
}
