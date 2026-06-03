package learning;
import java.util.ArrayList;
import java.util.List;

/**
 * LearningModule.java
 * Core class for the educational content module in the SDG4 Quality Education app.
 *
 * Creator  : Adriana Umang Anak Nawang (103088)
 * Tester   :
 *
 * Implements IModule to manage a list of ContentPage objects.
 * Pre-loads 10 pages of educational content about SDG 4 (Quality Education).
 * Tracks the user's current page and overall progress.
 *
 * This module interacts with:
 *   - QuizModule  : quiz is unlocked when this module is completed
 *   - GamificationModule : awards a badge when progress hits 100%
 *   - DataManager : progress is persisted via IDataStorage
 */
public class LearningModule implements IModule {

    // ── Attributes ──────────────────────────────────────────────────────────
    private String           moduleTitle;    // Display name of this module
    private List<ContentPage> pages;         // Ordered list of content pages
    private int              currentPage;    // 1-based index of the current page
    private String           moduleTopic;    // SDG topic (e.g. "Quality Education")
    private boolean          finishedByUser; // true after the user taps Finish on the last page

    // ── Constructor ──────────────────────────────────────────────────────────

    /**
     * Creates a LearningModule and immediately loads all 10 content pages.
     *
     * @param moduleTitle  the display title shown in the GUI header
     * @param moduleTopic  the SDG topic name
     */
    public LearningModule(String moduleTitle, String moduleTopic) {
        this.moduleTitle  = moduleTitle;        // <-- must be set by caller, not auto-generated
        this.moduleTopic  = moduleTopic;        // <-- must be set by caller, not auto-generated
        this.pages        = new ArrayList<>();      // <-- initialized as empty list, will be populated by loadContent()
        this.currentPage  = 1;      // <-- starts at page 1
        loadContent();      // <-- loadContent() is called from the constructor to pre-populate the pages list with 10 ContentPage objects about SDG 4 Quality Education
    }

    // ── IModule Implementation ────────────────────────────────────────────────

