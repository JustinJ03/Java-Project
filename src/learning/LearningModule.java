package learning;

import app.AppContext;
import app.AppModule;
import data.exceptions.ContentLoadException;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TASK: Member 1 - Learning Module
 *
 * PURPOSE:
 *   Displays 10+ educational screens about SDG 4 - Quality Education.
 *   Users move between screens with Prev / Next buttons.
 *   Content is loaded from resources/learning/content.txt at runtime.
 *
 * EXTENDS: AppModule  -- gives you getName(), onShow(), and the placeholder() helper.
 * IMPLEMENTS: IModule  -- via AppModule. You MUST override getView().
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Inheritance       : LearningModule extends AppModule
 *   - Overriding        : getView() and onShow() override AppModule's versions
 *   - Abstraction       : AppModule is abstract; this class provides the concrete body
 *   - Constructor chaining via super()
 */
public class LearningModule extends AppModule {

    // === FIELDS (already declared -- do not remove) ===
    private final List<ContentPage> pages = new ArrayList<>();
    private int index = 0; // index of the currently displayed page

    // === CONSTRUCTOR ===
    public LearningModule(AppContext ctx) {
        super(ctx, "Learning"); // registers the module name used by Navigator
    }

    // =========================================================
    // METHOD: loadPages()
    // =========================================================
    /**
     * Reads content.txt and builds the list of ContentPage objects.
     * Call this inside getView() before accessing 'pages'.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Open "resources/learning/content.txt" with a BufferedReader:
     *             BufferedReader br = new BufferedReader(new FileReader("resources/learning/content.txt"));
     *
     *   STEP 2: Declare two working variables:
     *             String currentTitle = null;
     *             StringBuilder currentBody = new StringBuilder();
     *
     *   STEP 3: Loop with br.readLine(). For each line:
     *             a. If line equals "---"  -->  the block is complete.
     *                  Create: pages.add(new ContentPage(currentTitle, currentBody.toString().trim()));
     *                  Then reset: currentTitle = null; currentBody.setLength(0);
     *             b. Else if currentTitle == null  -->  this is the first line of a new block.
     *                  Set: currentTitle = line;
     *             c. Else  -->  body line.
     *                  Append: currentBody.append(line).append("\n");
     *
     *   STEP 4: After the loop, add the FINAL block (the last screen has no "---" after it):
     *             if (currentTitle != null) pages.add(new ContentPage(currentTitle, currentBody.toString().trim()));
     *
     *   STEP 5: Use try-with-resources so the file closes automatically:
     *             try (BufferedReader br = new BufferedReader(...)) { ... }
     *             catch (IOException e) { throw new ContentLoadException("Cannot load content", e); }
     *
     *   EXPECTED: pages.size() >= 10 when content.txt is present.
     */
    public List<ContentPage> loadPages() {
        // TODO(Member 1): implement using the steps above.
        return pages;
    }

    // =========================================================
    // METHOD: getView()  [OVERRIDE -- required by IModule]
    // =========================================================
    /**
     * Builds and returns the Learning Module's Swing panel.
     *
     * HOW TO IMPLEMENT:
     *   STEP 1: Load pages if not already done:
     *             if (pages.isEmpty()) loadPages();
     *             Guard against empty list: if (pages.isEmpty()) return placeholder("No content loaded.");
     *
     *   STEP 2: Create the outer panel:
     *             JPanel panel = new JPanel(new BorderLayout());
     *             panel.setBackground(Theme.BG);
     *
     *   STEP 3: Title label at NORTH:
     *             JLabel titleLabel = new JLabel(pages.get(index).getTitle(), SwingConstants.CENTER);
     *             titleLabel.setFont(Theme.TITLE_FONT);
     *             titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
     *             panel.add(titleLabel, BorderLayout.NORTH);
     *
     *   STEP 4: Body text area at CENTER (non-editable, word-wrapped):
     *             JTextArea body = new JTextArea(pages.get(index).getBody());
     *             body.setEditable(false);  body.setLineWrap(true);  body.setWrapStyleWord(true);
     *             body.setFont(Theme.BODY_FONT);  body.setBackground(Theme.BG);
     *             panel.add(new JScrollPane(body), BorderLayout.CENTER);
     *
     *   STEP 5: Navigation buttons at SOUTH:
     *             JPanel nav = new JPanel(new FlowLayout());
     *             JButton prev = new JButton("< Prev");
     *             JButton next = new JButton("Next >");
     *             // Prev listener: index = Math.max(0, index - 1); refresh titleLabel + body
     *             // Next listener: index = Math.min(pages.size()-1, index+1); refresh titleLabel + body
     *             // Refresh means: titleLabel.setText(pages.get(index).getTitle());
     *             //                body.setText(pages.get(index).getBody());
     *             nav.add(prev); nav.add(next);
     *             panel.add(nav, BorderLayout.SOUTH);
     *
     *   STEP 6: return panel;
     *
     * OOP NOTE: This override of AppModule.getView() demonstrates method overriding.
     *           The Navigator calls getView() on any IModule -- polymorphism.
     */
    @Override
    public JComponent getView() {
        // TODO(Member 1): implement using the steps above.
        return placeholder("Learning: implement getView() -- see comments above.");
    }

    // =========================================================
    // METHOD: onShow()  [OVERRIDE -- optional but good practice]
    // =========================================================
    /**
     * Called by Navigator each time the user navigates to this module.
     *
     * HOW TO IMPLEMENT:
     *   Optionally reset to the first page:
     *     index = 0;
     *   Or refresh the currently displayed title/body labels
     *   if you hold references to them as fields.
     *
     * OOP NOTE: Overrides AppModule.onShow() -- demonstrates overriding.
     */
    @Override
    public void onShow() {
        // TODO(Member 1): optionally reset index = 0 and refresh the view.
    }
}
