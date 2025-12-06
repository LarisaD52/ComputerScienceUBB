#include "ShortTest.h"
#include <assert.h>
#include "Bag.h"
#include "BagIterator.h"


void testAll() { 
	Bag b;
	assert(b.isEmpty() == true);
	assert(b.size() == 0); 
	b.add(5);
	b.add(1);
	b.add(10);
	b.add(7);
	b.add(1);
	b.add(11);
	b.add(-3);
	assert(b.size() == 7);
	assert(b.search(10) == true);
	assert(b.search(16) == false);
	assert(b.nrOccurrences(1) == 2);
	assert(b.nrOccurrences(7) == 1);
	assert(b.remove(1) == true);
	assert(b.remove(6) == false);
	assert(b.size() == 6);
	assert(b.nrOccurrences(1) == 1);
	BagIterator it = b.iterator();
	it.first();
	while (it.valid()) {
		TElem e = it.getCurrent();
		it.next();
	}
}


void testRemoveOccurrences() {
	Bag b;

	//add elements: 5 (3 times), 3 (2 times), 10 (once)
	b.add(5);
	b.add(5);
	b.add(5);
	b.add(3);
	b.add(3);
	b.add(10);

	//check initial state
	assert(b.nrOccurrences(5) == 3);
	assert(b.nrOccurrences(3) == 2);
	assert(b.nrOccurrences(10) == 1);

	//remove 2 occurrences of 5
	int removed = b.removeOccurrences(2, 5);
	assert(removed == 2);
	assert(b.nrOccurrences(5) == 1);

	//try to remove 5 occurrences of 3(only 2 exist)
	removed = b.removeOccurrences(5, 3);
	assert(removed == 2);
	assert(b.nrOccurrences(3) == 0);

	//try to remove 1 occurrence of a missing element(3)
	removed = b.removeOccurrences(1, 3);
	assert(removed == 0);

	//remove the only occurrence of 10
	removed = b.removeOccurrences(1, 10);
	assert(removed == 1);
	assert(b.nrOccurrences(10) == 0);

	//try to call with a negative number of occurrences
	bool exceptionThrown = false;
	try {
		b.removeOccurrences(-1, 5);
	} catch (...) {
		exceptionThrown = true;
	}
	assert(exceptionThrown);

	//check the final size of the Bag after all removals
	assert(b.nrOccurrences(5) == 1);  //there should be 1 occurrence of 5
	assert(b.nrOccurrences(3) == 0);  //there should be no occurrences of 3
	assert(b.nrOccurrences(10) == 0); //there should be no occurrences of 10
}
