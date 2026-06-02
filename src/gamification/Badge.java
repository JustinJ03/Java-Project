package gamification;

/**
 * TASK: Member 3 - Badge Model
 *
 * PURPOSE:
 *   Represents a reward badge earned by the player.
 *   Badges are awarded when the player reaches point thresholds.
 *
 * OOP CONCEPTS TO DEMONSTRATE:
 *   - Constructor overloading: two constructors -- Badge(name) and
 *     Badge(name, description). The one-argument version delegates
 *     to the two-argument one with an empty description string.
 *
 * THIS FILE IS COMPLETE -- no logic left to write.
 * You may add extra fields (e.g. an icon path) if your UI needs them.
 */
public class Badge {

    // === FIELDS ===
    private final String name;
    private final String description;

    // === CONSTRUCTOR OVERLOADING (OOP requirement) ===
    // One-argument version: no description provided
    public Badge(String name) {
        this(name, ""); // delegates to full constructor
    }

    // Two-argument version: full constructor
    public Badge(String name, String description) {
        this.name        = name;
        this.description = description;
    }

    // === GETTERS ===
    public String getName()        { return name; }
    public String getDescription() { return description; }
}
