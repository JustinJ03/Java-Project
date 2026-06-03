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

public class QuizModule extends JPanel implements IQuiz {

    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0;
    private int currentQuestionIndex = 0;
    private String playerName = "Player"; // set when the player enters their name

    // GUI Components
    private JPanel questionContainer;
    private JLabel progressLabel;
    private JButton btnNext;

    public QuizModule() {
        super();
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize UI panels
        questionContainer = new JPanel(new BorderLayout());
        questionContainer.setOpaque(false);

        progressLabel = new JLabel("");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        progressLabel.setForeground(new Color(26, 122, 74));

        btnNext = new JButton("Next / Submit");
        btnNext.setBackground(new Color(26, 122, 74));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFocusPainted(false);
        btnNext.setVisible(false); // hidden until the quiz actually starts
        btnNext.addActionListener(e -> advanceQuestion());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(questionContainer, BorderLayout.CENTER);
        add(btnNext, BorderLayout.SOUTH);

        // Load questions — show an error screen if questions.txt is missing
        try {
            loadQuestions();
        } catch (ContentLoadException e) {
            System.err.println("Warning: " + e.getMessage());
            // Show the start screen anyway; quiz will have 0 questions
        }
        showStartScreen();
    }

    @Override
    public List<Question> loadQuestions() throws ContentLoadException {
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
            throw new ContentLoadException("Cannot load questions from resources/quiz/questions.txt", e);
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
        else if (percent >= 60) return "That's good!";
        else if (percent >= 40) return "Good try!";
        else if (percent >= 20) return "You can do better!";
        else return "Don't give up!";
    }

    // Landing Page ===
    private void showStartScreen() {
        questionContainer.removeAll();
        progressLabel.setText(""); 
        btnNext.setVisible(false); 

        JPanel startPanel = new JPanel();
        startPanel.setLayout(new BoxLayout(startPanel, BoxLayout.Y_AXIS));
        startPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SDG 4: Quality Education Quiz");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnStart = new JButton("Start Quiz");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnStart.setBackground(new Color(26, 122, 74));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.addActionListener(e -> startQuiz());

        startPanel.add(Box.createVerticalStrut(80));
        startPanel.add(titleLabel);
        startPanel.add(Box.createVerticalStrut(40));
        startPanel.add(btnStart);

        questionContainer.add(startPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    // Prompt for the player's name, then start the quiz
    private void startQuiz() {
        // Member 4 integration: ask for player name; score will be saved to data/scores.txt
        String name = JOptionPane.showInputDialog(
                SwingUtilities.getWindowAncestor(this),
                "Enter your name to record your score:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE);
        playerName = (name != null && !name.trim().isEmpty()) ? name.trim() : "Player";

        Collections.shuffle(questions);
        correctCount = 0;
        currentQuestionIndex = 0;
        btnNext.setVisible(true);
        showQuestion(currentQuestionIndex);
    }

    private void showQuestion(int index) {
        questionContainer.removeAll();
        if (index < questions.size()) {
            progressLabel.setText("Question " + (index + 1) + " / " + questions.size());
            questionContainer.add(questions.get(index).render(), BorderLayout.CENTER);
            
            if (index == questions.size() - 1) {
                btnNext.setText("Finish Quiz");
            } else {
                btnNext.setText("Next Question");
            }
        }
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    private void advanceQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            String answer = "";
            
            if (q instanceof MCQQuestion) {
                answer = ((MCQQuestion) q).getSelectedAnswer();
            } else if (q instanceof TrueFalseQuestion) {
                answer = ((TrueFalseQuestion) q).getSelectedAnswer();
            }

            if (q.checkAnswer(answer)) {
                correctCount++;
            }

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
        
        // Member 4: persist score to text file via DataManager
        try {
            DataManager dm = new DataManager("data/scores.txt");
            dm.saveScore(playerName, percent);
        } catch (InvalidScoreException e) {
            System.err.println("Score save failed: " + e.getMessage());
        }

        btnNext.setVisible(false); // Hide next button on the final screen
        questionContainer.removeAll();
        progressLabel.setText("Results");
        
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        
        JLabel resultLabel = new JLabel("<html><center><h2>Quiz Complete!</h2><br>" 
                                        + "Score: " + percent + "%<br><br>" 
                                        + "<i>" + feedback + "</i></center></html>", SwingConstants.CENTER);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnRestart = new JButton("Play Again");
        btnRestart.setBackground(new Color(26, 122, 74));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setFocusPainted(false);
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRestart.addActionListener(e -> showStartScreen());

        resultsPanel.add(Box.createVerticalStrut(50));
        resultsPanel.add(resultLabel);
        resultsPanel.add(Box.createVerticalStrut(30));
        resultsPanel.add(btnRestart);

        questionContainer.add(resultsPanel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    public JComponent getView() {
        return this;
    }

    // clicking the Quiz tab always resets to the Start Screen
    public void onShow() {
        showStartScreen(); 
    }
}
