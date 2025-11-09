package model.state;

public interface IFileTable<K, V> {
  void addOpenFile(K key, V value);

  V get(K key);



  void remove(K key);



  boolean isOpen(String key);
}
