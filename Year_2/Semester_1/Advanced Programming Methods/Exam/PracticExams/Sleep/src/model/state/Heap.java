package model.state;

import model.value.IValue;
import java.util.HashMap;
import java.util.Map;

public class Heap implements IHeap {
    private final Map<Integer, IValue> heap;
    private int nextFree;

    public Heap() {
        this.heap = new HashMap<>();
        this.nextFree = 1; // adresele valide incep de la 1
    }

    @Override
    public int allocate(IValue value) {
        int address = nextFree;
        heap.put(address, value);
        nextFree++;
        return address;
    }

    @Override
    public IValue read(int address) {
        if (!heap.containsKey(address)) {
            throw new RuntimeException("Invalid heap address: " + address);
        }
        return heap.get(address);
    }

    @Override
    public void write(int address, IValue value) {
        if (!heap.containsKey(address)) {
            throw new RuntimeException("Invalid heap address: " + address);
        }
        heap.put(address, value);
    }

    @Override
    public boolean containsKey(int address) {
        return heap.containsKey(address);
    }

    @Override
    public Map<Integer, IValue> getHeap() {
        return heap;
    }


    @Override
    public Map<Integer, IValue> getContent() {
        return heap;
    }

    @Override
    public void setContent(Map<Integer, IValue> map) {
        heap.clear();
        heap.putAll(map);
    }


    @Override
    public String toString() {
        return heap.toString();
    }
}
