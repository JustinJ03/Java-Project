/**
 * GamificationModule.java
 * Gamification Module for the EduSDG application (SDG 4 – Quality Education).
 *
 * Tracks the player's points and badges, displays a leaderboard read from
 * shared storage, and shows an optional countdown timer during a quiz session.
 *
 * Creator  : Member 3
 */
package gamification;

import app.AppContext;
import app.AppModule;
import data.exceptions.DataNotFoundException;
import interfaces.IGamification;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamificationModule extends AppModule implements IGamification {

    // ── Constants ─────────────────────────────────────────────────────────────
    private static final Color COLOR_BG      = new Color(245, 248, 255);
    private static final Color COLOR_PRIMARY = new Color(63, 81, 181);
    private static final Color COLOR_ACCENT  = new Color(255, 193, 7);
    private static final Color COLOR_SUCCESS = new Color(76, 175, 80);
    private static final Font  FONT_TITLE    = new Font("SansSerif", Font.BOLD, 18);
    private static final Font  FONT_BODY     = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font  FONT_TIMER    = new Font("Monospaced", Font.BOLD, 28);

    // ── State ─────────────────────────────────────────────────────────────────
    private int              points = 0;
    private final List<Badge> badges = new ArrayList<>();

    // ── GUI references (held so onShow() can refresh them) ────────────────────
    private JLabel  pointsLabel;
    private JPanel  badgesPanel;
    private JList<String> leaderboardList;
    private DefaultListModel<String> listModel;
    private JLabel  timerLabel;
    private JProgressBar timerBar;
    private javax.swing.Timer swingTimer;
    private int remainingSeconds  = 0;
    private int totalTimerSeconds = 0;

    // ── Constructor ───────────────────────────────────────────────────────────
    public GamificationModule(AppContext ctx) {
        super(ctx, "Gamification");
    }

    // =========================================================
    // IGamification — addPoints(int p)
    // =========================================================
    /**
     * Adds points to the player's running total, then checks badge thresholds.
     * OOP: one overload of addPoints; the two-arg overload delegates here.
     */
    @Override
    public void addPoints(int p) {
        points += p;

        // Badge thresholds
        if (points >= 10  && !hasBadge("First Steps"))
            badges.add(new Badge("First Steps",  "Earned your first 10 points!", "🌱"));

        if (points >= 50  && !hasBadge("Rising Star"))
            badges.add(new Badge("Rising Star",  "Earned 50 points!", "⭐"));

        if (points >= 100 && !hasBadge("Scholar"))
            badges.add(new Badge("Scholar",      "Earned 100 points!", "📚"));

        if (points >= 200 && !hasBadge("Champion"))
            badges.add(new Badge("Champion",     "Earned 200 points!", "🏆"));

        refreshBadgesPanel();
        if (pointsLabel != null)
            pointsLabel.setText("Points: " + points);
    }

    // =========================================================
    // addPoints(int p, String reason)  — OVERLOAD
    // =========================================================
    /**
     * Overloaded version: adds points and prints the reason to the console.
     * OOP: method overloading — same name, different parameter list.
     */
    public void addPoints(int p, String reason) {
        addPoints(p);                                          // delegate to single-arg version
        System.out.println("+" + p + " pts: " + reason);
    }

    // =========================================================
    // IGamification — simple getters
    // =========================================================
    @Override public int          totalPoints()   { return points; }
    @Override public List<Badge>  awardedBadges() { return badges; }

    // =========================================================
    // IGamification — leaderboard()
    // =========================================================
    /**
     * Reads all saved scores via Member 4's IDataStorage, sorts them
     * highest-first, and returns formatted strings.
     * OOP: uses IDataStorage through polymorphism (ctx.storage()).
     * OOP: demonstrates exception handling with DataNotFoundException.
     */
    @Override
    public List<String> leaderboard() {
        List<String[]> rows;

        // STEP 1: Read scores — catch DataNotFoundException if file is missing/empty
        try {
            rows = ctx.storage().readScores();
        } catch (DataNotFoundException e) {
            System.out.println("Leaderboard: no scores found. (" + e.getMessage() + ")");
            return new ArrayList<>();
        }

        // STEP 2: Sort by score (index 1) descending
        rows.sort((a, b) -> {
            try {
                return Integer.parseInt(b[1]) - Integer.parseInt(a[1]);
            } catch (NumberFormatException ex) {
                return 0;
            }
        });

        // STEP 3: Format as "name - score"
        List<String> result = new ArrayList<>();
        int rank = 1;
        for (String[] row : rows) {
            String medal = rank == 1 ? "🥇" : rank == 2 ? "🥈" : rank == 3 ? "🥉" : rank + ".";
            result.add(medal + " " + row[0] + " — " + row[1] + "%");
            rank++;
        }
        return result;
    }

    // =========================================================
    // AppModule — getView()  OVERRIDE
    // =========================================================
    /**
     * Builds and returns the Gamification screen as a JComponent.
     * OOP: overrides AppModule.getView() — inheritance + overriding.
     */
    @Override
    public JComponent getView() {
        JPanel main = new JPanel(new BorderLayout(0, 10));
        main.setBackground(COLOR_BG);
        main.setBorder(new EmptyBorder(14, 14, 14, 14));

        // ── NORTH: title
        JLabel title = new JLabel("🎮  Gamification", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_PRIMARY);
        main.add(title, BorderLayout.NORTH);

        // ── CENTER: scrollable content column
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBackground(COLOR_BG);

        centre.add(buildTimerCard());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildPointsCard());
        centre.add(Box.createVerticalStrut(10));
        centre.add(buildBadgesCard());

        JScrollPane scroll = new JScrollPane(centre);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(COLOR_BG);
        main.add(scroll, BorderLayout.CENTER);

        // ── SOUTH: leaderboard
        main.add(buildLeaderboardCard(), BorderLayout.SOUTH);

        return main;
    }

    // =========================================================
    // AppModule — onShow()  OVERRIDE
    // =========================================================
    /**
     * Called whenever the user navigates to this module.
     * Refreshes the leaderboard list and badge display.
     * OOP: overrides AppModule.onShow().
     */
    @Override
    public void onShow() {
        refreshLeaderboardList();
        refreshBadgesPanel();
        if (pointsLabel != null)
            pointsLabel.setText("Points: " + points);
    }

    // =========================================================
    // Public helper — called by QuizModule after quiz ends
    // =========================================================
    /**
     * Evaluates quiz result and awards appropriate points + badges.
     * Call this from QuizModule: gamificationModule.onQuizComplete(username, score, total, timeLeft)
     *
     * @param score     number of correct answers
     * @param total     total number of questions
     * @param timeLeft  seconds remaining on the timer (0 if no timer used)
     */
    public void onQuizComplete(int score, int total, int timeLeft) {
        if (total == 0) return;
        double pct = (double) score / total * 100.0;

        // Award points based on percentage
        if (pct == 100.0) {
            addPoints(50, "Perfect score!");
            awardBadge(new Badge("Perfect Score", "Scored 100% on a quiz!", "💯"));
        } else if (pct >= 80.0) {
            addPoints(30, "High achiever (80%+)");
            awardBadge(new Badge("High Achiever", "Scored 80% or above!", "🏅"));
        } else if (pct >= 60.0) {
            addPoints(20, "Good result (60%+)");
        } else {
            addPoints(10, "Quiz completed");
        }

        // Speed bonus
        if (timeLeft > 0) {
            addPoints(10, "Speed bonus — finished with time to spare");
            awardBadge(new Badge("Speed Runner", "Finished a timed quiz early!", "⚡"));
        }

        // First quiz badge (always, if not already held)
        awardBadge(new Badge("First Step", "Completed your first quiz!", "🌱"));

        // Show popup with result summary
        String msg = String.format(
            "Quiz complete!\nScore: %d / %d  (%.0f%%)\n\nPoints earned this session: %d",
            score, total, pct, points);
        JOptionPane.showMessageDialog(null, msg, "Well done!", JOptionPane.INFORMATION_MESSAGE);

        onShow(); // refresh display
    }

    // =========================================================
    // Timer controls (also callable from QuizModule)
    // =========================================================

    /** Starts a countdown timer for the given number of seconds. */
    public void startTimer(int seconds) {
        stopTimer();
        totalTimerSeconds = seconds;
        remainingSeconds  = seconds;
        updateTimerDisplay();

        swingTimer = new javax.swing.Timer(1000, e -> {
            remainingSeconds--;
            updateTimerDisplay();
            if (remainingSeconds <= 0) {
                stopTimer();
                timerLabel.setText("⏰  Time's Up!");
                timerLabel.setForeground(Color.RED);
            }
        });
        swingTimer.start();
    }

    /** Stops the countdown timer. */
    public void stopTimer() {
        if (swingTimer != null && swingTimer.isRunning()) {
            swingTimer.stop();
        }
    }

    /** Returns seconds remaining on the current timer. */
    public int getRemainingTime() { return remainingSeconds; }

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Returns true if the player already holds a badge with this name. */
    private boolean hasBadge(String name) {
        return badges.stream().anyMatch(b -> b.getName().equals(name));
    }

    /** Awards a badge only if not already held; shows a popup notification. */
    private void awardBadge(Badge badge) {
        if (!hasBadge(badge.getName())) {
            badges.add(badge);
            refreshBadgesPanel();
            JOptionPane.showMessageDialog(
                null,
                badge.getIcon() + "  Badge Unlocked!\n\n"
                    + badge.getName() + "\n" + badge.getDescription(),
                "New Badge!",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void refreshBadgesPanel() {
        if (badgesPanel == null) return;
        badgesPanel.removeAll();
        if (badges.isEmpty()) {
            JLabel none = new JLabel("No badges yet — complete a quiz!");
            none.setFont(FONT_SMALL);
            none.setForeground(Color.GRAY);
            badgesPanel.add(none);
        } else {
            for (Badge b : badges) {
                JLabel chip = new JLabel(b.getIcon() + " " + b.getName());
                chip.setFont(FONT_SMALL);
                chip.setForeground(Color.WHITE);
                chip.setBackground(COLOR_PRIMARY);
                chip.setOpaque(true);
                chip.setBorder(new EmptyBorder(4, 8, 4, 8));
                chip.setToolTipText(b.getDescription());
                badgesPanel.add(chip);
            }
        }
        badgesPanel.revalidate();
        badgesPanel.repaint();
    }

    private void refreshLeaderboardList() {
        if (listModel == null) return;
        listModel.clear();
        List<String> entries = leaderboard();
        if (entries.isEmpty()) {
            listModel.addElement("No scores recorded yet.");
        } else {
            for (String entry : entries) listModel.addElement(entry);
        }
    }

    private void updateTimerDisplay() {
        if (timerLabel == null) return;
        int m = remainingSeconds / 60, s = remainingSeconds % 60;
        timerLabel.setText(String.format("%02d:%02d", m, s));

        if (totalTimerSeconds > 0) {
            int pct = (int) ((double) remainingSeconds / totalTimerSeconds * 100);
            timerBar.setValue(pct);
            if (remainingSeconds > totalTimerSeconds * 0.5) {
                timerLabel.setForeground(COLOR_SUCCESS);
                timerBar.setForeground(COLOR_SUCCESS);
            } else if (remainingSeconds > totalTimerSeconds * 0.25) {
                timerLabel.setForeground(new Color(255, 152, 0));
                timerBar.setForeground(new Color(255, 152, 0));
            } else {
                timerLabel.setForeground(new Color(211, 47, 47));
                timerBar.setForeground(new Color(211, 47, 47));
            }
        }
    }

    // ── Card builders ─────────────────────────────────────────────────────────

    private JPanel buildTimerCard() {
        JPanel card = createCard("⏱  Quiz Timer");

        timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(FONT_TIMER);
        timerLabel.setForeground(COLOR_PRIMARY);
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerBar = new JProgressBar(0, 100);
        timerBar.setValue(0);
        timerBar.setForeground(COLOR_SUCCESS);
        timerBar.setBackground(new Color(220, 220, 220));
        timerBar.setMaximumSize(new Dimension(320, 10));
        timerBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnStart = makeButton("▶  Start (60s)", COLOR_PRIMARY);
        JButton btnStop  = makeButton("■  Stop",        new Color(211, 47, 47));
        btnStart.addActionListener(e -> startTimer(60));
        btnStop .addActionListener(e -> stopTimer());

        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        row.setOpaque(false);
        row.add(btnStart);
        row.add(btnStop);

        card.add(timerLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(timerBar);
        card.add(Box.createVerticalStrut(8));
        card.add(row);
        return card;
    }

    private JPanel buildPointsCard() {
        JPanel card = createCard("⭐  Your Points");
        pointsLabel = new JLabel("Points: " + points, SwingConstants.CENTER);
        pointsLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        pointsLabel.setForeground(COLOR_ACCENT);
        pointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(pointsLabel);
        return card;
    }

    private JPanel buildBadgesCard() {
        JPanel card = createCard("🏅  Badges Earned");
        badgesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        badgesPanel.setOpaque(false);
        card.add(badgesPanel);
        refreshBadgesPanel();
        return card;
    }

    private JPanel buildLeaderboardCard() {
        JPanel card = createCard("🏆  Leaderboard");
        listModel = new DefaultListModel<>();
        leaderboardList = new JList<>(listModel);
        leaderboardList.setFont(FONT_BODY);
        leaderboardList.setBackground(Color.WHITE);

        JScrollPane sp = new JScrollPane(leaderboardList);
        sp.setPreferredSize(new Dimension(320, 120));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton refresh = makeButton("🔄  Refresh", COLOR_PRIMARY);
        refresh.addActionListener(e -> refreshLeaderboardList());
        refresh.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(sp);
        card.add(Box.createVerticalStrut(6));
        card.add(refresh);
        refreshLeaderboardList();
        return card;
    }

    /** Creates a titled card panel (BoxLayout Y_AXIS). */
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 220), 1, true),
            new EmptyBorder(10, 14, 10, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(COLOR_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}

