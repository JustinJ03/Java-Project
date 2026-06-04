package learning;
/**
 * IModule.java
 * Interface for the Learning Module component.
 *
 * Creator  : Adriana Umang Anak Nawang (103088)
 * Tester   : Dhiaurrahman Bin Muhammad Dino (102521)
 *
 * This interface defines the contract for any module in the SDG4 Quality Education app.
 * It enforces that all modules can load content, navigate pages, and report progress.
 */
public interface IModule {

    /**
     * Load the module's content pages into memory.
     */
    void loadContent();

    /**
     * Display the content for a specific page number.
     * @param pageNumber the 1-based index of the page to display
     */
    void displayPage(int pageNumber);

    /**
     * Advance to the next content page.
     */
    void nextPage();

    /**
     * Go back to the previous content page.
     */
    void prevPage();

    /**
     * Get the total number of pages/screens in this module.
     * @return total page count
     */
    int getTotalPages();

    /**
     * Get the index of the currently displayed page.
     * @return current page number (1-based)
     */
    int getCurrentPage();

    /**
     * Get the completion progress of this module as a percentage (0-100).
     * @return progress percentage
     */
    double getProgress();

    /**
     * Check whether the module has been fully completed.
     * @return true if all pages have been viewed
     */
    boolean isCompleted();

    /**
     * Reset the module back to page 1 and clear progress.
     */
    void reset();
}
