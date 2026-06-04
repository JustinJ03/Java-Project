package gamification;

/**
 * Badge.java
 * Represents a reward badge earned by the player in the EduSDG application.
 *
 * Badges are awarded when the player reaches point thresholds or completes
 * quiz milestones inside GamificationModule.
 *
 * OOP CONCEPTS DEMONSTRATED:
 *   - Constructor overloading: three constructors, each delegating upward
 *     to the full three-argument constructor.
 *
 * Creator  : Member 3
 */
public class Badge {

    // === FIELDS ===
    private final String name;
    private final String description;
    private final String icon;          // emoji shown in the GUI

    // === CONSTRUCTOR OVERLOADING ===

    /**
     * Two-arg: name + description — uses the default medal icon.
     * Useful when an emoji icon is not needed.
     */
    public Badge(String name, String description) {
        this(name, description, "🏅");
    }

    /** Three-arg: full constructor — name, description, and custom emoji icon. */
    public Badge(String name, String description, String icon) {
        this.name        = name;
        this.description = description;
        this.icon        = icon;
    }

    // === GETTERS ===
    public String getName()        { return name; }
    public String getDescription() { return description; }
    public String getIcon()        { return icon; }

    // === OVERRIDES ===

    /** Two badges are equal if they share the same name (case-insensitive).
     *  Used by hasBadge() in GamificationModule to prevent duplicates. */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Badge)) return false;
        return this.name.equalsIgnoreCase(((Badge) obj).name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return icon + " " + name + " — " + description;
    }
}

