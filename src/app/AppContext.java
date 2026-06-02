package app;

import interfaces.IDataStorage;
import data.DataManager;

/** Holds shared services so modules don't construct their own. DB-swap point is here. */
public class AppContext {
    private final Navigator navigator = new Navigator();
    private final IDataStorage storage = new DataManager("data/scores.txt");

    public Navigator navigator() { return navigator; }
    public IDataStorage storage() { return storage; }
}
