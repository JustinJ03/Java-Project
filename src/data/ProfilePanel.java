package data;

import data.exceptions.DataNotFoundException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * ProfilePanel.java
 * Displays the player score history read from data/scores.txt.
 *
 * Shows:
 *   - Summary stats: total entries, best score, average score
 *   - Score table: Rank / Name / Score / Date  (sorted highest-first)
 *   - File location so the user can always find the text file
 *   - Refresh button to reload from disk
 *
 * OOP concepts demonstrated:
 *   - Uses IDataStorage through DataManager (polymorphism)
 *   - Encapsulation: all UI logic self-contained in this class
 *   - Exception handling: DataNotFoundException caught and displayed in-panel
 *
 * Creator : Member 4
 * Tester  : Member 4
 */
public class ProfilePanel extends JPanel {

    // ── Colours (match GamificationModule palette) ────────────────────────────
    private static final Color COLOR_BG      = new Color(245, 248, 255);
    private static final Color COLOR_PRIMARY = new Color(63, 81, 181);
    private static final Color COLOR_ACCENT  = new Color(255, 193, 7);
    private static final Color COLOR_HEADER  = new Color(63, 81, 181);

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_BODY   = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_STAT   = new Font("SansSerif", Font.BOLD, 22);

    // ── Data source ───────────────────────────────────────────────────────────
    private static final String SCORES_FILE = "data/scores.txt";
    private final DataManager dataManager;

    // ── Live UI references (refreshed in onShow) ──────────────────────────────
    private DefaultTableModel tableModel;
    private JLabel lblTotal, lblBest, lblAvg;
    private JLabel lblStatus;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Builds the profile panel.
     * Call {@link #onShow()} whenever this panel becomes visible to refresh data.
     */
    public ProfilePanel() {
        this.dataManager = new DataManager(SCORES_FILE);
        setLayout(new BorderLayout(0, 10));
        setBackground(COLOR_BG);
        setBorder(new EmptyBorder(14, 14, 14, 14));

        add(buildHeader(),     BorderLayout.NORTH);
        add(buildContent(),    BorderLayout.CENTER);
        add(buildFooter(),     BorderLayout.SOUTH);

        // Load on construction
        refreshData();
    }

    // ── Called by Main.showTab() when user taps the Profile tab ───────────────

    /** Refreshes the score table from disk. Call when the tab becomes visible. */
    public void onShow() {
        refreshData();
    }

    // ── Panel builders ────────────────────────────────────────────────────────

    private JLabel buildHeader() {
        JLabel title = new JLabel("📋  Score History", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_PRIMARY);
        return title;
    }

    private JPanel buildContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(COLOR_BG);

        content.add(buildStatsCard());
        content.add(Box.createVerticalStrut(10));
        content.add(buildTableCard());
        return content;
    }

    /** Three-cell summary: Total / Best / Avg */
    private JPanel buildStatsCard() {
        JPanel card = createCard("📊  Summary");

        JPanel row = new JPanel(new GridLayout(1, 3, 10, 0));
        row.setOpaque(false);

        lblTotal = makeStat("0", "Quizzes");
        lblBest  = makeStat("0%", "Best Score");
        lblAvg   = makeStat("0%", "Average");

        row.add(wrapStat(lblTotal, "Quizzes"));
        row.add(wrapStat(lblBest,  "Best Score"));
        row.add(wrapStat(lblAvg,   "Average"));

        card.add(row);
        return card;
    }

    /** Score history table: Rank / Name / Score / Date */
    private JPanel buildTableCard() {
        JPanel card = createCard("🏆  Leaderboard (from scores.txt)");

        // Table model — non-editable
        String[] columns = { "#", "Name", "Score", "Date" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(FONT_BODY);
        table.setRowHeight(26);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(COLOR_HEADER);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 210, 245));
        table.setGridColor(new Color(220, 220, 235));
        table.setShowGrid(true);

        // Centre-align rank and score columns
        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centre);   // #
        table.getColumnModel().getColumn(2).setCellRenderer(centre);   // Score

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setBorder(new LineBorder(new Color(200, 200, 220), 1, true));

        card.add(sp);
        return card;
    }

    private JPanel buildFooter() {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);

        // File path hint
        lblStatus = new JLabel("File: " + new java.io.File(SCORES_FILE).getAbsolutePath());
        lblStatus.setFont(FONT_SMALL);
        lblStatus.setForeground(Color.GRAY);

        // Refresh button
        JButton btnRefresh = makeButton("🔄  Refresh", COLOR_PRIMARY);
        btnRefresh.addActionListener(e -> refreshData());

        row.add(lblStatus,   BorderLayout.CENTER);
        row.add(btnRefresh,  BorderLayout.EAST);
        return row;
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    /** Reads scores.txt and populates the stats labels and table. */
    private void refreshData() {
        tableModel.setRowCount(0); // clear existing rows

        List<String[]> rows;
        try {
            rows = dataManager.readScores();
        } catch (DataNotFoundException e) {
            lblStatus.setText("Cannot read file: " + e.getMessage());
            lblTotal.setText("0"); lblBest.setText("0%"); lblAvg.setText("0%");
            return;
        }

        if (rows.isEmpty()) {
            lblStatus.setText("No scores yet — complete a quiz to record your first score.");
            lblTotal.setText("0"); lblBest.setText("0%"); lblAvg.setText("0%");
            return;
        }

        // Sort by score descending (leaderboard style)
        rows.sort(Comparator.comparingInt(
                r -> -parseSafe(r.length > 1 ? r[1] : "0")));

        // Calculate stats
        int total = rows.size();
        int best  = rows.stream().mapToInt(r -> parseSafe(r.length > 1 ? r[1] : "0")).max().orElse(0);
        int avg   = (int) rows.stream().mapToInt(r -> parseSafe(r.length > 1 ? r[1] : "0")).average().orElse(0);

        lblTotal.setText(String.valueOf(total));
        lblBest.setText(best + "%");
        lblAvg.setText(avg + "%");

        // Populate table
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        int rank = 1;
        for (String[] row : rows) {
            String name  = row.length > 0 ? row[0] : "?";
            String score = (row.length > 1 ? row[1] : "?") + "%";
            String date  = "—";
            if (row.length > 2) {
                try { date = sdf.format(new Date(Long.parseLong(row[2].trim()))); }
                catch (NumberFormatException ignored) { }
            }
            tableModel.addRow(new Object[]{ rank++, name, score, date });
        }

        lblStatus.setText("File: " + new java.io.File(SCORES_FILE).getAbsolutePath()
                + "  (" + total + " " + (total == 1 ? "entry" : "entries") + ")");
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private int parseSafe(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    /** Creates a titled card panel (BoxLayout Y). */
    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 220), 1, true),
            new EmptyBorder(10, 14, 10, 14)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(COLOR_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(8));
        return card;
    }

    /** Stat value label (the big number). */
    private JLabel makeStat(String value, String label) {
        JLabel l = new JLabel(value, SwingConstants.CENTER);
        l.setFont(FONT_STAT);
        l.setForeground(COLOR_ACCENT);
        return l;
    }

    /** Wraps a stat label in a mini card with a caption below. */
    private JPanel wrapStat(JLabel valueLbl, String caption) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(240, 243, 255));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        valueLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel cap = new JLabel(caption, SwingConstants.CENTER);
        cap.setFont(FONT_SMALL);
        cap.setForeground(Color.GRAY);
        cap.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(valueLbl);
        p.add(cap);
        return p;
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
