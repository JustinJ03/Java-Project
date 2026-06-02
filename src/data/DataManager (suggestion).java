// SUGGESTION (AI-generated reference). Rename to DataManager.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 4
package data;

import interfaces.IDataStorage;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Text-file score storage. No hardcoding: scores are read/written to scores.txt.
 * Demonstrates method overloading (two saveScore signatures) and custom exceptions.
 * Line format: name,score,timestamp
 */
public class DataManager implements IDataStorage {
    private final String filePath;

    public DataManager(String filePath) { this.filePath = filePath; }

    @Override
    public void saveScore(String user, int score) throws InvalidScoreException {
        // TODO(Member 4): if score < 0 || score > 100 throw new InvalidScoreException(...).
        //   Otherwise append "user,score," + System.currentTimeMillis() as a new line to
        //   filePath using try-with-resources (new FileWriter(filePath, true) wrapped in
        //   BufferedWriter). Create the data/ folder if missing.
    }

    /** Overloaded convenience: save straight from a UserProfile. */
    public void saveScore(UserProfile profile) throws InvalidScoreException {
        // TODO(Member 4): delegate -> saveScore(profile.getName(), profile.getLastScore()).
    }

    @Override
    public List<String[]> readScores() throws DataNotFoundException {
        // TODO(Member 4): read every line of filePath, split on ",", add as String[] to a list.
        //   Return an empty list if the file does not exist yet.
        //   Wrap any IOException in a new DataNotFoundException(message, cause).
        return new ArrayList<>();
    }
}
