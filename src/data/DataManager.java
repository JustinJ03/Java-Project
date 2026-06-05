package data;

import interfaces.IDataStorage;
import data.exceptions.DataNotFoundException;
import data.exceptions.InvalidScoreException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Text-file implementation of IDataStorage.
 *
 * Scores are persisted to a CSV file: name,score,timestamp (ms since epoch).
 * No scores are ever hardcoded -- all reads and writes go through this class.
 *
 * Demonstrates:
 *   - Method overloading  : saveScore(String,int) and saveScore(UserProfile)
 *   - Custom exceptions   : InvalidScoreException, DataNotFoundException
 *   - Interface implementation : implements IDataStorage
 *
 * Member 4 -- this file is complete.
 */
public class DataManager implements IDataStorage {

    private final String filePath;

    public DataManager(String filePath) {
        this.filePath = filePath;
    }

    // =========================================================
    // saveScore(String user, int score)  -- primary overload
    // =========================================================
    /**
     * Validates the score and appends one CSV line to the scores file.
     *
     * @throws InvalidScoreException if score is outside 0-100
     */
    @Override
    public void saveScore(String user, int score) throws InvalidScoreException {
        if (score < 0 || score > 100) {
            throw new InvalidScoreException(
                "Score must be between 0 and 100, got: " + score);
        }
        // create data/ directory on first run if it does not yet exist.
        new File("data").mkdirs();
        // open in append mode so existing scores are not overwritten.
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(user + "," + score + "," + System.currentTimeMillis());
            bw.newLine();
        // log but do not crash — a failed write should not end the quiz session.
        } catch (IOException e) {
            System.err.println("Warning: could not save score: " + e.getMessage());
        }
    }

    // =========================================================
    // saveScore(UserProfile)  -- overloaded convenience method
    // =========================================================
    /**
     * Convenience overload: saves the score stored in a UserProfile.
     * Delegates to saveScore(String, int) above.
     *
     * @throws InvalidScoreException forwarded from the primary overload
     */
    public void saveScore(UserProfile profile) throws InvalidScoreException {
        saveScore(profile.getName(), profile.getLastScore());
    }

    // =========================================================
    // readScores()
    // =========================================================
    /**
     * Reads all rows from the scores file.
     *
     * Returns an empty list (not an exception) if the file does not
     * exist yet -- this is normal before any quiz has been completed.
     *
     * @return list of String arrays: each row is [name, score, timestamp]
     * @throws DataNotFoundException if the file exists but cannot be read
     */
    @Override
    public List<String[]> readScores() throws DataNotFoundException {
        List<String[]> result = new ArrayList<>();
        File f = new File(filePath);
        // scores file does not exist yet (no quiz has been completed) — return empty list, not an error.
        if (!f.exists()) {
            return result;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // each CSV line is: name,score,epochMilliseconds
                    result.add(line.split(","));
                }
            }
        } catch (IOException e) {
            throw new DataNotFoundException(
                "Could not read scores from " + filePath, e);
        }
        return result;
    }
}
