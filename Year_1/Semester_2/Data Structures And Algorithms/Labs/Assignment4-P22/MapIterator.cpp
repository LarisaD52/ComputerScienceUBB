#include "MapIterator.h"
#include <exception>

using namespace std;

// BC: Theta(1)
// WC: Theta(capacity)
// Total: O(capacity)
MapIterator::MapIterator(const Map& m) : map(m) {
	first();//initialize iterator cu prima pozitie valida
}

// BC: Theta(1)
// WC: Theta(capacity)
// Tota: O(capacity)
void MapIterator::first() {
	currentBucket = 0;
	currentNode = nullptr;
	while (currentBucket < map.capacity && map.table[currentBucket] == nullptr)//cautam primul bucket valid
		currentBucket++;
	if (currentBucket < map.capacity)//daca am gasit un bucket valid, il setam ca nod curent
		currentNode = map.table[currentBucket];
}

// BC: Theta(1)
// WC: Theta(capacity)
// Total: O(capacity)
void MapIterator::next() {
	if (!valid())
		throw exception();

	currentNode = currentNode->next;
	while (currentNode == nullptr && ++currentBucket < map.capacity)//daca am ajuns la finalul listei, cautam urmatorul bucket valid
		currentNode = map.table[currentBucket];
}

// BC=WC=Total = Theta(1)
TElem MapIterator::getCurrent() {
	if (!valid())
		throw exception();
	return currentNode->data;
}

// BC=WC=Total = Theta(1)
bool MapIterator::valid() const {
	return currentNode != nullptr;
}
