package model.state;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LatchTable implements ILatchTable {

    private final Map<Integer, Integer> latchTable;
    private int freeLocation;
    private final Lock lock;

    public LatchTable() {
        this.latchTable = new HashMap<>();
        this.freeLocation = 1;
        this.lock = new ReentrantLock();
    }

    @Override
    public int get(int key) {
        lock.lock();
        try {
            return latchTable.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(int key, int value) {
        lock.lock();
        try {
            latchTable.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean containsKey(int key) {
        lock.lock();
        try {
            return latchTable.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int add(int value) {
        lock.lock();
        try {
            int location = freeLocation;
            latchTable.put(location, value);
            freeLocation++;
            return location;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Map<Integer, Integer> getContent() {
        lock.lock();
        try {
            return new HashMap<>(latchTable);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void setContent(Map<Integer, Integer> newContent) {
        lock.lock();
        try {
            latchTable.clear();
            latchTable.putAll(newContent);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            return latchTable.toString();
        } finally {
            lock.unlock();
        }
    }
}
