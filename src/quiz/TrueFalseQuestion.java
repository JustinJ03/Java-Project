package quiz;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Creator : Vince Adhar Anak Sekelie (106653)
 * Tester/Reviewer  : Justin Joel anak Juni (104607)
 * 
 * True/False question. Renders two JRadioButtons labelled "True" and "False".
 * selectedAnswer stores "true" or "false" as a string; checkAnswer() parses it back
 * to boolean for comparison with the stored correct answer.
 */
public class TrueFalseQuestion extends Question {

    // Stores if the correct answer is 'true' or 'false'
    private final boolean correct;
    
    // Remembers what the user clicked
    private String selectedAnswer = "";

    // Constructor: Sets up the True/False question
    public TrueFalseQuestion(String prompt, boolean correct) {
        super(prompt);
        this.correct = correct;
    }

    // Checks if the user's answer matches the correct answer
    @Override
    public boolean checkAnswer(String response) {
        if (response == null || response.trim().isEmpty()) {
            return false;
        }
        
        // converts the string "true"/"false" to a real boolean for comparison with the stored answer
        boolean playerAnswer = Boolean.parseBoolean(response.trim());
        
        // Checks if they match
        return playerAnswer == correct;
    }

    //visual UI for the True/False screen
    @Override
    public JComponent render() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // The question text, formatted to wrap correctly inside the screen
        JLabel promptLabel = new JLabel("<html><div style='width:100%; font-size:16px; font-family: sans-serif; font-weight: bold; color: #2C3E50; margin-bottom:15px;'>" 
                                        + prompt + "</div></html>");
        panel.add(promptLabel);
        panel.add(Box.createVerticalStrut(10)); // Add spacing

        // Groups the buttons so only one can be checked at a time
        ButtonGroup group = new ButtonGroup();
        
        // Create the "True" button
        JRadioButton btnTrue = new JRadioButton("True");
        btnTrue.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnTrue.setForeground(new Color(60, 60, 60));
        btnTrue.setOpaque(false);
        btnTrue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTrue.setBorder(new EmptyBorder(5, 5, 5, 5));
        // Save "true" if they click this
        btnTrue.addActionListener(e -> selectedAnswer = "true");
        
        // Create the "False" button
        JRadioButton btnFalse = new JRadioButton("False");
        btnFalse.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btnFalse.setForeground(new Color(60, 60, 60));
        btnFalse.setOpaque(false);
        btnFalse.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFalse.setBorder(new EmptyBorder(5, 5, 5, 5));
        // Save "false" if they click this
        btnFalse.addActionListener(e -> selectedAnswer = "false");

        group.add(btnTrue);
        group.add(btnFalse);

        panel.add(btnTrue);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnFalse);

        return panel;
    }

    // Allows the QuizModule to read what the user picked
    public String getSelectedAnswer() {
        return selectedAnswer;
    }
}
