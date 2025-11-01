package model.state;

import java.util.HashMap;
import java.util.Map;

import exceptions.KeyNotFoundException;

public class MapSymbolTable<K,V> implements ISymbolTable<K,V>{
    private final Map<K,V> map;

    public MapSymbolTable(){
        this.map = new HashMap<>();
    }

    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public void update(K key, V value) {
        if (!map.containsKey(key)) {
            throw new KeyNotFoundException("Key not defined in symbol table: " + key);
        }
        map.put(key, value);
    }

    @Override
    public V getValue(K key) {
        return map.get(key);
    }

    @Override
    public void remove(K key) {
        map.remove(key);
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }

    @Override
    public String toString() {
        return map.toString();
    }
}