// Starter file. Implement the // TODO blocks below.
package quiz;

import javax.swing.JComponent;

/** Abstract question. Subclasses override checkAnswer() and render() (polymorphism). */
public abstract class Question {
    protected final String prompt;

    protected Question(String prompt) { this.prompt = prompt; }

    public String getPrompt() { return prompt; }

    public abstract boolean checkAnswer(String response);
    public abstract JComponent render();
}
