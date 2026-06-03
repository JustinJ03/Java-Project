package quiz;

import javax.swing.*;
import java.awt.*;

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

        JLabel promptLabel = new JLabel("<html><p style='width:350px; font-size:14px; margin-bottom:10px;'>" 
                                        + prompt + "</p></html>");
        panel.add(promptLabel);

        ButtonGroup group = new ButtonGroup();
        
        JRadioButton btnTrue = new JRadioButton("True");
        btnTrue.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnTrue.setOpaque(false);
        btnTrue.addActionListener(e -> selectedAnswer = "true");
        
        JRadioButton btnFalse = new JRadioButton("False");
        btnFalse.setFont(new Font("SansSerif", Font.PLAIN, 13));
        btnFalse.setOpaque(false);
        btnFalse.addActionListener(e -> selectedAnswer = "false");

        group.add(btnTrue);
        group.add(btnFalse);

        panel.add(btnTrue);
        panel.add(btnFalse);

        return panel;
    }

    // Helper method allowing QuizModule to easily read the final choice
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
