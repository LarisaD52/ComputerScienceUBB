#pragma once
//DO NOT INCLUDE BAGITERATOR


//DO NOT CHANGE THIS PART
#define NULL_TELEM -111111;
typedef int TElem;
class BagIterator;

template <class TElem>


struct Node {
	TElem info;//valoarea de ex 5
	int frequency;//de cate ori apare
	Node* next;//legatura spre urmatorul nod
	Node* prev;//legatura spre nodul anterior
};

class Bag {

private:
	//TODO - Representation
	Node<TElem>* head;//inceputul listei
	Node<TElem>* tail;//sfarsitul listei
	int sizeOfBag;//numarul total de elemente din bag(cu tot cu dubluri)


	//DO NOT CHANGE THIS PART
	friend class BagIterator;
public:
	//constructor
	Bag();

	//adds an element to the bag
	void add(TElem e);

	//removes one occurence of an element from a bag
	//returns true if an element was removed, false otherwise (if e was not part of the bag)
	bool remove(TElem e);

	//checks if an element appearch is the bag
	bool search(TElem e) const;

	//returns the number of occurrences for an element in the bag
	int nrOccurrences(TElem e) const;

	//returns the number of elements from the bag
	int size() const;

	//returns an iterator for this bag
	BagIterator iterator() const;

	//checks if the bag is empty
	bool isEmpty() const;

	//destructor
	~Bag();

	int removeOccurrences(int nr, TElem elem);
};