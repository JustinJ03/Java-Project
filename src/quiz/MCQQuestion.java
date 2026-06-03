package quiz;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class MCQQuestion extends Question {

    // These variables store the choices (options) and which one is correct
    private final List<String> options;
    private final int correctIndex;
    
    // This remembers what the player clicked before they hit "Submit"
    private String selectedAnswer = "";

    // Constructor: Sets up the question when it is first created
    public MCQQuestion(String prompt, List<String> options, int correctIndex) {
        super(prompt); // Sends the question text to the parent class
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public List<String> getOptions() { 
        return options; 
    }

    // This method checks if the player picked the right answer
    @Override
    public boolean checkAnswer(String response) {
        // If they didn't pick anything, it's automatically wrong
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        
        // Find the actual text of the correct answer
        String correct = options.get(correctIndex);
        
        // Compare what the user picked to the correct answer (ignoring capital letters)
        return correct.trim().equalsIgnoreCase(response.trim());
    }

    //visual UI for the question
    @Override
    public JComponent render() {
        // Create a blank panel that stacks items vertically
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Create the question text (wrapped in HTML so long questions don't get cut off)
        JLabel promptLabel = new JLabel("<html><div style='width:320px; font-size:16px; font-family: sans-serif; font-weight: bold; color: #2C3E50; margin-bottom:15px;'>" 
                                        + prompt + "</div></html>");
        panel.add(promptLabel);
        panel.add(Box.createVerticalStrut(10)); // Adds empty space below the text

        // ButtonGroup makes sure the user can only select one circle at a time
        ButtonGroup group = new ButtonGroup();
        
        // Loop through every option (A, B, C, D) and make a radio button for it
        for (String option : options) {
            // HTML wrap prevents long answers from going off the screen
            JRadioButton rb = new JRadioButton("<html><div style='width: 300px; padding-bottom: 5px;'>" + option + "</div></html>");
            rb.setFont(new Font("SansSerif", Font.PLAIN, 14));
            rb.setForeground(new Color(60, 60, 60));
            rb.setOpaque(false);
            rb.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Changes mouse to a pointer hand
            rb.setBorder(new EmptyBorder(5, 5, 5, 5));
            
            // When clicked, save this option as the selected answer
            rb.addActionListener(e -> selectedAnswer = option);
            
            group.add(rb);
            panel.add(rb);
            panel.add(Box.createVerticalStrut(5)); // Add small spacing between choices
        }

        return panel;
    }

    // Allows the main QuizModule to ask this class what the user selected
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
