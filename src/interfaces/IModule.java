package interfaces;

import javax.swing.JComponent;

/**
 * Contract that every application module must fulfil.
 *
 * Implemented by: LearningModule (Member 1), QuizModule (Member 2),
 * GamificationModule (Member 3).
 *
 * The Navigator uses this interface to hold and switch between all modules
 * without knowing their concrete types -- this is polymorphism in action.
 */
public interface IModule {

    /** Returns the display name used to register and navigate to this module. */
    String getName();

    /** Returns the root Swing component that fills the module's screen area. */
    JComponent getView();

    /** Called by the Navigator every time the user navigates to this module. */
    void onShow();
}
