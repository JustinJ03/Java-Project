package learning;

/**
 * TASK: Member 1 - Content Page Model
 *
 * PURPOSE:
 *   Represents a single educational screen in the Learning Module.
 *   Each page has a title, a body of text, and an optional image path
 *   (images are not used yet; the field is reserved for a future enhancement).
 *
 * OOP CONCEPTS TO DEMONSTRATE HERE:
 *   - Constructor overloading: provide TWO constructors -- one with just
 *     (title, body), and a second with (title, body, imagePath).
 *     The two-argument constructor should delegate to the three-argument one
 *     passing null for imagePath.
 *
 * THIS FILE IS COMPLETE -- no logic left to write.
 * You may add extra fields or methods if your implementation needs them.
 */
public class ContentPage {

    // === FIELDS ===
    private final String title;
    private final String body;
    private final String imagePath; // null when no image is associated

    // === CONSTRUCTOR OVERLOADING (OOP requirement) ===
    // Two-argument version: no image path provided
    public ContentPage(String title, String body) {
        this(title, body, null); // delegates to the full constructor below
    }

    // Three-argument version: full constructor
    public ContentPage(String title, String body, String imagePath) {
        this.title     = title;
        this.body      = body;
        this.imagePath = imagePath;
    }

    // === GETTERS ===
    public String getTitle()     { return title; }
    public String getBody()      { return body; }
    public String getImagePath() { return imagePath; } // returns null if no image
}
