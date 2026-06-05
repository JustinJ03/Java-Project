package gamification;

/**
 * Define reward badge awarded to the player. The two-arg constructor delegates
 * to the three-arg constructor (constructor overloading). equals() and hashCode()
 * compare by name so GamificationModule can detect duplicates.
 *
 * Creator  : Dhiaurrahman Bin Muhammad Dino (102521)
 * Tester/Reviewer : Justin Joel anak Juni (104607)
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

    // name-based equality lets hasBadge() prevent the same badge being awarded twice.
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