    /**
     * Pre-loads 10 educational content pages about SDG 4 Quality Education.
     * Called automatically from the constructor.
     */
    @Override
    public void loadContent() {
        pages.clear();

        pages.add(new ContentPage(1,
            "Lesson 1: Understanding SDG 4 - Quality Education",
            "SDG 4 is about ensuring inclusive and equitable quality education and promoting lifelong " +
            "learning opportunities for all. Education is more than attending school; it means effective " +
            "teaching, safe environments, and opportunities that nurture creativity and critical thinking.",
            "images/sdg4_intro.png",
            "Key Fact: Education is a fundamental human right recognized by the United Nations."
        ));

        pages.add(new ContentPage(2,
            "Lesson 2: Why Quality Education Matters",
            "Education empowers individuals to break the cycle of poverty, improve health, and contribute " +
            "positively to their communities. It builds knowledge, confidence, and skills needed for " +
            "employment and innovation. When education is inclusive, societies become more stable and " +
            "prosperous.",
            "images/literacy_map.png",
            "Key Fact: Each additional year of schooling can increase a person's income by up to 10 percent."
        ));

        pages.add(new ContentPage(3,
            "Lesson 3: Barriers to Education",
            "Many children and young people cannot access or complete schooling. Barriers include " +
            "poverty, child labour, gender inequality, conflict and displacement, disability without adequate " +
            "support, and schools that are too far away or unsafe. Removing these barriers is essential to " +
            "achieving SDG 4.",
            "images/barriers.png",
            "Key Fact: UNICEF estimates that 244 million children and youth are out of school worldwide."
        ));

        pages.add(new ContentPage(4,
            "Lesson 4: The Role of Teachers",
            "Teachers are the heart of education. Skilled and motivated educators create inclusive " +
            "classrooms and inspire students to learn. Investing in teacher training, fair pay, and " +
            "professional development ensures that every learner receives quality instruction.",
            "images/inclusive.png",
            "Key Fact: SDG 4 aims to substantially increase the supply of qualified teachers, especially in developing countries."
        ));

        pages.add(new ContentPage(5,
            "Lesson 5: Technology and Digital Learning",
            "Digital tools make education more accessible and interactive. Online platforms, educational " +
            "apps, and virtual classrooms allow students to learn anytime and anywhere. Technology also " +
            "helps bridge gaps for learners in remote areas and those with disabilities.",
            "images/ece.png",
            "Key Fact: During the COVID-19 pandemic, e-learning platforms reached over one billion users globally."
        ));

        pages.add(new ContentPage(6,
            "Lesson 6: Gender Equality in Education",
            "Gender inequality remains a major barrier to education. In some regions, girls face cultural " +
            "restrictions, early marriage, or unsafe school environments. Promoting gender equality ensures " +
            "that both girls and boys have equal opportunities to learn and succeed.",
            "images/teachers.png",
            "Key Fact: Educated women are more likely to participate in decision-making and improve community well-being."
        ));

        pages.add(new ContentPage(7,
            "Lesson 7: Inclusive Education for Learners with Disabilities",
            "Inclusive education means adapting teaching methods, materials, and environments so that all " +
            "learners, including those with disabilities, can participate fully. Accessibility features such as " +
            "ramps, assistive technology, and supportive teachers make learning possible for everyone.",
            "images/digital.png",
            "Key Fact: SDG 4 emphasizes equal access to education for persons with disabilities and those in vulnerable situations."
        ));

        pages.add(new ContentPage(8,
            "Lesson 8: Lifelong Learning Opportunities",
            "Learning does not end after school. Lifelong learning helps people adapt to changes in " +
            "technology, work, and society. It includes vocational training, adult education, and community " +
            "workshops that keep skills relevant throughout life.",
            "images/financing.png",
            "Key Fact: Continuous learning supports innovation and sustainable development."
        ));

        pages.add(new ContentPage(9,
            "Lesson 9: Global Partnerships for Education",
            "Achieving SDG 4 requires collaboration among governments, NGOs, educators, and " +
            "communities. Partnerships help fund schools, train teachers, and share best practices across " +
            "countries. Working together ensures that no learner is left behind.",
            "images/malaysia.png",
            "Key Fact: The Global Partnership for Education supports more than 80 countries in improving access and quality of education."
        ));

        pages.add(new ContentPage(10,
            "Lesson 10: The Future of Education",
            "The future of education focuses on creativity, critical thinking, and sustainability. Schools must " +
            "prepare students to solve global challenges such as climate change and inequality. Innovative " +
            "teaching methods and technology will make learning more personalized and inclusive.",
            "images/action.png",
            "Key Fact: By 2030, SDG 4 aims for universal access to free, equitable, and quality primary and secondary education for all children."
        ));
    }

