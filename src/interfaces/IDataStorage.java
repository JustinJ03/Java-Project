package interfaces;

import java.util.List;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;

/**
 * Storage contract for score persistence.
 *
 * Implemented by: DataManager (Member 4).
 * Currently backed by a text file; can be swapped to a database
 * by providing a new implementation without changing any module code.
 */
public interface IDataStorage {

    /**
     * Persists a score entry.
     *
     * @param user  player name
     * @param score percentage score (must be 0-100)
     * @throws InvalidScoreException if score is outside 0-100
     */
    void saveScore(String user, int score) throws InvalidScoreException;

    /**
     * Returns all saved score rows.
     * Each row is a String array: [name, score, timestamp].
     * Returns an empty list if no scores have been saved yet.
     *
     * @throws DataNotFoundException if the file exists but cannot be read
     */
    List<String[]> readScores() throws DataNotFoundException;
}
