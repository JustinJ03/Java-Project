// SUGGESTION (AI-generated reference). Rename to Question.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 2
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
