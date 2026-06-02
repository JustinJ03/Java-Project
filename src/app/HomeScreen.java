package app;

import javax.swing.*;
import java.awt.*;

/** Main menu with buttons to each module. */
public class HomeScreen extends Screen {
    public HomeScreen(AppContext ctx) {
        super(ctx, "EduSDG - SDG 4 Quality Education");
        JPanel menu = new JPanel(new GridLayout(0, 1, 10, 10));
        menu.setBackground(Theme.BG);
        menu.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));
        menu.add(navButton("Learn", "Learning"));
        menu.add(navButton("Take Quiz", "Quiz"));
        menu.add(navButton("Achievements", "Gamification"));
        add(menu, BorderLayout.CENTER);
    }

    private JButton navButton(String label, String target) {
        JButton b = new JButton(label);
        b.setFont(Theme.BODY_FONT);
        b.addActionListener(e -> ctx.navigator().show(target));
        return b;
    }
}
