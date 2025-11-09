package model.state;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements IFileTable<String, BufferedReader> {

    private final Map<String, BufferedReader> table;

    public MapFileTable() {
        table = new HashMap<>();
    }

    @Override
    public void addOpenFile(String key, BufferedReader value) {
        table.put(key, value);
    }

    @Override
    public BufferedReader get(String key) {
        return table.get(key);
    }

    @Override
    public void remove(String key) {
        table.remove(key);
    }


    @Override
    public boolean isOpen(String key) {
        return table.containsKey(key);
    }

    @Override
    public String toString() {
        return table.keySet().toString();
    }
}
