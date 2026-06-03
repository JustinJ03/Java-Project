package gamification;

import data.DataManager;
import data.exceptions.DataNotFoundException;
import interfaces.IGamification;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * TASK: Member 3 - Gamification Module
 *
 * PURPOSE:
 *   Keeps track of the player's points and badges, displays a leaderboard,
 *   and shows an optional countdown timer during a quiz session.
 *
 * EXTENDS: JPanel      -- lightweight Swing container for Main's CardLayout
 * IMPLEMENTS: IGamification -- addPoints(), totalPoints(), awardedBadges(), leaderboard().
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Inheritance     : extends JPanel
 *   - Overriding      : all IGamification methods
 *   - Method overloading : addPoints(int) AND addPoints(int, String reason)
 *   - Exception handling : DataNotFoundException from readScores()
 */
public class GamificationModule extends JPanel implements IGamification {

    // === FIELDS ===
    private int points = 0;
    private final List<Badge> badges = new ArrayList<>();

    // === CONSTRUCTOR ===
    /**
     * No-arg constructor required for Main's CardLayout management.
     * Main instantiates modules directly and adds them to the CardLayout.
     */
    public GamificationModule() {
        super();
    }

    // =========================================================
    // METHOD: addPoints(int p)  [IGamification]
    // =========================================================
    /**
     * Adds points to the player's total and awards badges at thresholds.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: points += p;
     *
     *   STEP 2: Check thresholds and award badges if not already held:
     *             if (points >= 50 && !hasBadge("Rising Star"))
     *                 badges.add(new Badge("Rising Star", "Earned 50 points"));
     *             if (points >= 100 && !hasBadge("Scholar"))
     *                 badges.add(new Badge("Scholar", "Earned 100 points"));
     *             (Add more thresholds as you like.)
     *
     * OOP NOTE: This is one overload of addPoints. The other overload
     *           (with a String reason) should call this method.
     */
    @Override
    public void addPoints(int p) {
        // TODO(Member 3): implement using the steps above.
    }

    // =========================================================
    // METHOD: addPoints(int p, String reason)  [OVERLOAD]
    // =========================================================
    /**
     * Overloaded version: adds points and logs/displays the reason.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Call addPoints(p);  (delegates to the single-arg version above)
     *   STEP 2: Optionally print or display the reason string, e.g.:
     *             System.out.println("+" + p + " pts: " + reason);
     *
     * OOP NOTE: Method overloading -- same name, different parameter list.
     */
    public void addPoints(int p, String reason) {
        // TODO(Member 3): delegate to addPoints(p) then handle the reason.
    }

    // === Simple getters (already working) ===
    @Override public int         totalPoints()   { return points; }
    @Override public List<Badge> awardedBadges() { return badges; }

    // =========================================================
    // METHOD: leaderboard()  [IGamification]
    // =========================================================
    /**
     * Reads all saved scores and returns them sorted highest-first.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Read scores with a try-catch:
     *             try {
     *                 List<String[]> rows = DataManager.getInstance().readScores();
     *             } catch (DataNotFoundException e) {
     *                 return new ArrayList<>(); // no scores yet
     *             }
     *
     *   STEP 2: Sort by score (column 1) descending:
     *             rows.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
     *
     *   STEP 3: Format each row as "name - score":
     *             List<String> result = new ArrayList<>();
     *             for (String[] row : rows) result.add(row[0] + " - " + row[1]);
     *             return result;
     *
     * OOP NOTE: Uses the IDataStorage interface -- polymorphism through the interface.
     */
    @Override
    public List<String> leaderboard() {
        // TODO(Member 3): implement using the steps above.
        return new ArrayList<>();
    }

    // =========================================================
    // METHOD: getView()  [DEPRECATED -- GamificationModule now IS a JPanel]
    // =========================================================
    /**
     * Builds and returns the Gamification Module's Swing panel.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Create a main JPanel (BorderLayout).
     *
     *   STEP 2: Points label at NORTH:
     *             JLabel pts = new JLabel("Points: " + totalPoints(), SwingConstants.CENTER);
     *
     *   STEP 3: Badges panel in the CENTER -- loop through awardedBadges()
     *             and show each badge name as a JLabel or icon.
     *             If no badges: show "No badges yet."
     *
     *   STEP 4: Leaderboard at SOUTH -- loop through leaderboard() entries
     *             and display them in a JList or stacked JLabels.
     *
     *   STEP 5: (Optional) Add a countdown timer:
     *             Use javax.swing.Timer to tick every 1000ms.
     *             Timer t = new Timer(1000, e -> { ... decrement counter ... });
     *             t.start();
     *
     *   STEP 6: Return the main panel.
     *
     * OOP NOTE: Previously overrode AppModule.getView(). Now GamificationModule extends JPanel,
     *           so build the UI directly in the constructor or an init() method.
     */
    @Override
    public JComponent getView() {
        // TODO(Member 3): This method is for backward compatibility. Build UI in constructor instead.
        return new JPanel();
    }

    // =========================================================
    // METHOD: onShow()  [OVERRIDE -- optional]
    // =========================================================
    /**
     * Refresh the panel when the user navigates to this module.
     *
     * HOW TO IMPLEMENT:
     *   Re-fetch the leaderboard and repaint if you hold a reference
     *   to the leaderboard JList/panel as a field.
     *
     * OOP NOTE: Overrides AppModule.onShow().
     */
    @Override
    public void onShow() {
        // TODO(Member 3): optionally refresh leaderboard display.
    }

    // =========================================================
    // PRIVATE HELPER: hasBadge(String name)
    // =========================================================
    /**
     * Returns true if the player already holds a badge with the given name.
     * Use this inside addPoints() to avoid awarding the same badge twice.
     *
     * HOW TO IMPLEMENT:
     *   return badges.stream().anyMatch(b -> b.getName().equals(name));
     *
     * NOTE: This helper is already here for you -- implement it and call it
     *       from addPoints().
     */
    private boolean hasBadge(String name) {
        // TODO(Member 3): implement using the stream expression above.
        return false;
    }
}
