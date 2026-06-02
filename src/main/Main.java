package main;

import app.AppContext;
import app.HomeScreen;
import app.Theme;
import learning.LearningModule;
import quiz.QuizModule;
import gamification.GamificationModule;
import javax.swing.*;

/** Application entry point. Build & run from the command line (see build.bat / run.bat). */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        AppContext ctx = new AppContext();
        ctx.navigator().registerScreen("home", new HomeScreen(ctx));
        ctx.navigator().registerModule(new LearningModule(ctx));
        ctx.navigator().registerModule(new QuizModule(ctx));
        ctx.navigator().registerModule(new GamificationModule(ctx));

        JFrame frame = new JFrame("EduSDG - SDG 4 Quality Education");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(ctx.navigator().getContainer());
        frame.setResizable(false);
        frame.pack();
        frame.setSize(Theme.APP_WIDTH, Theme.APP_HEIGHT);
        frame.setLocationRelativeTo(null);
        ctx.navigator().show("home");
        frame.setVisible(true);
    }
}
