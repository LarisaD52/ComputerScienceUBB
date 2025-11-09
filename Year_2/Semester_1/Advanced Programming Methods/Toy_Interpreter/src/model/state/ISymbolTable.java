package model.state;


public interface ISymbolTable<K, V> {
    boolean isDefined(K key);
    void put(K key, V value);
    void update(K key, V value);

    V getValue(K key);
}
