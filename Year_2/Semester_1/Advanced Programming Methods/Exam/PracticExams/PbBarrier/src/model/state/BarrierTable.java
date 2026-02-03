package model.state;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BarrierTable implements IBarrierTable {

    private final Map<Integer, Pair<Integer, List<Integer>>> table;
    private int freeLocation;
    private final Lock lock;

    public BarrierTable() {
        this.table = new HashMap<>();
        this.freeLocation = 1;
        this.lock = new ReentrantLock();
    }

    @Override
    public int getFreeLocation() {
        lock.lock();
        try {
            return freeLocation++;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(int key, Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            table.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Pair<Integer, List<Integer>> get(int key) {
        lock.lock();
        try {
            return table.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(int key) {
        lock.lock();
        try {
            return table.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int key, Pair<Integer, List<Integer>> value) {
        lock.lock();
        try {
            table.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        lock.lock();
        try {
            return new HashMap<>(table);
        } finally {
            lock.unlock();
        }
    }
}
