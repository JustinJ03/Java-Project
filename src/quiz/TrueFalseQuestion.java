package quiz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TrueFalseQuestion extends Question {

    private final boolean correct;
    private String selectedAnswer = "";

    public TrueFalseQuestion(String prompt, boolean correct) {
        super(prompt);
        this.correct = correct;
    }

    @Override
    public boolean checkAnswer(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        boolean playerAnswer = Boolean.parseBoolean(response.trim());
        return playerAnswer == correct;
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
        
        // Create and style the True button
        JRadioButton btnTrue = new JRadioButton("True");
        btnTrue.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnTrue.setForeground(new Color(60, 60, 60));
        btnTrue.setOpaque(false);
        btnTrue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTrue.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnTrue.addActionListener(e -> selectedAnswer = "true");
        
        // Create and style the False button
        JRadioButton btnFalse = new JRadioButton("False");
        btnFalse.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnFalse.setForeground(new Color(60, 60, 60));
        btnFalse.setOpaque(false);
        btnFalse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFalse.setBorder(new EmptyBorder(5, 5, 5, 5));
        btnFalse.addActionListener(e -> selectedAnswer = "false");

        // Group them so only one can be selected at a time
        group.add(btnTrue);
        group.add(btnFalse);

        // Add them to the panel with some vertical spacing
        panel.add(btnTrue);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnFalse);

        return panel;
    }

    // Helper method allowing QuizModule to easily read the final choice
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
