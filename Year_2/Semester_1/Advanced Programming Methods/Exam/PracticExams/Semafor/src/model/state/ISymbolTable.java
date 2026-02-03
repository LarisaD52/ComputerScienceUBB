package model.state;


import java.util.Map;

public interface ISymbolTable<K, V> {
    boolean isDefined(K key);
    void put(K key, V value);
    void update(K key, V value);
    void add(K key, V value);
    V getValue(K key); //asta ii functia exhivalenta cu lockup

    boolean containsKey(K varName);

    Map<K,V> getContent();

    ISymbolTable<K,V> deepCopy();
}
