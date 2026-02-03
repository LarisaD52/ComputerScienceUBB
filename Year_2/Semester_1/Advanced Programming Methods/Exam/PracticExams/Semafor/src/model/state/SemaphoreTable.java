package model.state;

import javafx.collections.FXCollections;
import javafx.util.Pair;
import view.GUI.SemaphoreTableRow;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreTable implements ISemaphoreTable {

    private Map<Integer, Pair<Integer, List<Integer>>> table;
    private int freeLocation;
    private final Lock lock;

    public SemaphoreTable() {
        table = new HashMap<>();
        freeLocation = 1;
        lock = new ReentrantLock();
    }

    @Override
    public int allocate(Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            table.put(freeLocation, value);
            return freeLocation++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> read(int address) {
        lock.lock();
        try {
            return table.get(address);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void write(int address, Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            table.put(address, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(int address) {
        lock.lock();
        try {
            return table.containsKey(address);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        return table;
    }

    @Override
    public void setContent(Map<Integer, Pair<Integer, List<Integer>>> map) {
        lock.lock();
        try {
            table = map;
        } finally {
            lock.unlock();
        }
    }
}
