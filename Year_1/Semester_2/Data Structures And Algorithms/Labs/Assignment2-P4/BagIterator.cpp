#include <exception>
#include "BagIterator.h"
#include "Bag.h"

using namespace std;

//Complexity: theta(1)
//Best Case == Worst Case
BagIterator::BagIterator(const Bag& c): bag(c)
{
	this->currentNode = c.head;//seteaza currentNode la inceputul listei
	this->currentFrequency = 1;
}

//Complexity: theta(1)
//Best Case == Worst Case
void BagIterator::first() {
	this->currentNode = this->bag.head;//seteaza currentNode la inceputul listei
}

//Complexity: theta(1)
//Best Case == Worst Case
void BagIterator::next() {
	if (this->currentNode == nullptr) {
		throw exception();
	}else{
		if (this->currentFrequency < this->currentNode->frequency) {//daca frecventa curenta e mai mica decat frecventa nodului curent
			this->currentFrequency++;//cresc frecventa curenta pentru a trece la urmatoarea aparitie a aceluiasi nod
		}else{
			this->currentNode = this->currentNode->next;//daca nu mai am frecvente de parcurs trec la urmatorul nod
			this->currentFrequency = 1;
		}
	}
}

//Complexity: theta(1)
//Best Case == Worst Case
bool BagIterator::valid() const {
	if (this->currentNode == nullptr) {
		return false;
	}
	return true;
}


//Complexity: theta(1)
//Best Case == Worst Case
TElem BagIterator::getCurrent() const
{
	if (this->currentNode == nullptr) {
		throw exception();
	}
	return this->currentNode->info;
}