#include "ShortTest.h"
#include <assert.h>
#include "Map.h"
#include "MapIterator.h"
#include <iostream>
using namespace std;

void testAll() { //call each function to see if it is implemented
	Map m;
	assert(m.isEmpty() == true);
	assert(m.size() == 0); //add elements
	assert(m.add(5,5)==NULL_TVALUE);
	assert(m.add(1,111)==NULL_TVALUE);
	assert(m.add(10,110)==NULL_TVALUE);
	assert(m.add(7,7)==NULL_TVALUE);
	assert(m.add(1,1)==111);
	assert(m.add(10,10)==110);
	assert(m.add(-3,-3)==NULL_TVALUE);
	assert(m.size() == 5);
	assert(m.search(10) == 10);
	assert(m.search(16) == NULL_TVALUE);
	assert(m.remove(1) == 1);
	assert(m.remove(6) == NULL_TVALUE);
	assert(m.size() == 4);


	TElem e;
	MapIterator id = m.iterator();
	id.first();
	int s1 = 0, s2 = 0;
	while (id.valid()) {
		e = id.getCurrent();
		s1 += e.first;
		s2 += e.second;
		id.next();
	}
	assert(s1 == 19);
	assert(s2 == 19);
	cout << "testAll passed" << endl;
}


void testUpdateValues() {
	Map m1;
	Map m2;

	// Adaugam elemente in m1
	m1.add(1, 100);
	m1.add(2, 200);
	m1.add(3, 300);
	m1.add(4, 400);

	// Adaugam elemente in m2
	m2.add(2, 250);  //comun cu m1, valoare diferita
	m2.add(3, 300);  //comun cu m1, valoare identica
	m2.add(5, 500);  //nu exista in m1

	// Apelam functia
	int modified = m1.updateValues(m2);

	// Verificam cate modificari s-au facut (doar cheia 2 avea valoare diferita)
	assert(modified == 1);

	//verific daca valorile din m1 au fost actualizate corect
	assert(m1.search(1) == 100);  // nemodificat
	assert(m1.search(2) == 250);  // modificat
	assert(m1.search(3) == 300);  // nemodificat
	assert(m1.search(4) == 400);  // nemodificat
	cout<<"Test updateValues passed!"<<endl;
}


