package model.state;

import javafx.util.Pair;
import java.util.List;
import java.util.Map;

public interface ISemaphoreTable {
    int allocate(Pair<Integer, List<Integer>> value);
    Pair<Integer, List<Integer>> read(int address);
    void write(int address, Pair<Integer, List<Integer>> value);
    boolean containsKey(int address);
    Map<Integer, Pair<Integer, List<Integer>>> getContent();
    void setContent(Map<Integer, Pair<Integer, List<Integer>>> map);
}
