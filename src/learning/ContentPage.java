package learning;
/**
 * ContentPage.java
 * Represents one page of educational content inside a LearningModule.
 * Each page holds a title, body text, optional image, and an optional key fact.
 *
 * Creator : Adriana Umang Anak Nawang (103088)
 */
public class ContentPage {

    // ── Fields ────────────────────────────────────────────────────────────────
    private int    pageNumber;  // 1-based position within the module
    private String title;       // short heading shown at top of the page
    private String bodyText;    // main educational paragraph(s)
    private String imagePath;   // relative path to the image (null if no image)
    private String keyFact;     // highlighted takeaway fact (null if none)
    private boolean viewed;     // defaults to false; set true when the user navigates to this page

    // ── Constructors ──────────────────────────────────────────────────────────

    /**
     * Full constructor — all fields provided by the caller.
     * Two overloaded variants exist for pages that omit the key fact or both
     * the image and key fact; they delegate here with null for the missing fields.
     */
    public ContentPage(int pageNumber, String title, String bodyText,
                       String imagePath, String keyFact) {
        this.pageNumber = pageNumber;
        this.title      = title;
        this.bodyText   = bodyText;
        this.imagePath  = imagePath;
        this.keyFact    = keyFact;
        this.viewed     = false; // always unread until the user opens the page
    }

    /** Overloaded: no key fact (keyFact defaults to null). */
    public ContentPage(int pageNumber, String title,
                       String bodyText, String imagePath) {
        this(pageNumber, title, bodyText, imagePath, null);
    }

    /** Overloaded: no image and no key fact (both default to null). */
    public ContentPage(int pageNumber, String title, String bodyText) {
        this(pageNumber, title, bodyText, null, null);
    }

    // ── State methods ─────────────────────────────────────────────────────────

    /** Mark this page as read. Called by LearningModulePanel when displayed. */
    public void markViewed()  { this.viewed = true; }

    /** Reset this page back to unread (used when the module is reset). */
    public void resetViewed() { this.viewed = false; }

    /** Returns true if the user has opened this page at least once. */
    public boolean isViewed() { return viewed; }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int    getPageNumber() { return pageNumber; }
    public String getTitle()      { return title; }
    public String getBodyText()   { return bodyText; }
    public String getImagePath()  { return imagePath; }
    public String getKeyFact()    { return keyFact; }

    @Override
    public String toString() {
        return "ContentPage{page=" + pageNumber
                + ", title='" + title + "', viewed=" + viewed + "}";
    }
}
