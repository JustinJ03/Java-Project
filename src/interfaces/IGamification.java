package interfaces;

import java.util.List;
import gamification.Badge;

/**
 * Contract for the Gamification module.
 *
 * Implemented by: GamificationModule (Member 3).
 */
public interface IGamification {

    /** Adds points to the player's running total. */
    void addPoints(int points);

    /** Returns the player's current total points. */
    int totalPoints();

    /** Returns all badges the player has earned so far. */
    List<Badge> awardedBadges();

    /**
     * Reads scores.txt via DataManager, sorts by score descending,
     * and returns each entry formatted as "name - score".
     */
    List<String> leaderboard();
}
