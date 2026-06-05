package main;

import data.ProfilePanel;
import gamification.GamificationModule;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import learning.LearningModule;
import learning.LearningModulePanel;
import quiz.QuizModule;

/**
 * Entry point and root JFrame.
 * Owns all four module instances and manages tab navigation via CardLayout.
 * Wires modules together using setter injection at startup.
 *
 * Creator : All members (shared file)
 * Tester  : All members
 *
 * To compile:  build.bat
 * To run:      run.bat
 */
public class Main extends JFrame {

    static final int   WIDTH = 480;
    static final int   HEIGHT = 820;
    static final Color GREEN = new Color(26, 122, 74);

    /** Vivid accent colour for each bottom tab (Learn, Quiz, Leaderboard, Profile). */
    static final Color[] TAB_COLORS = {
        new Color(22, 163, 90),
        new Color(59, 130, 246),
        new Color(245, 158, 11),
        new Color(234, 88, 12),
    };

    CardLayout cardLayout;
    JPanel     cardPanel;
    JButton[]  tabBtns = new JButton[4];

    LearningModule     learningModule;      // Member 1
    QuizModule         quizModule;          // Member 2
    GamificationModule gamifModule;         // Member 3
    ProfilePanel       profilePanel;        // Member 4

    public Main() {
        super("SDG 4 - Quality Education");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initModels();
        buildUI();
        showTab(0);
    }

    /**
     * Constructs all four modules and injects dependencies between them.
     * QuizModule receives a GamificationModule reference (so it can award badges on quiz end)
     * and a Runnable callback (so 'View Leaderboard' can navigate without knowing about Main).
     */
    void initModels() {
        learningModule = new LearningModule("Quality Education", "SDG 4");      // Member 1
        quizModule     = new QuizModule();                                        // Member 2
        gamifModule    = new GamificationModule();                               // Member 3
        profilePanel   = new ProfilePanel();                                     // Member 4

        // Wire quiz → gamification: points/badges update the moment a quiz ends
        quizModule.setGamificationModule(gamifModule);
        // Wire quiz → navigation: "View Leaderboard" button jumps to tab 2 (BADGES/Leaderboard)
        quizModule.setGoToLeaderboardCallback(() -> showTab(2));
    }

    /** Adds the four module panels to the CardLayout and attaches the bottom tab bar. */
    void buildUI() {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);

        // ── Member 1: Learning panel (LIVE) ─────────────────────────────────
        cardPanel.add(new LearningModulePanel(learningModule), "LEARN");

        cardPanel.add(quizModule,           "QUIZ");    // Member 2 (LIVE)
        cardPanel.add(gamifModule.getView(), "BADGES");  // Member 3 (LIVE)
        cardPanel.add(profilePanel,          "PROFILE"); // Member 4 (LIVE)

