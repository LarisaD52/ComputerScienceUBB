#pragma once
#include <utility>

typedef int TKey;
typedef int TValue;
typedef std::pair<TKey, TValue> TElem;
#define NULL_TVALUE -111111
#define NULL_TELEM pair<TKey, TValue>(-111111, -111111)

class MapIterator;

class Map {
	friend class MapIterator;

private:
	struct Node {
		TElem data;// perechea (cheie, valoare)
		Node* next;//pointer la urmatorul element din lista
	};

	Node** table;//tabelul de dispersie – vector de liste
	int capacity;
	int numElements;//numarul de elemente (chei )

	int hashFunction(TKey key) const;
	static const int INITIAL_CAPACITY = 101;

public:
	Map();
	~Map();

	TValue add(TKey c, TValue v);
	TValue search(TKey c) const;
	TValue remove(TKey c);

	int size() const;
	bool isEmpty() const;

	MapIterator iterator() const;
	void resize();
	int updateValues(Map& m);
};
