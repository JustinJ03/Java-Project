package interfaces;

import java.util.List;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;

/**
 * Storage contract that decouples modules from the persistence mechanism.
 * Any class implementing this can be swapped in (text file, database, etc.)
 * without changing the calling code. Implemented by DataManager.
 */
public interface IDataStorage {

    /**
     * Persists a score entry for the given player.
     *
     * @param user  player name
     * @param score percentage score (must be 0-100)
     * @throws InvalidScoreException if score is outside the valid 0-100 range
     */
    void saveScore(String user, int score) throws InvalidScoreException;

    /**
     * Returns all saved score rows from the backing store.
     * Each row is a String array: [name, score, timestamp].
     * Returns an empty list if no scores have been saved yet.
     *
     * @return list of score rows; never null
     * @throws DataNotFoundException if the file exists but cannot be read
     */
    List<String[]> readScores() throws DataNotFoundException;
}
