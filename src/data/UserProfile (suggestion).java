// SUGGESTION (AI-generated reference). Rename to UserProfile.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 4
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
