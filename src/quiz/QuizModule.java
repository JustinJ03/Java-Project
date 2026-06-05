package quiz;
/**
 * Presents the SDG 4 quiz: loads questions from questions.txt, displays them one at a
 * time with a 300-second countdown timer, checks answers, saves the score to
 * data/scores.txt via DataManager (Member 4), and notifies GamificationModule to award
 * points and badges the moment the quiz ends (Member 3).
 *
 * Creator : Vince Adhar Anak Sekelie (106653)
 * Tester  :
 */
import data.DataManager;
import data.exceptions.ContentLoadException;
import data.exceptions.InvalidScoreException;
import gamification.GamificationModule;
import interfaces.IQuiz;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Creator : Vince Adhar Anak Sekelie (106653)
 * Tester/Reviewer  : Justin Joel anak Juni (104607)
 * 
 * Presents the SDG 4 quiz: loads questions from questions.txt, displays them one at a
 * time with a 300-second countdown timer, checks answers, saves the score to
 * data/scores.txt via DataManager (Member 4), and notifies GamificationModule to award
 * points and badges the moment the quiz ends (Member 3).
 *
 */
public class QuizModule extends JPanel implements IQuiz {

    // ── Quiz state ────────────────────────────────────────────────────────────
    private final List<Question> questions    = new ArrayList<>();
    private int    correctCount              = 0;
    private int    currentQuestionIndex      = 0;
    private boolean isAnswerSubmitted        = false;
    private String playerName               = "Player"; // set at quiz start via dialog

    // ── 300-second countdown timer ─────────────────────────────────────────────
    private static final int QUIZ_SECONDS    = 300;
    private javax.swing.Timer quizTimer;
    private int    remainingSeconds          = QUIZ_SECONDS;

    // ── Integration callbacks (set by Main.java) ──────────────────────────────
    /** Called when quiz ends — navigates to the Leaderboard tab. */
    private Runnable goToLeaderboardCallback  = null;
    /** Receives quiz result so gamification points/badges update immediately. */
    private GamificationModule gamifModule    = null;

    // ── UI components ─────────────────────────────────────────────────────────
    private JPanel  questionContainer;
    private JLabel  progressLabel;
    private JLabel  timerLabel;
    private JButton btnAction;
    private JLabel  feedbackLabel;

    // ── Constructor ───────────────────────────────────────────────────────────

