package model.state;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTable implements ILockTable {

    private final Map<Integer, Integer> table;
    private int freeLocation;
    private final Lock lock;

    public LockTable() {
        this.table = new HashMap<>();
        this.freeLocation = 1;
        this.lock = new ReentrantLock();
    }


    @Override
    public int get(int key) {
        lock.lock();
        try {
            return table.get(key);
        } finally {
            lock.unlock();
        }
    }


    @Override
    public void update(int key, int value) {
        lock.lock();
        try {
            table.put(key, value);
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
    public int getFreeLocation() {
        lock.lock();
        try {
            return freeLocation++;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public Map<Integer, Integer> getContent() {
        lock.lock();
        try {
            return new HashMap<>(table);
        } finally {
            lock.unlock();
        }
    }
}
