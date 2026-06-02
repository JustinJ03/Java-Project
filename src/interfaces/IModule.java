// Starter file. Implement the // TODO blocks below.
package interfaces;

import javax.swing.JComponent;

/** Contract every app module (Learning, Quiz, Gamification) implements. */
public interface IModule {
    String getName();
    JComponent getView();
    void onShow();
}
