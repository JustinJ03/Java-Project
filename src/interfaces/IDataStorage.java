// Starter file. Implement the // TODO blocks below.
package interfaces;

import java.util.List;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;

/** Storage contract. Implemented now by a text-file DataManager; swappable for a DB later. */
public interface IDataStorage {
    void saveScore(String user, int score) throws InvalidScoreException;
    List<String[]> readScores() throws DataNotFoundException;
}
