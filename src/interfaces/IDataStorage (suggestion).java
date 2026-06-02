// SUGGESTION (AI-generated reference). Rename to IDataStorage.java and merge into your own work.
// Do not overwrite code you have already written.  Owner: Member 4
package interfaces;

import java.util.List;
import data.exceptions.InvalidScoreException;
import data.exceptions.DataNotFoundException;

/** Storage contract. Implemented now by a text-file DataManager; swappable for a DB later. */
public interface IDataStorage {
    void saveScore(String user, int score) throws InvalidScoreException;
    List<String[]> readScores() throws DataNotFoundException;
}