    /**
     * Display the content of a given page to the console.
     * In the full GUI, the LearningModulePanel calls getPage() instead.
     *
     * @param pageNumber 1-based page index
     */
    @Override
    public void displayPage(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.size()) {
            System.out.println("Invalid page number: " + pageNumber);       // <-- added error handling for invalid page numbers
            return;     // <-- early return if page number is out of range
        }
        ContentPage page = pages.get(pageNumber - 1);       // <-- retrieve the ContentPage object for the requested page number (adjusting for 0-based index)
        page.markViewed();      // <-- mark the page as viewed when displayed
        currentPage = pageNumber;       // <-- update the currentPage attribute to reflect the newly displayed page
        System.out.println(page.getFormattedContent());     // <-- print the formatted content of the page to the console (in the GUI, this would be rendered in the panel instead)
        System.out.println("Page " + currentPage + " of " + getTotalPages());       // <-- print the page number and total pages to the console
    }

    /** Move to the next page if not already at the last page. */
    @Override
    public void nextPage() {
        if (currentPage < pages.size()) {
            displayPage(currentPage + 1);       // <-- simply call displayPage() with the next page number, which will handle marking it as viewed and updating currentPage
        } else {
            System.out.println("You have reached the last page of this module.");
        }
    }

    /** Move to the previous page if not already at the first page. */
    @Override
    public void prevPage() {
        if (currentPage > 1) {
            displayPage(currentPage - 1);       // <-- simply call displayPage() with the previous page number, which will handle marking it as viewed and updating currentPage
        } else {
            System.out.println("You are already on the first page.");
        }
    }

    @Override
    public int getTotalPages() {
        return pages.size();        //<-- return the total number of pages in this module by returning the size of the pages list
    }

    @Override
    public int getCurrentPage() {
        return currentPage;     //<-- return the current page number
    }

    /**
     * Calculate progress as a percentage of pages viewed.
     * @return value from 0.0 to 100.0
     */
    @Override
    public double getProgress() {
        if (pages.isEmpty()) return 0.0;        //<-- avoid division by zero if there are no pages
        long viewed = pages.stream().filter(ContentPage::isViewed).count();     //<-- count how many pages have been marked as viewed using a stream and filter
        return (viewed * 100.0) / pages.size();     //<-- calculate the percentage of pages viewed by dividing the count of viewed pages by the total number of pages and multiplying by 100
    }

    /**
     * Module is complete only after the user confirms with Finish on the last page.
     * @return true if the user has finished the module
     */
    @Override
    public boolean isCompleted() {
        return finishedByUser;      //<-- return the value of finishedByUser, which is set to true when the user taps Finish on the last page
    }

    /** Called from the GUI when the user taps Finish on the last page. */
    public void markFinished() {
        finishedByUser = true;      //<-- set finishedByUser to true to indicate that the user has completed the module
        ContentPage last = getPage(getTotalPages());        // <-- retrieve the last ContentPage object using getPage() with the total number of pages as the argument
        if (last != null) {
            last.markViewed();      // <-- ensure the last page is marked as viewed when the user finishes, in case they tapped Finish without navigating to the last page
        }
    }

    public boolean hasFinished() {
        return finishedByUser;      // <-- return the value of finishedByUser to indicate whether the user has finished the module
    }

    /** Reset the module — mark all pages as unread and return to page 1. */
    @Override
    public void reset() {
        pages.clear();      //<-- clear the existing pages list to remove all current ContentPage objects
        currentPage = 1;        //<-- reset currentPage back to 1
        finishedByUser = false;     // <-- reset finishedByUser back to false
        loadContent();      //<-- call loadContent() to re-populate the pages list with the original 10 ContentPage objects, effectively resetting the module to its initial state
        System.out.println("Module reset. Starting from page 1.");      // <-- print a message to the console indicating that the module has been reset and the user is back at page 1
    }

    // ── Additional Methods ────────────────────────────────────────────────────

    /**
     * Retrieve a specific ContentPage object by 1-based index.
     * Used by the GUI panel to build the display.
     *
     * @param pageNumber 1-based index
     * @return the ContentPage, or null if out of range
     */
    public ContentPage getPage(int pageNumber) {
        if (pageNumber < 1 || pageNumber > pages.size()) return null;       // <-- added error handling for invalid page numbers, returning null if the requested page number is out of range
        return pages.get(pageNumber - 1);       // <-- return the ContentPage object from the pages list, adjusting for 0-based index by subtracting 1 from the pageNumber argument
    }

    /**
     * Returns the current ContentPage object.
     * Convenience method for GUI navigation.
     *
     * @return current ContentPage
     */
    public ContentPage getCurrentContentPage() {
        return getPage(currentPage);        // <-- simply call getPage() with the currentPage attribute to retrieve the ContentPage object for the currently displayed page
    }

    /**
     * Returns the list of all content pages.
     * Used by the GUI to populate the module list panel.
     *
     * @return unmodifiable view of the pages list
     */
    public List<ContentPage> getAllPages() {
        return java.util.Collections.unmodifiableList(pages);       // <-- return an unmodifiable view of the pages list to prevent external modification of the internal state of the LearningModule
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getModuleTitle()  { return moduleTitle; }
    public String getModuleTopic()  { return moduleTopic; }

    @Override
    public String toString() {
        return String.format("LearningModule{title='%s', pages=%d, progress=%.1f%%}",
                moduleTitle, pages.size(), getProgress());      // <-- toString() provides a concise summary of the module, including its title, number of pages, and current progress percentage (but not the full content of the pages)
    }
}
