package learning;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * LearningModulePanel.java
 * Swing GUI panel for the Learning Module.
 *
 * Creator : Adriana Umang Anak Nawang (103088)
 * Tester  :
 */
public class LearningModulePanel extends JPanel {

    static final Color GREEN       = new Color(26, 122, 74);    // primary green for headers and buttons
    static final Color GREEN_PALE  = new Color(225, 245, 238);  // pale green for backgrounds and highlights
    static final Color TEXT_MAIN   = new Color(38, 38, 38);     // near-black for main body text
    static final Color TEXT_FACT   = new Color(52, 52, 52);     // slightly lighter dark gray for key-fact text
    static final Color BORDER_MAIN = new Color(170, 195, 175);  // muted green for primary borders
    static final Color BORDER_SOFT = new Color(205, 220, 205);  // lighter green for secondary borders

    static final int IMAGE_HEIGHT  = 220;   // fixed image height; width scales with viewport
    static final int BODY_FONT_SZ  = 16;    // body text font size
    static final int BODY_BOX_H    = 170;   // fixed body-text area height for consistent layout
    static final int TITLE_FONT_SZ = 18;    // page title font size
    static final int FACT_FONT_SZ  = 14;    // key-fact font size, slightly smaller than body

    /** Soft sans-serif — easy to read on screen (Segoe UI on Windows). */
    static final String FONT_FAMILY = resolveFontFamily();

    LearningModule module;  // the module whose content this panel displays

    static String resolveFontFamily() {
        String[] preferred = { "Segoe UI", "Calibri", "Arial" };  // preferred fonts in priority order
        for (String name : preferred) {
            Font test = new Font(name, Font.PLAIN, 12);
            if (test.getFamily().equalsIgnoreCase(name)) {
                return name;  // font is available on this system
            }
        }
        return Font.SANS_SERIF;  // fallback if no preferred font is found
    }

    static Font lessonFont(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    static String htmlFontCss(int sizePx, boolean bold, boolean italic, String color) {
        StringBuilder css = new StringBuilder();
        css.append("font-family:'").append(FONT_FAMILY).append("',Calibri,Arial,sans-serif;");
        css.append("font-size:").append(sizePx).append("px;");
        css.append("line-height:1.45;");
        if (bold)   css.append("font-weight:bold;");
        if (italic) css.append("font-style:italic;");
        if (color != null) css.append("color:").append(color).append(";");
        return css.toString();
    }

    // Header widgets
    JLabel       lblSubtitle;   // "SDG 4 · Page X/N · Y%" shown below the module title
    JProgressBar progBar;       // progress bar reflecting how far through the module the user is

    // Content widgets
    JLabel    lblImage;     // image for the current page (fixed height, responsive width)
    JTextArea txtTitle;     // page title — bold, wraps if long
    JTextArea txtBody;      // main body text — fixed-height box for layout consistency
    JPanel    pnlFact;      // container for the key-fact highlight box
    JTextArea txtFact;      // key-fact text inside pnlFact

    // Footer widgets
    JPanel  pnlDots;            // page-indicator dots (current page shown larger/filled)
    JButton btnPrev, btnNext;   // prev/next navigation buttons

    JScrollPane contentScroll;  // scroll pane wrapping the main content area
    JPanel      contentInner;   // inner panel holding image, title, body, and key fact

    public LearningModulePanel(LearningModule module) {
        this.module = module;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(makeHeader(),  BorderLayout.NORTH);
        add(makeCenter(),  BorderLayout.CENTER);
        add(makeFooter(),  BorderLayout.SOUTH);

        refresh();  // initialize display to first page
    }

    // ── HEADER ────────────────────────────────────────────────────────────────
    JPanel makeHeader() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(GREEN);
        p.setBorder(new EmptyBorder(14, 16, 12, 16));

        JLabel title = new JLabel(module.getModuleTitle());
        title.setFont(lessonFont(Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        lblSubtitle = new JLabel("SDG 4");
        lblSubtitle.setFont(lessonFont(Font.PLAIN, 11));
        lblSubtitle.setForeground(new Color(200, 235, 215));

        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));
        row.setOpaque(false);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(title);
        row.add(Box.createVerticalStrut(4));
        row.add(lblSubtitle);

        progBar = new JProgressBar(0, 100);
        progBar.setValue(0);
        progBar.setForeground(Color.WHITE);
        progBar.setBackground(new Color(100, 170, 130));
        progBar.setBorderPainted(false);
        progBar.setPreferredSize(new Dimension(0, 5));  // slim progress bar

        p.add(row,     BorderLayout.NORTH);
        p.add(progBar, BorderLayout.SOUTH);
        return p;
    }

