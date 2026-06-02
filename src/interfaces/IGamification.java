// Starter file. Implement the // TODO blocks below.
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
