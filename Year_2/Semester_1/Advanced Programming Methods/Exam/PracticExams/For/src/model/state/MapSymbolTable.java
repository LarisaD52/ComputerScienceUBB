package model.state;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import exceptions.KeyNotFoundException;

public class MapSymbolTable<K,V> implements ISymbolTable<K,V>{
    private final Map<K,V> map;

    public MapSymbolTable(){
        this.map = new HashMap<>();
    }

    @Override
    public Map<K, V> getContent() {
        return map;
    }



    @Override
    public boolean isDefined(K key) {
        return map.containsKey(key);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, value);
    }

    public void add(K key, V value) {
        this.put(key, value);
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
    public boolean containsKey(K varName) {
        return map.containsKey(varName); // CORECȚIE
    }



    @Override
    public ISymbolTable<K, V> deepCopy() {
        MapSymbolTable<K, V> newTable = new MapSymbolTable<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            newTable.put(entry.getKey(), entry.getValue());
            // V este deja un IValue (care este imutabil), deci poti copia referința
        }
        return newTable;
    }


    @Override
    public String toString() {
        return map.toString();
    }
}