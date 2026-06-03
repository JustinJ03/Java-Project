package quiz;

import data.DataManager;
import data.exceptions.ContentLoadException;
import data.exceptions.InvalidScoreException;
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

public class QuizModule extends JPanel implements IQuiz {

    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0;
    private int currentQuestionIndex = 0;
    private boolean isAnswerSubmitted = false; // Tracks if we are in the "feedback" state

    // GUI Components
    private JPanel questionContainer;
    private JLabel progressLabel;
    private JButton btnAction;
    private JLabel feedbackLabel; // Shows "Correct!" or "Incorrect!"

    public QuizModule() {
        super();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Soft modern background
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        progressLabel = new JLabel("");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        progressLabel.setForeground(new Color(44, 62, 80));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Question Card (The visual improvement)
        questionContainer = new JPanel(new BorderLayout());
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Feedback & Action Area
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
        bottomPanel.add(btnAction, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(questionContainer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        loadQuestions();
        showStartScreen(); 
    }

    // ... [Keep your existing loadQuestions(), scorePercent(), and motivationalMessage() methods exactly the same here] ...

    @Override
    public List<Question> loadQuestions() {
        questions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/quiz/questions.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("\\|");
                String type = parts[0].trim();
                
                if (type.equals("MCQ")) {
                    String prompt = parts[1].trim();
                    List<String> opts = Arrays.asList(parts[2].split(";"));
                    int correctIdx = Integer.parseInt(parts[3].trim());
                    questions.add(new MCQQuestion(prompt, opts, correctIdx));
                } else if (type.equals("TF")) {
                    String prompt = parts[1].trim();
                    boolean answer = Boolean.parseBoolean(parts[3].trim());
                    questions.add(new TrueFalseQuestion(prompt, answer));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(new ContentLoadException("Cannot load questions", e));
        }
        return questions;
    }

    @Override
    public int scorePercent() {
        return questions.isEmpty() ? 0 : (correctCount * 100) / questions.size();
    }

    @Override
    public String motivationalMessage(int percent) {
        if (percent >= 80) return "Outstanding!";
        if (percent >= 60) return "That's good!";
        if (percent >= 40) return "Good try!";
        if (percent >= 20) return "You can do better!";
        return "Don't give up!";
    }

    private void showStartScreen() {
        questionContainer.removeAll();
        questionContainer.setBackground(new Color(245, 247, 250)); // Match background
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

        JButton btnStart = new JButton("Start Quiz");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnStart.setBackground(new Color(26, 122, 74));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.addActionListener(e -> startQuiz());

        startPanel.add(Box.createVerticalStrut(60)); 
        startPanel.add(titleLabel);
        startPanel.add(Box.createVerticalStrut(40));
        startPanel.add(btnStart);

        questionContainer.add(startPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    private void startQuiz() {
        Collections.shuffle(questions);
        correctCount = 0;
        currentQuestionIndex = 0;
        
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        
        showQuestion(currentQuestionIndex);
    }

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

    // The new logic that handles the two-step Submit -> Next process
    private void handleAction() {
        if (!isAnswerSubmitted) {
            // STEP 1: Grade the answer and show feedback
            Question q = questions.get(currentQuestionIndex);
            String answer = "";
            
            if (q instanceof MCQQuestion) answer = ((MCQQuestion) q).getSelectedAnswer();
            else if (q instanceof TrueFalseQuestion) answer = ((TrueFalseQuestion) q).getSelectedAnswer();

            if (q.checkAnswer(answer)) {
                correctCount++;
                feedbackLabel.setForeground(new Color(22, 163, 90)); // Success Green
                feedbackLabel.setText("✔ Correct!");
            } else {
                feedbackLabel.setForeground(new Color(220, 38, 38)); // Error Red
                feedbackLabel.setText("✘ Incorrect");
            }

            isAnswerSubmitted = true;
            btnAction.setText(currentQuestionIndex == questions.size() - 1 ? "Finish Quiz" : "Next Question");
            
        } else {
            // STEP 2: Move to the next screen
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex);
            } else {
                finishQuiz();
            }
        }
    }

    private void finishQuiz() {
        int percent = scorePercent();
        String feedback = motivationalMessage(percent);
        
        try {
            DataManager dm = new DataManager("data/scores.txt");
            dm.saveScore("Player1", percent);
        } catch (InvalidScoreException e) {
            e.printStackTrace();
        }

        btnAction.setVisible(false); 
        feedbackLabel.setText("");
        questionContainer.removeAll();
        progressLabel.setText("Your Results");
        
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        
        JLabel scoreLabel = new JLabel(percent + "%");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        scoreLabel.setForeground(percent >= 60 ? new Color(26, 122, 74) : new Color(234, 88, 12));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel resultLabel = new JLabel("<html><center><i>" + feedback + "</i></center></html>");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnRestart = new JButton("Play Again");
        btnRestart.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRestart.setBackground(new Color(26, 122, 74));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setFocusPainted(false);
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRestart.addActionListener(e -> showStartScreen());

        resultsPanel.add(Box.createVerticalStrut(30));
        resultsPanel.add(scoreLabel);
        resultsPanel.add(Box.createVerticalStrut(15));
        resultsPanel.add(resultLabel);
        resultsPanel.add(Box.createVerticalStrut(40));
        resultsPanel.add(btnRestart);

        questionContainer.add(resultsPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    public JComponent getView() {
        return this;
    }

    public void onShow() {
        showStartScreen(); 
    }
}
