// SUGGESTION (AI-generated reference). Rename to ContentPage.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 1
package learning;

/** One learning screen. Overloaded constructor leaves room for an image path (unused for now). */
public class ContentPage {
    private final String title;
    private final String body;
    private final String imagePath; // optional; no images for now

    public ContentPage(String title, String body) {
        this(title, body, null);
    }

    public ContentPage(String title, String body, String imagePath) {
        this.title = title;
        this.body = body;
        this.imagePath = imagePath;
    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getImagePath() { return imagePath; }
}
