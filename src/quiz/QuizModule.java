package quiz;

import data.DataManager;
import data.exceptions.ContentLoadException;
import data.exceptions.InvalidScoreException;
import interfaces.IQuiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuizModule extends JPanel implements IQuiz {

    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0;
    private int currentQuestionIndex = 0;

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

        progressLabel = new JLabel("Question 1 / 0");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        progressLabel.setForeground(new Color(26, 122, 74));

        btnNext = new JButton("Next / Submit");
        btnNext.setBackground(new Color(26, 122, 74));
        btnNext.setForeground(Color.WHITE);
        btnNext.setFocusPainted(false);
        btnNext.addActionListener(e -> advanceQuestion());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        add(headerPanel, BorderLayout.NORTH);
        add(questionContainer, BorderLayout.CENTER);
        add(btnNext, BorderLayout.SOUTH);

        // Load data and start quiz
        loadQuestions();
        showQuestion(0);
    }

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
            // Wrapping the checked exception in a RuntimeException as IQuiz interface doesn't declare throws
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
        else if (percent >= 60) return "That's good!";
        else if (percent >= 40) return "Good try!";
        else if (percent >= 20) return "You can do better!";
        else return "Don't give up!";
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
            
            // Extract the user's answer dynamically
            if (q instanceof MCQQuestion) {
                answer = ((MCQQuestion) q).getSelectedAnswer();
            } else if (q instanceof TrueFalseQuestion) {
                answer = ((TrueFalseQuestion) q).getSelectedAnswer();
            }

            // Grade answer
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
        
        // Save the score via Member 4's DataManager
        try {
            DataManager dm = new DataManager("data/scores.txt");
            dm.saveScore("Player1", percent);
        } catch (InvalidScoreException e) {
            e.printStackTrace();
        }

        // Output results to user
        btnNext.setEnabled(false);
        questionContainer.removeAll();
        
        JLabel resultLabel = new JLabel("<html><center><h2>Quiz Complete!</h2><br>" 
                                        + "Score: " + percent + "%<br><br>" 
                                        + "<i>" + feedback + "</i></center></html>", SwingConstants.CENTER);
        questionContainer.add(resultLabel, BorderLayout.CENTER);
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    public JComponent getView() {
        return this;
    }

    public void onShow() {
        correctCount = 0;
        currentQuestionIndex = 0;
        btnNext.setEnabled(true);
        loadQuestions();
        showQuestion(currentQuestionIndex);
    }
}
