package learning;
import java.util.ArrayList;
import java.util.List;

/**
 * LearningModule.java
 * Core data-and-logic class for the Learning tab.
 *
 * Implements IModule to manage an ordered list of ContentPage objects.
 * Pre-loads 10 pages of SDG 4 content at construction time.
 * Tracks the current page and the user's overall progress percentage.
 *
 * Creator  : Adriana Umang Anak Nawang (103088)
 */
public class LearningModule implements IModule {

    // ── Fields ───────────────────────────────────────────────────────────────
    private String           moduleTitle;    // Display name shown in the header
    private List<ContentPage> pages;         // Ordered list of all content pages
    private int              currentPage;    // 1-based index of the active page
    private String           moduleTopic;    // SDG topic label (e.g. "SDG 4")
    private boolean          finishedByUser; // true after the user taps Finish

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Creates the module and immediately loads all 10 content pages.
     *
     * @param moduleTitle display title shown in the GUI header
     * @param moduleTopic SDG topic label
     */
    public LearningModule(String moduleTitle, String moduleTopic) {
        this.moduleTitle  = moduleTitle;
        this.moduleTopic  = moduleTopic;
        this.pages        = new ArrayList<>();
        this.currentPage  = 1;
        loadContent();
    }

    // ── IModule implementation ────────────────────────────────────────────────

    /**
     * Pre-loads 10 educational content pages about SDG 4 Quality Education.
     * Called automatically from the constructor and again on reset().
     */
    @Override
    public void loadContent() {
        // Content is hardcoded here (no external file); each page has title, body, image path, and key fact.
        pages.clear();

        pages.add(new ContentPage(1,
            "Lesson 1: Understanding SDG 4 - Quality Education",
            "SDG 4 is about ensuring inclusive and equitable quality education and promoting lifelong " +
            "learning opportunities for all. Education is more than attending school; it means effective " +
            "teaching, safe environments, and opportunities that nurture creativity and critical thinking.",
            "resources/learning/Images/sdg4_intro.png",
            "Key Fact: Education is a fundamental human right recognized by the United Nations."
        ));

        pages.add(new ContentPage(2,
            "Lesson 2: Why Quality Education Matters",
            "Education empowers individuals to break the cycle of poverty, improve health, and contribute " +
            "positively to their communities. It builds knowledge, confidence, and skills needed for " +
            "employment and innovation. When education is inclusive, societies become more stable and " +
            "prosperous.",
            "resources/learning/Images/literacy_map.png",
            "Key Fact: Each additional year of schooling can increase a person's income by up to 10 percent."
        ));

        pages.add(new ContentPage(3,
            "Lesson 3: Barriers to Education",
            "Many children and young people cannot access or complete schooling. Barriers include " +
            "poverty, child labour, gender inequality, conflict and displacement, disability without adequate " +
            "support, and schools that are too far away or unsafe. Removing these barriers is essential to " +
            "achieving SDG 4.",
            "resources/learning/Images/barriers.png",
            "Key Fact: UNICEF estimates that 244 million children and youth are out of school worldwide."
        ));

        pages.add(new ContentPage(4,
            "Lesson 4: The Role of Teachers",
            "Teachers are the heart of education. Skilled and motivated educators create inclusive " +
            "classrooms and inspire students to learn. Investing in teacher training, fair pay, and " +
            "professional development ensures that every learner receives quality instruction.",
            "resources/learning/Images/inclusive.png",
            "Key Fact: SDG 4 aims to substantially increase the supply of qualified teachers, especially in developing countries."
        ));

        pages.add(new ContentPage(5,
            "Lesson 5: Technology and Digital Learning",
            "Digital tools make education more accessible and interactive. Online platforms, educational " +
            "apps, and virtual classrooms allow students to learn anytime and anywhere. Technology also " +
            "helps bridge gaps for learners in remote areas and those with disabilities.",
            "resources/learning/Images/ece.png",
            "Key Fact: During the COVID-19 pandemic, e-learning platforms reached over one billion users globally."
        ));

        pages.add(new ContentPage(6,
            "Lesson 6: Gender Equality in Education",
            "Gender inequality remains a major barrier to education. In some regions, girls face cultural " +
            "restrictions, early marriage, or unsafe school environments. Promoting gender equality ensures " +
            "that both girls and boys have equal opportunities to learn and succeed.",
            "resources/learning/Images/teachers.png",
            "Key Fact: Educated women are more likely to participate in decision-making and improve community well-being."
        ));

        pages.add(new ContentPage(7,
            "Lesson 7: Inclusive Education for Learners with Disabilities",
            "Inclusive education means adapting teaching methods, materials, and environments so that all " +
            "learners, including those with disabilities, can participate fully. Accessibility features such as " +
            "ramps, assistive technology, and supportive teachers make learning possible for everyone.",
            "resources/learning/Images/digital.png",
            "Key Fact: SDG 4 emphasizes equal access to education for persons with disabilities and those in vulnerable situations."
        ));

        pages.add(new ContentPage(8,
            "Lesson 8: Lifelong Learning Opportunities",
            "Learning does not end after school. Lifelong learning helps people adapt to changes in " +
            "technology, work, and society. It includes vocational training, adult education, and community " +
            "workshops that keep skills relevant throughout life.",
            "resources/learning/Images/financing.png",
            "Key Fact: Continuous learning supports innovation and sustainable development."
        ));

        pages.add(new ContentPage(9,
            "Lesson 9: Global Partnerships for Education",
            "Achieving SDG 4 requires collaboration among governments, NGOs, educators, and " +
            "communities. Partnerships help fund schools, train teachers, and share best practices across " +
            "countries. Working together ensures that no learner is left behind.",
            "resources/learning/Images/malaysia.png",
            "Key Fact: The Global Partnership for Education supports more than 80 countries in improving access and quality of education."
        ));

        pages.add(new ContentPage(10,
            "Lesson 10: The Future of Education",
            "The future of education focuses on creativity, critical thinking, and sustainability. Schools must " +
            "prepare students to solve global challenges such as climate change and inequality. Innovative " +
            "teaching methods and technology will make learning more personalized and inclusive.",
            "resources/learning/Images/action.png",
            "Key Fact: By 2030, SDG 4 aims for universal access to free, equitable, and quality primary and secondary education for all children."
        ));
    }

