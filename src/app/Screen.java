package app;

import javax.swing.*;
import java.awt.*;

/** Abstract base for every full-screen panel. Provides a coloured title header. */
public abstract class Screen extends JPanel {
    protected final AppContext ctx;

    protected Screen(AppContext ctx, String title) {
        this.ctx = ctx;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        setPreferredSize(new Dimension(Theme.APP_WIDTH, Theme.APP_HEIGHT));
        add(buildHeader(title), BorderLayout.NORTH);
    }

    private JComponent buildHeader(String title) {
        JLabel h = new JLabel(title, SwingConstants.CENTER);
        h.setOpaque(true);
        h.setBackground(Theme.PRIMARY);
        h.setForeground(Color.WHITE);
        h.setFont(Theme.TITLE_FONT);
        h.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));
        return h;
    }
}
