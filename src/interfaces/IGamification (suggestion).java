// SUGGESTION (AI-generated reference). Rename to IGamification.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 3
package interfaces;

import java.util.List;
import gamification.Badge;

/** Contract for points, badges, and leaderboard. */
public interface IGamification {
    void addPoints(int points);
    int totalPoints();
    List<Badge> awardedBadges();
    List<String> leaderboard();
}
