#include "SortedSetIterator.h"
#include <exception>

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
SortedSetIterator::SortedSetIterator(const SortedSet& m) : multime(m) {
	this->stackCapacity = 100;
	this->stackSize = 0;
	this->stack = new int[stackCapacity];
	this->currentNode = -1;
	first();
}

//BC=theta(n)
//WC=theta(n)
//TC=theta(n)
void SortedSetIterator::resizeStack() {
	int newCapacity = stackCapacity * 2;
	int* newStack = new int[newCapacity];
	for (int i = 0; i < stackSize; i++)
		newStack[i] = stack[i];
	delete[] stack;
	stack = newStack;
	stackCapacity = newCapacity;
}

//BC=theta(1)
//WC=theta(h)
//TC=O(h)
void SortedSetIterator::pushLeft(int node) {
	while (node != -1) {
		if (stackSize == stackCapacity)
			resizeStack();
		stack[stackSize++] = node;
		node = multime.tree[node].left;
	}
}

//BC=theta(1)
//WC=theta(h)
//TC=O(h)
void SortedSetIterator::first() {
	stackSize = 0;
	pushLeft(multime.root);

	if (stackSize > 0)
		currentNode = stack[--stackSize];
	else
		currentNode = -1;
}

//BC=theta(1)
//WC=theta(h)
//TC=O(h)
void SortedSetIterator::next() {
	if (!valid())
		throw std::exception();

	int rightChild = multime.tree[currentNode].right;
	pushLeft(rightChild);

	if (stackSize > 0)
		currentNode = stack[--stackSize];
	else
		currentNode = -1;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
TElem SortedSetIterator::getCurrent() {
	if (!valid())
		throw std::exception();
	return multime.tree[currentNode].value;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
bool SortedSetIterator::valid() const {
	return currentNode != -1;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
SortedSetIterator::~SortedSetIterator() {
	delete[] stack;
}
