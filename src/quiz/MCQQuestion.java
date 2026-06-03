package quiz;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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

        JLabel promptLabel = new JLabel("<html><p style='width:350px; font-size:14px; margin-bottom:10px;'>" 
                                        + prompt + "</p></html>");
        panel.add(promptLabel);

        ButtonGroup group = new ButtonGroup();
        for (String option : options) {
            JRadioButton rb = new JRadioButton(option);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 13));
            rb.setOpaque(false);
            
            // Update the local state whenever a radio button is clicked
            rb.addActionListener(e -> selectedAnswer = option);
            
            group.add(rb);
            panel.add(rb);
        }

        return panel;
    }

    // Helper method allowing QuizModule to easily read the final choice
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
