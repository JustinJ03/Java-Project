// Starter file. Implement the // TODO blocks below.
package gamification;

/** A reward badge. Overloaded constructors (name only, or name + description). */
public class Badge {
    private final String name;
    private final String description;

    public Badge(String name) { this(name, ""); }

    public Badge(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
}
