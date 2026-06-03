package learning;
/**
 * ContentPage.java
 * Represents one educational content page inside a LearningModule.
 *
 * Creator : Adriana Umang Anak Nawang (103088)
 * Tester  :
 */
public class ContentPage {

    // ── Attributes ────────────────────────────────────────────────────────────
    private int    pageNumber;  // e.g. 1, 2, 3... (must be unique within a module)
    private String title;       // e.g. "Inclusive Education"
    private String bodyText;    // main content text (can be multiple paragraphs)
    private String imagePath;   // relative path, e.g. "images/sdg4_intro.png"
    private String keyFact;     // optional highlighted fact (null if none)
    private boolean viewed;     // MUST default to false — do NOT change this

    // ── Constructors ──────────────────────────────────────────────────────────

    /** Full constructor. */
    public ContentPage(int pageNumber, String title, String bodyText,
                       String imagePath, String keyFact) {
        this.pageNumber = pageNumber;   // <-- must be set by caller, not auto-generated
        this.title      = title;        // <-- must be set by caller, not auto-generated
        this.bodyText   = bodyText;    // <-- must be set by caller, not auto-generated
        this.imagePath  = imagePath;   // <-- can be null if no image, but must be set by caller (not auto-generated)
        this.keyFact    = keyFact;     // <-- can be null if no key fact, but must be set by caller (not auto-generated)
        this.viewed     = false;       // <-- always starts as false
    }

    /** Overloaded: no key fact. */
    public ContentPage(int pageNumber, String title,
                       String bodyText, String imagePath) {
        this(pageNumber, title, bodyText, imagePath, null);     // <-- keyFact is null if not provided
    }

    /** Overloaded: no image, no key fact. */
    public ContentPage(int pageNumber, String title, String bodyText) {
        this(pageNumber, title, bodyText, null, null);  // <-- imagePath and keyFact are null if not provided
    }

    // ── Methods ───────────────────────────────────────────────────────────────

    /** Mark this page as read by the user. */
    public void markViewed() { this.viewed = true; }

    /** Reset this page back to unread. */
    public void resetViewed() { this.viewed = false; }

    /** True if the user has opened this page at least once. */
    public boolean isViewed() { return viewed; }

    /**
     * One-line summary for the module list panel.
     * e.g. "[4] Inclusive Education  (viewed)"
     */
    public String getSummary() {
        return String.format("[%d] %s  %s",
                pageNumber, title, viewed ? "(viewed)" : "(unread)");      // <-- note: the "(viewed)" or "(unread)" status is shown in the summary
    }

    /** Full formatted content string (used for console/debug output). */
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();     // <-- using StringBuilder for better performance when concatenating multiple lines
        sb.append("=== ").append(title).append(" ===\n\n");     // <-- title is prominently displayed at the top
        sb.append(bodyText).append("\n");       // <-- body text is the main content, followed by a newline
        if (keyFact != null && !keyFact.isEmpty())      // <-- only include key fact if it's not null or empty
            sb.append("\nKey Fact: ").append(keyFact).append("\n");     // <-- key fact is highlighted with a "Key Fact:" label and separated by newlines for emphasis
        return sb.toString();       // <-- the imagePath is not included in the formatted content string, as it's meant for console/debug output and cannot display images
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public int    getPageNumber() { return pageNumber; }
    public String getTitle()      { return title; }
    public String getBodyText()   { return bodyText; }
    public String getImagePath()  { return imagePath; }
    public String getKeyFact()    { return keyFact; }

    public void setTitle(String t)    { this.title = t; }
    public void setBodyText(String b) { this.bodyText = b; }
    public void setImagePath(String p){ this.imagePath = p; }
    public void setKeyFact(String f)  { this.keyFact = f; }

    @Override
    public String toString() {
        return "ContentPage{page=" + pageNumber
                + ", title='" + title + "', viewed=" + viewed + "}";        // <-- toString() provides a concise summary of the page, including its number, title, and viewed status (but not the full content or image path)
    }
}
