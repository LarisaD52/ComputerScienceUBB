package model.state;

import java.util.Map;

public interface ILatchTable {

    int add(int value);
    void update(int location, int value);
    int get(int location);
    boolean containsKey(int location);

    Map<Integer, Integer> getContent();
    void setContent(Map<Integer, Integer> newContent);
}
