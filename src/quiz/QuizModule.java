package quiz;

import data.DataManager;
import data.exceptions.ContentLoadException;
import data.exceptions.InvalidScoreException;
import interfaces.IQuiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizModule extends JPanel implements IQuiz {

    private final List<Question> questions = new ArrayList<>();
    private int correctCount = 0;
    private int currentQuestionIndex = 0;
    private boolean isAnswerSubmitted = false; 

    // Visual UI components for the screen
    private JPanel questionContainer; 
    private JLabel progressLabel; 
    private JButton btnAction; 
    private JLabel feedbackLabel;

    // Constructor: Sets up the main layout when the app starts
    public QuizModule() {
        super();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding around the edges

        //Header
        progressLabel = new JLabel("");
        progressLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        progressLabel.setForeground(new Color(44, 62, 80));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(progressLabel, BorderLayout.WEST);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        //Main Content Card (Center)
        questionContainer = new JPanel(new BorderLayout());
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        //Action Area (Bottom)
        feedbackLabel = new JLabel(""); // Starts empty
        feedbackLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnAction = new JButton("Submit Answer");
        btnAction.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAction.setBackground(new Color(26, 122, 74));
        btnAction.setForeground(Color.WHITE);
        btnAction.setFocusPainted(false);
        btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAction.setPreferredSize(new Dimension(150, 40));
        btnAction.setVisible(false); // Hidden until quiz starts
        
        // When the button is clicked, run the handleAction() method
        btnAction.addActionListener(e -> handleAction());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        bottomPanel.add(feedbackLabel, BorderLayout.CENTER);
        bottomPanel.add(btnAction, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(questionContainer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Read the text file and show the welcome page
        loadQuestions();
        showStartScreen(); 
    }

    // Reads questions.txt and turns them into Java objects
    @Override
    public List<Question> loadQuestions() {
        questions.clear();
        try (BufferedReader br = new BufferedReader(new FileReader("resources/quiz/questions.txt"))) {
            String line;
            // Read every line one by one
            while ((line = br.readLine()) != null) {
                // Ignore empty lines or comments starting with #
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                // Split the text by the | symbol
                String[] parts = line.split("\\|");
                String type = parts[0].trim(); // Check if it is MCQ or TF
                
                if (type.equals("MCQ")) {
                    String prompt = parts[1].trim();
                    // Split the options by the ; symbol
                    List<String> opts = Arrays.asList(parts[2].split(";"));
                    int correctIdx = Integer.parseInt(parts[3].trim());
                    // Create the MCQ object and add to list
                    questions.add(new MCQQuestion(prompt, opts, correctIdx));
                    
                } else if (type.equals("TF")) {
                    String prompt = parts[1].trim();
                    boolean answer = Boolean.parseBoolean(parts[3].trim());
                    // Create the True/False object and add to list
                    questions.add(new TrueFalseQuestion(prompt, answer));
                }
            }
        } catch (IOException e) {
            // Stop the program if the file is missing
            throw new RuntimeException(new ContentLoadException("Cannot load questions", e));
        }
        return questions;
    }

    // Calculates percentage (correct / total * 100)
    @Override
    public int scorePercent() {
        if (questions.isEmpty()) return 0;
        return (correctCount * 100) / questions.size();
    }

    // Syllabus grading scale
    @Override
    public String motivationalMessage(int percent) {
        if (percent >= 80) return "Outstanding!";
        if (percent >= 60) return "That's good!";
        if (percent >= 40) return "Good try!";
        if (percent >= 20) return "You can do better!";
        return "Don't give up!";
    }

    // Builds the landing page before the quiz starts
    private void showStartScreen() {
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
        
        JLabel descLabel = new JLabel("<html><center>Test your knowledge on SDG 4!<br><br>Questions are randomized.</center></html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnStart = new JButton("Start Quiz");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnStart.setBackground(new Color(26, 122, 74));
        btnStart.setForeground(Color.WHITE);
        btnStart.setFocusPainted(false);
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // When clicked, start the game
        btnStart.addActionListener(e -> startQuiz());

        startPanel.add(Box.createVerticalStrut(60)); 
        startPanel.add(titleLabel);
        startPanel.add(Box.createVerticalStrut(20));
        startPanel.add(descLabel);
        startPanel.add(Box.createVerticalStrut(40));
        startPanel.add(btnStart);

        questionContainer.add(startPanel, BorderLayout.CENTER);
        questionContainer.revalidate(); // Refreshes UI
        questionContainer.repaint();
    }

    // Sets up the game to begin
    private void startQuiz() {
        // Randomizes the list so it is different every time
        Collections.shuffle(questions); 
        correctCount = 0; // Reset score
        currentQuestionIndex = 0; // Start at question 1
        
        // Turn the center panel back into a white card
        questionContainer.setBackground(Color.WHITE);
        questionContainer.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 224, 228), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));
        
        showQuestion(currentQuestionIndex);
    }

    // Puts the current question's UI onto the screen
    private void showQuestion(int index) {
        questionContainer.removeAll();
        isAnswerSubmitted = false; // Reset to allow clicking
        feedbackLabel.setText(""); // Clear feedback
        btnAction.setText("Submit Answer");
        btnAction.setVisible(true);

        if (index < questions.size()) {
            // Update the top label (e.g., Question 1 of 20)
            progressLabel.setText("Question " + (index + 1) + " of " + questions.size());
            
            // Get the question object and call its render() method to draw it
            questionContainer.add(questions.get(index).render(), BorderLayout.CENTER);
        }
        questionContainer.revalidate();
        questionContainer.repaint();
    }

    // Handles what happens when the main bottom button is clicked
    private void handleAction() {
        if (!isAnswerSubmitted) {
            // STEP 1: The user clicked "Submit"
            Question q = questions.get(currentQuestionIndex);
            String answer = "";
            
            // Look inside the class to find out what radio button they clicked
            if (q instanceof MCQQuestion) answer = ((MCQQuestion) q).getSelectedAnswer();
            else if (q instanceof TrueFalseQuestion) answer = ((TrueFalseQuestion) q).getSelectedAnswer();

            // Check if it is correct
            if (q.checkAnswer(answer)) {
                correctCount++; // Increase score
                feedbackLabel.setForeground(new Color(22, 163, 90)); // Green text
                feedbackLabel.setText("✔ Correct!");
            } else {
                feedbackLabel.setForeground(new Color(220, 38, 38)); // Red text
                feedbackLabel.setText("✘ Incorrect");
            }

            isAnswerSubmitted = true; // Lock in the answer
            
            // Change the button text depending on if we are at the end
            if (currentQuestionIndex == questions.size() - 1) {
                btnAction.setText("Finish Quiz");
            } else {
                btnAction.setText("Next Question");
            }
            
        } else {
            // STEP 2: The user clicked "Next Question" or "Finish"
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                showQuestion(currentQuestionIndex); // Load next
            } else {
                finishQuiz(); // Game over
            }
        }
    }

    // Calculates final results and saves them
    private void finishQuiz() {
        int percent = scorePercent();
        String feedback = motivationalMessage(percent);
        
        // Save the score using Member 4's DataManager class
        try {
            DataManager dm = new DataManager("data/scores.txt");
            dm.saveScore("Player1", percent);
        } catch (InvalidScoreException e) {
            e.printStackTrace(); // Print error if something breaks
        }

        // Hide action button and clear the screen
        btnAction.setVisible(false); 
        feedbackLabel.setText("");
        questionContainer.removeAll();
        progressLabel.setText("Your Results");
        
        // Build the final score screen
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setOpaque(false);
        
        // Huge percentage text
        JLabel scoreLabel = new JLabel(percent + "%");
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        scoreLabel.setForeground(percent >= 60 ? new Color(26, 122, 74) : new Color(234, 88, 12));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Motivational message from rubric
        JLabel resultLabel = new JLabel("<html><center><i>" + feedback + "</i></center></html>");
        resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Button to restart the game
        JButton btnRestart = new JButton("Play Again");
        btnRestart.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnRestart.setBackground(new Color(26, 122, 74));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setFocusPainted(false);
        btnRestart.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // If clicked, go all the way back to the start screen
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

    // This runs every time the user clicks the "Quiz" tab at the bottom
    public void onShow() {
        showStartScreen(); // Always reset to the start menu when clicking the tab
    }
}