    /**
     * Marks the page as viewed, updates currentPage, and prints a summary to
     * the console. The GUI uses getPage() directly — this satisfies the IModule
     * contract and supports console testing.
     *
     * @param pageNumber 1-based page index
     */
    // This method serves the console/IModule contract; the GUI uses getCurrentContentPage() + refresh() instead.
    @Override
    public void displayPage(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.size()) {
            System.out.println("Invalid page number: " + pageNumber);
            return;
        }
        ContentPage page = pages.get(pageNumber - 1);
        page.markViewed();
        currentPage = pageNumber;
    }

    /** Advances to the next page; prints a message if already on the last page. */
    @Override
    public void nextPage() {
        if (currentPage < pages.size()) {
            displayPage(currentPage + 1);
        } else {
            System.out.println("You have reached the last page of this module.");
        }
    }

    /** Goes back to the previous page; prints a message if already on the first page. */
    @Override
    public void prevPage() {
        if (currentPage > 1) {
            displayPage(currentPage - 1);
        } else {
            System.out.println("You are already on the first page.");
        }
    }

    @Override
    public int getTotalPages() { return pages.size(); }

    @Override
    public int getCurrentPage() { return currentPage; }

    /**
     * Returns the percentage of pages the user has viewed (0.0 – 100.0).
     * A page counts as viewed once the user navigates to it.
     */
    @Override
    public double getProgress() {
        if (pages.isEmpty()) return 0.0;
        long viewed = pages.stream().filter(ContentPage::isViewed).count(); // stream filter counts only pages flagged as viewed
        return (viewed * 100.0) / pages.size();
    }

    /**
     * Returns true only after the user explicitly taps Finish on the last page.
     * Viewing all pages does not automatically count as completed.
     */
    @Override
    public boolean isCompleted() { return finishedByUser; }

    /**
     * Called by LearningModulePanel when the user taps Finish on the last page.
     * Marks the module complete and ensures the last page is recorded as viewed.
     * The last page is explicitly marked viewed here because the user may tap Finish
     * without having reached it via nextPage() (e.g. direct jump or keyboard shortcut).
     */
    public void markFinished() {
        finishedByUser = true;
        ContentPage last = getPage(getTotalPages());
        if (last != null) last.markViewed();
    }

    /**
     * Resets the module back to page 1, clearing all viewed flags and
     * the finished state. Reloads content from scratch.
     */
    @Override
    public void reset() {
        pages.clear();
        currentPage    = 1;
        finishedByUser = false;
        loadContent();
        System.out.println("Module reset. Starting from page 1.");
    }

    // ── Additional methods used by the GUI panel ──────────────────────────────

    /**
     * Returns the ContentPage at the given 1-based index.
     * Used by LearningModulePanel to build the display for each page.
     *
     * @param pageNumber 1-based index
     * @return the ContentPage, or null if the index is out of range
     */
    public ContentPage getPage(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.size()) return null;
        return pages.get(pageNumber - 1);
    }

    /**
     * Returns the ContentPage that is currently on screen.
     * Convenience method so the panel does not need to call getPage(getCurrentPage()).
     */
    public ContentPage getCurrentContentPage() {
        return getPage(currentPage);
    }

    /**
     * Returns an unmodifiable view of all pages.
     * Used by the GUI to populate the module list panel.
     */
    public List<ContentPage> getAllPages() {
        return java.util.Collections.unmodifiableList(pages);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getModuleTitle() { return moduleTitle; }
    public String getModuleTopic() { return moduleTopic; }

    @Override
    public String toString() {
        return String.format("LearningModule{title='%s', pages=%d, progress=%.1f%%}",
                moduleTitle, pages.size(), getProgress());
    }
}
