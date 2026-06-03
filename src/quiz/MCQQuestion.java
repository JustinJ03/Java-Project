package quiz;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MCQQuestion extends Question {

    private final List<String> options;
    private final int correctIndex;
    
    // Tracks the user's selection locally so the QuizModule can retrieve it
    private String selectedAnswer = "";

    public MCQQuestion(String prompt, List<String> options, int correctIndex) {
        super(prompt);
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public List<String> getOptions() { 
        return options; 
    }

    @Override
    public boolean checkAnswer(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        String correct = options.get(correctIndex);
        return correct.trim().equalsIgnoreCase(response.trim());
    }

    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Larger, bolder question text
        JLabel promptLabel = new JLabel("<html><p style='width:250px; font-size:16px; font-family: sans-serif; font-weight: bold; color: #2C3E50; margin-bottom:15px;'>" 
                                        + prompt + "</p></html>");
        panel.add(promptLabel);
        panel.add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        
        // MCQQuestion.java loop:
        for (String option : options) {
            JRadioButton rb = new JRadioButton(option);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 15));
            rb.setForeground(new Color(60, 60, 60));
            rb.setOpaque(false);
            rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            rb.setBorder(new EmptyBorder(5, 5, 5, 5));
            rb.addActionListener(e -> selectedAnswer = option);
            group.add(rb);
            panel.add(rb);
            panel.add(Box.createVerticalStrut(8)); // Adds spacing between options
        }
        return panel;
    }

    // Helper method allowing QuizModule to easily read the final choice
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
