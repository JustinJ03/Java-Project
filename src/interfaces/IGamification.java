package interfaces;

import java.util.List;
import gamification.Badge;

/**
 * Implements the gamification system. Defines point tracking, badge management,
 * and leaderboard retrieval. Implemented by GamificationModule.
 * The two addPoints signatures demonstrate method overloading.
 */
public interface IGamification {

    /**
     * Adds points to the player's running total and checks badge thresholds.
     * Used by the single-arg call path; the two-arg overload delegates here.
     */
    void addPoints(int points);

    /** Returns the player's current accumulated point total. */
    int totalPoints();

    /** Returns the live list of badges the player has earned so far. */
    List<Badge> awardedBadges();

    /**
     * Reads all scores from scores.txt via DataManager, sorts highest-first,
     * and returns each entry formatted with a rank medal for positions 1–3.
     */
    List<String> leaderboard();
}
