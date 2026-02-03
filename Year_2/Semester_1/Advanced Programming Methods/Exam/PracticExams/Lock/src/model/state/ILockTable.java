package model.state;

import java.util.Map;

public interface ILockTable {
    int get(int key);
    void update(int key, int value);
    boolean containsKey(int key);
    int getFreeLocation();
    Map<Integer, Integer> getContent();
}