        add(cardPanel,    BorderLayout.CENTER);
        add(makeTabBar(), BorderLayout.SOUTH);
    }

    // ── Bottom tab bar ────────────────────────────────────────────────────────
    JPanel makeTabBar() {
        JPanel bar = new JPanel(new GridLayout(1, 4, 6, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(210, 210, 210)),
            new EmptyBorder(6, 10, 10, 10)));
        bar.setPreferredSize(new Dimension(0, 72));

        String[] labels = { "Learn", "Quiz", "Leaderboard", "Profile" };

        for (int i = 0; i < 4; i++) {
            final int idx = i;
            TabBarIcon tabIcon = new TabBarIcon(i, TAB_COLORS[i], false);
            Dimension iconBox = new Dimension(TabBarIcon.SIZE, TabBarIcon.SIZE);
            JLabel lIcon = new JLabel(tabIcon);
            lIcon.setHorizontalAlignment(SwingConstants.CENTER);
            lIcon.setVerticalAlignment(SwingConstants.CENTER);
            lIcon.setPreferredSize(iconBox);
            lIcon.setMinimumSize(iconBox);
            lIcon.setMaximumSize(iconBox);

            JLabel lText = new JLabel(labels[i]);
            lText.setFont(new Font("SansSerif", Font.PLAIN, 10));
            lText.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel stack = new JPanel();
            stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
            stack.setOpaque(false);
            lIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            lText.setAlignmentX(Component.CENTER_ALIGNMENT);
            stack.add(lIcon);
            stack.add(Box.createVerticalStrut(3));
            stack.add(lText);

            JPanel cell = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            cell.setOpaque(false);
            cell.add(stack);

            JButton btn = new JButton();
            btn.setLayout(new BorderLayout());
            btn.add(cell, BorderLayout.CENTER);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setOpaque(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> showTab(idx));

            tabBtns[i] = btn;
            btn.putClientProperty("icon",     lIcon);
            btn.putClientProperty("label",    lText);
            btn.putClientProperty("tabIcon",  tabIcon);

            bar.add(btn);
        }
        return bar;
    }

    /**
     * Switches the visible card and notifies the newly active module via onShow().
     * Modules use onShow() to refresh data (e.g. leaderboard re-reads scores.txt).
     */
    void showTab(int idx) {
        String[] cards = { "LEARN", "QUIZ", "BADGES", "PROFILE" };
        cardLayout.show(cardPanel, cards[idx]);
        // Notify active modules so they can refresh their display
        if (idx == 1 && quizModule    != null) quizModule.onShow();
        if (idx == 2 && gamifModule   != null) gamifModule.onShow();
        if (idx == 3 && profilePanel  != null) profilePanel.onShow();
        // update the active tab's icon and label to highlight it.
        for (int i = 0; i < tabBtns.length; i++) {
            boolean active = (i == idx);
            Color c = TAB_COLORS[i];
            TabBarIcon tabIcon = (TabBarIcon) tabBtns[i].getClientProperty("tabIcon");
            JLabel icon  = (JLabel) tabBtns[i].getClientProperty("icon");
            JLabel label = (JLabel) tabBtns[i].getClientProperty("label");
            if (tabIcon != null) { tabIcon.setColor(c); tabIcon.setActive(active); }
            if (icon  != null) icon.repaint();
            if (label != null) {
                label.setForeground(active ? c.darker() : new Color(110, 110, 110));
                label.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 10));
            }
        }
    }

    // Custom Icon implementation that paints a rounded gradient circle with a glyph for each tab.
    /** Rounded gradient badge icons for the bottom tab bar. */
    static final class TabBarIcon implements Icon {
        static final int SIZE  = 30;
        private static final int GLYPH = 22;
        private final int type;
        private Color color;
        private boolean active;

        TabBarIcon(int type, Color color, boolean active) {
            this.type   = type;
            this.color  = color;
            this.active = active;
        }

        void setColor(Color color)  { this.color = color; }
        void setActive(boolean on)  { this.active = on; }

        @Override public int getIconWidth()  { return SIZE; }
        @Override public int getIconHeight() { return SIZE; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.translate(x, y);
            int pad = 1; int d = SIZE - pad * 2;
            Color top = color.brighter(); Color bot = color.darker();
            g2.setPaint(new GradientPaint(0, pad, top, 0, pad + d, bot));
            g2.fillOval(pad, pad, d, d);
            if (active) {
                g2.setColor(new Color(255, 255, 255, 100));
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(pad, pad, d, d);
            }
            g2.setColor(new Color(255, 255, 255, 245));
            int gx = (SIZE - GLYPH) / 2; int gy = (SIZE - GLYPH) / 2;
            g2.translate(gx, gy);
            switch (type) {
                case 0: paintBook(g2);    break;
                case 1: paintQuiz(g2);    break;
                case 2: paintStar(g2);    break;
                default: paintProfile(g2); break;
            }
            g2.dispose();
        }

        void paintBook(Graphics2D g) {
            g.fillRoundRect(2, 4, 8, 14, 2, 2);
            g.fillRoundRect(12, 4, 8, 14, 2, 2);
            g.setColor(new Color(255, 255, 255, 120));
            g.fillRect(10, 5, 2, 12);
        }
        void paintQuiz(Graphics2D g) {
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            FontMetrics fm = g.getFontMetrics();
            String mark = "?";
            int tx = (GLYPH - fm.stringWidth(mark)) / 2;
            int ty = (GLYPH + fm.getAscent() - fm.getDescent()) / 2;
            g.drawString(mark, tx, ty);
        }
        void paintStar(Graphics2D g) {
            int[] xs = { 11, 13, 18, 14, 16, 11, 6, 8, 4, 9 };
            int[] ys = { 3, 8, 8, 11, 16, 13, 16, 11, 8, 8 };
            g.fillPolygon(xs, ys, 10);
        }
        void paintProfile(Graphics2D g) {
            g.fillOval(6, 2, 10, 10);
            g.fillRoundRect(3, 12, 16, 8, 6, 6);
        }
    }

    // ── Placeholder panel for unfinished modules ──────────────────────────────
    /**
     * Utility: builds a centred card panel used when a module has not yet been implemented.
     * Not used in the final build.
     */
    static JPanel makePlaceholder(String title, String msg, int tabIndex) {
        Color accent = TAB_COLORS[tabIndex];
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Color.WHITE);
        int cardW = WIDTH - 64; int textW = cardW - 56;
        Color accentSoft = new Color(
            Math.min(255, accent.getRed()   + 200),
            Math.min(255, accent.getGreen() + 200),
            Math.min(255, accent.getBlue()  + 200));
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(accentSoft);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accent, 2, true),
            new EmptyBorder(32, 28, 32, 28)));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setMaximumSize(new Dimension(cardW, 560));
        JLabel lIcon = new JLabel(new TabBarIcon(tabIndex, accent, true), SwingConstants.CENTER);
        lIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        lIcon.setMaximumSize(new Dimension(cardW - 56, TabBarIcon.SIZE + 4));
        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lTitle.setForeground(accent.darker());
        lTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lTitle.setMaximumSize(new Dimension(textW, 80));
        JTextArea lMsg = new JTextArea(msg);
        lMsg.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lMsg.setForeground(new Color(42, 42, 42));
        lMsg.setBackground(accentSoft);
        lMsg.setLineWrap(true); lMsg.setWrapStyleWord(true);
        lMsg.setEditable(false); lMsg.setFocusable(false);
        lMsg.setOpaque(false); lMsg.setBorder(null);
        lMsg.setAlignmentX(Component.CENTER_ALIGNMENT);
        Dimension msgDim = placeholderTextSize(lMsg, textW);
        lMsg.setPreferredSize(msgDim); lMsg.setMaximumSize(msgDim); lMsg.setMinimumSize(msgDim);
        JLabel lNote = new JLabel("  Coming soon  ", SwingConstants.CENTER);
        lNote.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lNote.setForeground(Color.WHITE); lNote.setOpaque(true);
        lNote.setBackground(accent);
        lNote.setBorder(new EmptyBorder(8, 16, 8, 16));
        lNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        lNote.setMaximumSize(new Dimension(textW, 40));
        card.add(lIcon); card.add(Box.createVerticalStrut(14));
        card.add(lTitle); card.add(Box.createVerticalStrut(16));
        card.add(lMsg); card.add(Box.createVerticalStrut(20));
        card.add(lNote);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        outer.add(card, gbc);
        return outer;
    }

    static Dimension placeholderTextSize(JTextArea area, int width) {
        area.setSize(new Dimension(width, Short.MAX_VALUE));
        Dimension pref = area.getPreferredSize();
        return new Dimension(width, pref.height + 4);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
