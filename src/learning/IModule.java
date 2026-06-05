package learning;
/**
 * IModule.java
 * Interface for the Learning Module component.
 *
 * Creator  : Adriana Umang Anak Nawang (103088)
 * Tester   : Dhiaurrahman Bin Muhammad Dino (102521)
 *
 * Defines the contract for any navigable content module: load pages into memory,
 * navigate forward and backward between them, and report the user's progress.
 * The primary implementation is {@link LearningModule}.
 */
public interface IModule {

    /**
     * Load the module's content pages into memory.
     * Must be called before any navigation or progress queries.
     */
    void loadContent();

    /**
     * Display the content for a specific page number and mark it as viewed.
     * @param pageNumber the 1-based index of the page to display
     */
    void displayPage(int pageNumber);

    /**
     * Advance to the next content page; no-op if already on the last page.
     */
    void nextPage();

    /**
     * Go back to the previous content page; no-op if already on the first page.
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
     * Get the completion progress of this module as a percentage (0–100).
     * Calculated from the number of pages the user has viewed.
     * @return progress percentage
     */
    double getProgress();

    /**
     * Check whether the module has been explicitly marked finished by the user.
     * Note: viewing all pages does not automatically set the completed flag.
     * @return true if the user has tapped Finish on the last page
     */
    boolean isCompleted();

    /**
     * Reset the module back to page 1, clearing all viewed flags and the finished state.
     */
    void reset();
}