    // ── CENTER (scrollable content) ───────────────────────────────────────────
    JPanel makeCenter() {
        contentInner = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int w = contentWidth();
                if (w > 0) d.width = w;  // stretch to fill viewport width
                return d;
            }

            @Override
            public Dimension getMaximumSize() {
                Dimension d = getPreferredSize();
                d.height = Integer.MAX_VALUE;  // allow unlimited vertical growth
                return d;
            }
        };
        JPanel inner = contentInner;
        inner.setLayout(new GridBagLayout());
        inner.setBackground(new Color(252, 254, 252));
        inner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_MAIN, 2, true),
            new EmptyBorder(16, 18, 14, 18)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.fill  = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;   // stretch to full width
        c.insets = new Insets(0, 0, 10, 0);

        // ── Image ──────────────────────────────────────────────────────────
        lblImage = new JLabel("[No image]", SwingConstants.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(238, 243, 238));
        lblImage.setForeground(new Color(150, 150, 150));
        lblImage.setFont(lessonFont(Font.PLAIN, 11));
        lblImage.setBorder(BorderFactory.createLineBorder(BORDER_SOFT, 2, true));
        lblImage.setPreferredSize(new Dimension(0, IMAGE_HEIGHT));
        lblImage.setMinimumSize(new Dimension(0, IMAGE_HEIGHT));
        c.gridy = 0;
        inner.add(lblImage, c);

        // ── Page title (wraps like lesson body) ───────────────────────────
        txtTitle = new JTextArea("Title");
        txtTitle.setFont(lessonFont(Font.BOLD, TITLE_FONT_SZ));
        txtTitle.setForeground(TEXT_MAIN);
        txtTitle.setBackground(new Color(252, 254, 252));
        txtTitle.setLineWrap(true);
        txtTitle.setWrapStyleWord(true);
        txtTitle.setEditable(false);
        txtTitle.setFocusable(false);
        txtTitle.setOpaque(false);
        txtTitle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, GREEN),
            new EmptyBorder(0, 0, 8, 0)));  // green underline separates title from body
        c.gridy = 1;
        c.insets = new Insets(0, 0, 10, 0);
        inner.add(txtTitle, c);

        // ── Body text ──────────────────────────────────────────────────────
        txtBody = new JTextArea();
        txtBody.setFont(lessonFont(Font.PLAIN, BODY_FONT_SZ));
        txtBody.setForeground(TEXT_MAIN);
        txtBody.setBackground(Color.WHITE);
        txtBody.setLineWrap(true);
        txtBody.setWrapStyleWord(true);
        txtBody.setEditable(false);
        txtBody.setOpaque(true);
        txtBody.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_SOFT, 1, true),
            new EmptyBorder(12, 14, 12, 14)));
        txtBody.setMargin(new Insets(0, 0, 0, 0));
        c.gridy = 2;
        c.weighty = 0;  // do not stretch vertically; height determined by content
        c.insets = new Insets(0, 0, 12, 0);
        inner.add(txtBody, c);

        // ── Key fact box ───────────────────────────────────────────────────
        pnlFact = new JPanel(new BorderLayout(0, 6));
        pnlFact.setBackground(GREEN_PALE);
        pnlFact.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 1, true),
            BorderFactory.createCompoundBorder(
                new MatteBorder(0, 4, 0, 0, GREEN),  // green left accent bar
                new EmptyBorder(10, 12, 10, 12))));

        JLabel lblFactHead = new JLabel("Key Fact");
        lblFactHead.setFont(lessonFont(Font.BOLD, 12));
        lblFactHead.setForeground(new Color(10, 90, 50));

        txtFact = new JTextArea();
        txtFact.setFont(lessonFont(Font.PLAIN, FACT_FONT_SZ));
        txtFact.setForeground(TEXT_FACT);
        txtFact.setBackground(GREEN_PALE);
        txtFact.setLineWrap(true);
        txtFact.setWrapStyleWord(true);
        txtFact.setEditable(false);
        txtFact.setOpaque(false);
        txtFact.setBorder(null);

        pnlFact.add(lblFactHead, BorderLayout.NORTH);
        pnlFact.add(txtFact,     BorderLayout.CENTER);

        c.gridy = 3;
        c.insets = new Insets(0, 0, 6, 0);
        inner.add(pnlFact, c);

        // ── Filler to push content to top ──────────────────────────────────
        GridBagConstraints filler = new GridBagConstraints();
        filler.gridx = 0; filler.gridy = 4;
        filler.weighty = 1.0;       // absorbs leftover vertical space
        filler.fill = GridBagConstraints.VERTICAL;
        inner.add(Box.createVerticalGlue(), filler);

        contentScroll = new JScrollPane(inner,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        contentScroll.setBorder(BorderFactory.createLineBorder(BORDER_MAIN, 1, true));
        contentScroll.getViewport().setBackground(new Color(252, 254, 252));

        contentScroll.getViewport().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                syncContentWidths();  // re-wrap text when viewport width changes
            }
        });

        JPanel lessonWrap = new JPanel(new BorderLayout());
        lessonWrap.setBackground(Color.WHITE);
        lessonWrap.setBorder(new EmptyBorder(10, 14, 6, 14));
        lessonWrap.add(contentScroll, BorderLayout.CENTER);
        return lessonWrap;
    }

    /** Usable text width inside the scroll area (padding already accounted for). */
    int textWrapWidth() {
        int w = contentWidth();
        return Math.max(200, w - 52);  // subtract border + padding; minimum 200px
    }

    int contentWidth() {
        if (contentScroll != null) {
            int w = contentScroll.getViewport().getWidth();
            if (w > 0) return w;
        }
        int w = getWidth();
        return w > 0 ? w : 448;  // fall back to panel width, then a sensible default
    }

    static String htmlWrap(String text, int widthPx, String fontCss) {
        return "<html><body style='width:" + widthPx + "px;" + fontCss + "'>"
                + htmlEscape(text) + "</body></html>";
    }

    static String htmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
              .replace("<", "&lt;")
              .replace(">", "&gt;");
    }

    /** Width for text inside the key-fact box (accounts for its border and padding). */
    int factWrapWidth() {
        return Math.max(180, textWrapWidth() - 30);
    }

    void syncTextAreaWidth(JTextArea area, int wrap) {
        syncTextAreaWidth(area, wrap, 0);
    }

    void syncTextAreaWidth(JTextArea area, int wrap, int extraHeight) {
        area.setSize(new Dimension(wrap, Short.MAX_VALUE));  // force layout at target width
        Dimension pref = area.getPreferredSize();
        int h = Math.max(pref.height + extraHeight, pref.height);
        area.setPreferredSize(new Dimension(wrap, h));
        area.setMaximumSize(new Dimension(wrap, Integer.MAX_VALUE));
    }

    void syncBodyAreaFixedHeight(int wrap) {
        // Body box has a constant height regardless of content length.
        txtBody.setSize(new Dimension(wrap, BODY_BOX_H));
        txtBody.setPreferredSize(new Dimension(wrap, BODY_BOX_H));
        txtBody.setMinimumSize(new Dimension(wrap, BODY_BOX_H));
        txtBody.setMaximumSize(new Dimension(wrap, BODY_BOX_H));
    }

    /** Keep title, body, and key fact within the visible panel width. */
    void syncContentWidths() {
        int wrap = textWrapWidth();
        if (wrap <= 0) return;

        syncTextAreaWidth(txtTitle, wrap);
        // Keep lesson body box height consistent across all pages.
        syncBodyAreaFixedHeight(wrap);
        if (txtFact != null && pnlFact.isVisible()) {
            syncTextAreaWidth(txtFact, factWrapWidth());
        }

        if (contentInner != null) {
            contentInner.revalidate();
        }
    }

    // ── FOOTER (page dots + prev/next buttons) ────────────────────────────────
    JPanel makeFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(215, 220, 215)));  // top divider

        pnlDots = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 8));
        pnlDots.setBackground(Color.WHITE);
        footer.add(pnlDots, BorderLayout.NORTH);

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.WHITE);
        btnRow.setBorder(new EmptyBorder(2, 16, 14, 16));

        btnPrev = makeBtn("← Prev", false);
        btnNext = makeBtn("Next →", true);
        btnPrev.addActionListener(e -> { module.prevPage(); refresh(); });
        btnNext.addActionListener(e -> {
            if (module.getCurrentPage() >= module.getTotalPages()) {
                onFinishClick();
            } else {
                module.nextPage();
                refresh();
            }
        });

        btnRow.add(btnPrev);
        btnRow.add(btnNext);
        footer.add(btnRow, BorderLayout.SOUTH);
        return footer;
    }

    JButton makeBtn(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(lessonFont(Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        if (primary) {
            b.setBackground(GREEN);
            b.setForeground(Color.WHITE);
            b.setBorder(new EmptyBorder(9, 20, 9, 20));
        } else {
            b.setBackground(Color.WHITE);
            b.setForeground(new Color(50, 50, 50));
            b.setBorder(BorderFactory.createLineBorder(new Color(180, 195, 180)));
        }
        return b;
    }

    // ── REFRESH: update all widgets to match current page ─────────────────────
    public void refresh() {
        ContentPage page = module.getCurrentContentPage();
        if (page == null) return;
        page.markViewed();

        // Header
        int pct = (int) module.getProgress();
        lblSubtitle.setText(htmlWrap(
                "SDG 4 · Page " + module.getCurrentPage() + "/"
                        + module.getTotalPages() + " · " + pct + "%",
                Math.max(200, getWidth() > 0 ? getWidth() - 48 : 400),
                htmlFontCss(11, false, false, "#c8ebd7")));
        progBar.setValue(pct);

        // Image
        lblImage.setIcon(null);  // clear before loading new image
        if (page.getImagePath() != null) {
            java.net.URL url = getClass().getResource("/" + page.getImagePath());
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                int imgW = Math.max(1, textWrapWidth());
                Image scaled = icon.getImage()
                        .getScaledInstance(imgW, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaled));
                lblImage.setText(null);
                lblImage.setPreferredSize(new Dimension(imgW, IMAGE_HEIGHT));
            } else {
                lblImage.setText("<html><center>"
                        + htmlEscape("[Image: " + page.getImagePath() + "]")
                        + "</center></html>");  // image file missing at runtime
            }
        } else {
            lblImage.setIcon(null);
            lblImage.setText("[No image for this page]");
            lblImage.setPreferredSize(new Dimension(0, IMAGE_HEIGHT));
        }

        int wrap = textWrapWidth();

        // Title and body
        txtTitle.setText(page.getTitle());
        txtTitle.setCaretPosition(0);
        txtBody.setText(page.getBodyText());
        txtBody.setCaretPosition(0);

        // Key fact
        String fact = page.getKeyFact();
        if (fact != null && !fact.isEmpty()) {
            txtFact.setText(fact);
            txtFact.setCaretPosition(0);
            pnlFact.setVisible(true);
        } else {
            pnlFact.setVisible(false);  // hide box when page has no key fact
        }
        syncContentWidths();

        // Navigation buttons
        updateNavButtons();

        // Page indicator dots
        rebuildDots();

        SwingUtilities.invokeLater(() -> {
            syncContentWidths();
            if (contentScroll != null) {
                contentScroll.getViewport().setViewPosition(new Point(0, 0));  // scroll to top on page change
            }
            revalidate();
            repaint();
        });
    }

    void rebuildDots() {
        pnlDots.removeAll();
        int total   = module.getTotalPages();
        int current = module.getCurrentPage();
        for (int i = 1; i <= total; i++) {
            JPanel dot = new JPanel();
            dot.setBackground(i == current ? GREEN : new Color(200, 210, 200));  // highlight active page
            dot.setPreferredSize(new Dimension(i == current ? 18 : 7, 7));       // active dot is wider
            pnlDots.add(dot);
        }
        pnlDots.revalidate();
        pnlDots.repaint();
    }

    void updateNavButtons() {
        boolean onLast = module.getCurrentPage() >= module.getTotalPages();
        btnPrev.setEnabled(module.getCurrentPage() > 1);  // disable on first page

        if (onLast) {
            if (module.isCompleted()) {
                btnNext.setText("Finished ✓");
                btnNext.setEnabled(true);
                btnNext.setBackground(new Color(90, 150, 110));  // darker green = done state
                btnNext.setForeground(Color.WHITE);
            } else {
                btnNext.setText("Finish");
                btnNext.setEnabled(true);
                btnNext.setBackground(GREEN);
                btnNext.setForeground(Color.WHITE);
            }
        } else {
            btnNext.setText("Next →");
            btnNext.setEnabled(true);
            btnNext.setBackground(GREEN);
            btnNext.setForeground(Color.WHITE);
        }
    }

    void onFinishClick() {
        if (module.isCompleted()) {
            return;  // already finished — no-op
        }

        module.markFinished();
        refresh();

        JOptionPane.showMessageDialog(this,
            "You have completed the Learning Module!\n"
            + "Head to the Quiz tab to test your knowledge.",
            "Module Complete!", JOptionPane.INFORMATION_MESSAGE);
        // TODO: gamifModule.awardBadge("Learning Champion");
    }

    public LearningModule getModule() { return module; }
}
