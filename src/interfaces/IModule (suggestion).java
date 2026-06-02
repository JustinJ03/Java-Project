// SUGGESTION (AI-generated reference). Rename to IModule.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 1
package interfaces;

import javax.swing.JComponent;

/** Contract every app module (Learning, Quiz, Gamification) implements. */
public interface IModule {
    String getName();
    JComponent getView();
    void onShow();
}
