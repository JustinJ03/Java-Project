// Starter file. Implement the // TODO blocks below.
package data;

/** Simple per-session user model. */
public class UserProfile {
    private final String name;
    private int lastScore;

    public UserProfile(String name) { this.name = name; }

    public String getName() { return name; }
    public int getLastScore() { return lastScore; }
    public void setLastScore(int lastScore) { this.lastScore = lastScore; }
}
