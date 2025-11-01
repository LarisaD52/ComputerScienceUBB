package model.state;

import java.util.Map;

public interface ISymbolTable<K, V> {
    boolean isDefined(K key);     // verifică dacă cheia există
    void put(K key, V value);     // adaugă o pereche nouă (ca la declarare)
    void update(K key, V value);  // actualizează valoarea existentă
    V getValue(K key);              // preia valoarea (nume conform PDF)
    void remove(K key);           // opțional, pentru completitudine
    Map<K, V> getContent();       // returnează conținutul complet
}
