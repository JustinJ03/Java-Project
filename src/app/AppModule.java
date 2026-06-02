package app;

import interfaces.IModule;
import javax.swing.*;
import java.awt.*;

/** Abstract base every module extends. Demonstrates abstraction + inheritance. */
public abstract class AppModule implements IModule {
    protected final AppContext ctx;
    private final String name;

    protected AppModule(AppContext ctx, String name) {
        this.ctx = ctx;
        this.name = name;
    }

    @Override public String getName() { return name; }
    @Override public void onShow() { /* override to refresh when navigated to */ }

    /** Compile-ready placeholder shown until a member fills in getView(). */
    protected JComponent placeholder(String todo) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG);
        JLabel l = new JLabel(
            "<html><center><b>" + name + "</b><br>TODO: " + todo + "</center></html>",
            SwingConstants.CENTER);
        l.setFont(Theme.BODY_FONT);
        p.add(l, BorderLayout.CENTER);
        return p;
    }
}
