// Starter file. Implement the // TODO blocks below.
package gamification;

import app.AppContext;
import app.AppModule;
import interfaces.IGamification;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.List;

/** Points, badges, timer, leaderboard. Overloaded addPoints demonstrates overloading. */
public class GamificationModule extends AppModule implements IGamification {
    private int points = 0;
    private final List<Badge> badges = new ArrayList<>();

    public GamificationModule(AppContext ctx) { super(ctx, "Gamification"); }

    @Override
    public void addPoints(int p) {
        // TODO(Member 3): points += p; award a Badge when points crosses thresholds
        //   (e.g. 50 -> "Rising Star", 100 -> "Scholar") and add it to 'badges'.
    }

    /** Overloaded: add points with a reason to display/log. */
    public void addPoints(int p, String reason) {
        // TODO(Member 3): call addPoints(p) then record/show 'reason'.
    }

    @Override public int totalPoints() { return points; }
    @Override public List<Badge> awardedBadges() { return badges; }

    @Override
    public List<String> leaderboard() {
        // TODO(Member 3): call ctx.storage().readScores() inside try-catch, sort rows by the
        //   integer score descending, format each as "name - score", return the list.
        return new ArrayList<>();
    }

    @Override
    public JComponent getView() {
        // TODO(Member 3): build the achievements screen - total points, badge icons/list,
        //   a quiz timer, and the leaderboard() entries.
        return placeholder("gamification: badges, points, timer, leaderboard");
    }
}