    public QuizModule() {
        super();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header: progress left, timer right
        progressLabel = new JLabel("");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        progressLabel.setForeground(new Color(44, 62, 80));

        timerLabel = new JLabel("");
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        timerLabel.setForeground(new Color(26, 122, 74));
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.add(timerLabel,    BorderLayout.EAST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main content card (center)
        questionContainer = new JPanel(new BorderLayout());
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Bottom action area: feedback label + button
        feedbackLabel = new JLabel("");
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        btnAction = new JButton("Submit Answer");
        btnAction.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAction.setBackground(new Color(26, 122, 74));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAction.setPreferredSize(new Dimension(150, 40));
        btnAction.setVisible(false);
        btnAction.addActionListener(e -> handleAction());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        bottomPanel.add(feedbackLabel, BorderLayout.CENTER);
        bottomPanel.add(btnAction,     BorderLayout.EAST);

        add(headerPanel,       BorderLayout.NORTH);
        add(questionContainer, BorderLayout.CENTER);
        add(bottomPanel,       BorderLayout.SOUTH);

        // Load questions at construction; show start screen
        try {
            loadQuestions();
        } catch (ContentLoadException e) {
            System.err.println("Warning: Could not load questions — " + e.getMessage());
        }
        showStartScreen();
    }

    // ── Integration setters (called from Main.java after construction) ─────────

    /**
     * Provides a GamificationModule reference so points/badges update the moment
     * the quiz ends (OOP: cross-module communication via dependency injection).
     */
    public void setGamificationModule(GamificationModule gm) {
        this.gamifModule = gm;
    }

    /**
     * Provides a callback that navigates to the Leaderboard tab when the player
     * clicks "View Leaderboard" on the results screen.
     */
    public void setGoToLeaderboardCallback(Runnable callback) {
        this.goToLeaderboardCallback = callback;
    }

    // ── IQuiz implementation ──────────────────────────────────────────────────

    /** Parses questions.txt and populates the question list (≥20 questions). */
    @Override
    public List<Question> loadQuestions() throws ContentLoadException {
        questions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/quiz/questions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                // questions.txt format: MCQ|prompt|opt1;opt2;opt3;opt4|correctIndex  or  TF|prompt||true
                String[] parts = line.split("\\|");
                String type = parts[0].trim();
                if (type.equals("MCQ")) {
                    String prompt    = parts[1].trim();
                    List<String> opts = Arrays.asList(parts[2].split(";"));
                    int correctIdx   = Integer.parseInt(parts[3].trim());
                    questions.add(new MCQQuestion(prompt, opts, correctIdx));
                } else if (type.equals("TF")) {
                    String prompt  = parts[1].trim();
                    boolean answer = Boolean.parseBoolean(parts[3].trim());
                    questions.add(new TrueFalseQuestion(prompt, answer));
                }
            }
        } catch (IOException e) {
            throw new ContentLoadException("Cannot load questions from resources/quiz/questions.txt", e);
        }
        return questions;
    }

    /** Returns the score as a percentage (0-100). */
    @Override
    public int scorePercent() {
        return questions.isEmpty() ? 0 : (correctCount * 100) / questions.size();
    }

    /** Maps a percentage to the five-band motivational message required by the rubric. */
    @Override
    public String motivationalMessage(int percent) {
        if (percent >= 80) return "Outstanding!";
        if (percent >= 60) return "That's good!";
        if (percent >= 40) return "Good try!";
        if (percent >= 20) return "You can do better!";
        return "Don't give up!";
    }

    // ── Screen builders ───────────────────────────────────────────────────────

    /** Welcome / start screen. Stops any running timer. */
    private void showStartScreen() {
        stopTimer();
        timerLabel.setText("");
        questionContainer.removeAll();
        questionContainer.setBackground(new Color(245, 247, 250));
        questionContainer.setBorder(null);
        progressLabel.setText("");
        btnAction.setVisible(false);
        feedbackLabel.setText("");

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Quality Education Quiz");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(26, 122, 74));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(
                "<html><div style='text-align: center; width: 100%;'>Test your knowledge on SDG 4!<br><br>"
                + "Questions are randomized.<br>"
                + "You have <b>" + QUIZ_SECONDS + " seconds</b> to finish.</div></html>", SwingConstants.CENTER);
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnStart = new JButton("Start Quiz");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnStart.setBackground(new Color(26, 122, 74));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.addActionListener(e -> startQuiz());

        startPanel.add(Box.createVerticalStrut(60));
        startPanel.add(titleLabel);
        startPanel.add(Box.createVerticalStrut(20));
        startPanel.add(descLabel);
        startPanel.add(Box.createVerticalStrut(40));
        startPanel.add(btnStart);

        questionContainer.add(startPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    /**
     * Prompts the player for their name, resets all counters, shuffles the question list,
     * and starts the timer before displaying the first question.
     */
    private void startQuiz() {
        // Ask player name — score saved to data/scores.txt via DataManager (Member 4)
        String name = JOptionPane.showInputDialog(
                SwingUtilities.getWindowAncestor(this),
                "Enter your name to record your score:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE);
        playerName = (name != null && !name.trim().isEmpty()) ? name.trim() : "Player";

        correctCount          = 0;
        currentQuestionIndex  = 0;
        isAnswerSubmitted     = false;
        remainingSeconds      = QUIZ_SECONDS;

        // Restore card styling
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        Collections.shuffle(questions);
        startTimer();
        showQuestion(currentQuestionIndex);
    }

    // ── Timer logic ───────────────────────────────────────────────────────────

    /**
     * Starts a 1-second Swing timer countdown from QUIZ_SECONDS.
     * Auto-calls finishQuiz() when it reaches zero.
     */
    private void startTimer() {
        stopTimer(); // ensure no old timer running
        updateTimerDisplay();
        quizTimer = new javax.swing.Timer(1000, e -> {
            remainingSeconds--;
            updateTimerDisplay();
            if (remainingSeconds <= 0) {
                stopTimer();
                finishQuiz(); // time's up — auto-submit
            }
        });
        quizTimer.start();
    }

    /**
     * Stops the countdown timer. Safe to call even if the timer is already stopped or null.
     */
    private void stopTimer() {
        if (quizTimer != null && quizTimer.isRunning()) {
            quizTimer.stop();
        }
    }

    /**
     * Refreshes the timer label. Turns red when under 20 seconds to signal urgency.
     */
    private void updateTimerDisplay() {
        int m = remainingSeconds / 60;
        int s = remainingSeconds % 60;
        timerLabel.setText(String.format("⏱ %02d:%02d", m, s));
        timerLabel.setForeground(remainingSeconds <= 20
                ? new Color(220, 38, 38)   // red — hurry!
                : new Color(26, 122, 74)); // green — fine
    }

    // ── Question display ──────────────────────────────────────────────────────

    /**
     * Clears the question container and renders the question at the given index using
     * question.render() (polymorphism) — the JVM dispatches to MCQQuestion or TrueFalseQuestion.
     */
    private void showQuestion(int index) {
        questionContainer.removeAll();
        isAnswerSubmitted = false;
        feedbackLabel.setText("");
        btnAction.setText("Submit Answer");
        btnAction.setVisible(true);

        if (index < questions.size()) {
            progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
            questionContainer.add(questions.get(index).render(), BorderLayout.CENTER);
        }
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    // ── Answer handling ───────────────────────────────────────────────────────

    /**
     * Two-phase state machine driven by isAnswerSubmitted:
     * Phase 1 (false): validates the selected answer, checks correctness, shows feedback.
     * Phase 2 (true): advances to the next question or calls finishQuiz() on the last one.
     */
    private void handleAction() {
        if (!isAnswerSubmitted) {
            // — Submission step —
            Question q      = questions.get(currentQuestionIndex);
            String answer   = "";
            if (q instanceof MCQQuestion)       answer = ((MCQQuestion) q).getSelectedAnswer();
            else if (q instanceof TrueFalseQuestion) answer = ((TrueFalseQuestion) q).getSelectedAnswer();

            // block submission if no option was selected
            if (answer == null || answer.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        SwingUtilities.getWindowAncestor(this),
                        "Please select an answer before submitting.",
                        "No Answer Selected",
                        JOptionPane.WARNING_MESSAGE);
                return; // stay on the same question
            }

            if (q.checkAnswer(answer)) {
                // tally correct answers for the final score
                correctCount++;
                feedbackLabel.setForeground(new Color(22, 163, 90));
                feedbackLabel.setText("✔ Correct!");
            } else {
                feedbackLabel.setForeground(new Color(220, 38, 38));
                feedbackLabel.setText("✘ Incorrect");
            }
            isAnswerSubmitted = true;
            btnAction.setText(currentQuestionIndex == questions.size() - 1
                    ? "Finish Quiz" : "Next Question");

        } else {
            // — Navigation step —
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                finishQuiz();
            }
        }
    }

    // ── Results screen ────────────────────────────────────────────────────────

    /**
     * Ends the quiz. In order: stops timer, saves score to scores.txt via DataManager,
     * notifies GamificationModule to award points/badges, then builds and shows the results screen.
     */
    private void finishQuiz() {
        stopTimer();
        int timeLeft = remainingSeconds; // capture before reset
        int percent  = scorePercent();
        String feedback = motivationalMessage(percent);

        // persist score — Member 4's DataManager writes to data/scores.txt
        try {
            new DataManager("data/scores.txt").saveScore(playerName, percent);
        } catch (InvalidScoreException e) {
            System.err.println("Score save failed: " + e.getMessage());
        }

        // Build results screen
        btnAction.setVisible(false);
        feedbackLabel.setText("");
        timerLabel.setText("");
        questionContainer.removeAll();
        progressLabel.setText("Your Results");

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);

        // Big score percentage
        JLabel scoreLabel = new JLabel(percent + "%");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 52));
        scoreLabel.setForeground(percent >= 60 ? new Color(26, 122, 74) : new Color(234, 88, 12));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Breakdown line: N / total correct
        JLabel breakdownLabel = new JLabel(correctCount + " / " + questions.size() + " correct");
        breakdownLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        breakdownLabel.setForeground(new Color(80, 80, 80));
        breakdownLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Motivational message
        JLabel resultLabel = new JLabel("<html><center><i>" + feedback + "</i></center></html>");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Time used
        int usedSeconds = QUIZ_SECONDS - timeLeft;
        JLabel timeLabel = new JLabel("Time used: " + usedSeconds + "s of " + QUIZ_SECONDS + "s");
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        timeLabel.setForeground(new Color(120, 120, 120));
        timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Play Again button
        JButton btnRestart = new JButton("Play Again");
        btnRestart.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRestart.setBackground(new Color(26, 122, 74));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setFocusPainted(false);
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRestart.addActionListener(e -> showStartScreen());

        // View Leaderboard button — navigates to the Leaderboard (BADGES) tab
        JButton btnLeaderboard = new JButton("View Leaderboard");
        btnLeaderboard.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLeaderboard.setBackground(new Color(63, 81, 181));
        btnLeaderboard.setForeground(Color.WHITE);
        btnLeaderboard.setFocusPainted(false);
        btnLeaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLeaderboard.addActionListener(e -> {
            if (goToLeaderboardCallback != null) goToLeaderboardCallback.run();
        });

        // Button row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnRestart);
        btnRow.add(btnLeaderboard);

        resultsPanel.add(Box.createVerticalStrut(20));
        resultsPanel.add(scoreLabel);
        resultsPanel.add(Box.createVerticalStrut(6));
        resultsPanel.add(breakdownLabel);
        resultsPanel.add(Box.createVerticalStrut(12));
        resultsPanel.add(resultLabel);
        resultsPanel.add(Box.createVerticalStrut(8));
        resultsPanel.add(timeLabel);
        resultsPanel.add(Box.createVerticalStrut(30));
        resultsPanel.add(btnRow);

        questionContainer.add(resultsPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();

        // trigger badge/points update immediately — Member 3's GamificationModule
        if (gamifModule != null) {
            gamifModule.onQuizComplete(playerName, correctCount, questions.size(), timeLeft);
        }
    }

    // ── IQuiz extras ─────────────────────────────────────────────────────────

    public JComponent getView() { return this; }

    /** Resets to start screen whenever the Quiz tab is tapped. */
    public void onShow() { showStartScreen(); }
}
