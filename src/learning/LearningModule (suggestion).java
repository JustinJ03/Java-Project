// SUGGESTION (AI-generated reference). Rename to LearningModule.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 1
package learning;

import app.AppContext;
import app.AppModule;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.List;

/** Learning module: cycles through >=10 content screens with Prev/Next. */
public class LearningModule extends AppModule {
    private final List<ContentPage> pages = new ArrayList<>();
    private int index = 0;

    public LearningModule(AppContext ctx) { super(ctx, "Learning"); }

    public List<ContentPage> loadPages() {
        // TODO(Member 1): read resources/learning/content.txt - blocks separated by a line
        //   containing only "---"; first line of a block = title, remaining lines = body.
        //   Build a ContentPage per block, add to 'pages', return it (>=10 expected).
        return pages;
    }

    @Override
    public JComponent getView() {
        // TODO(Member 1): build a panel that shows pages.get(index) (title + body) with
        //   Prev/Next buttons that change 'index' and refresh the displayed text.
        return placeholder("learning: 10+ content screens with Prev/Next");
    }
}
