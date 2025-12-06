#include "ShortTest.h"
#include "ExtendedTest.h"
#include "SortedSet.h"
#include "SortedSetIterator.h"
#include <iostream>

using namespace std;

int main() {
	testAll();
	testIntersection();
	cout << "Short tests passed" << endl;
	testAllExtended();
	cout << "Extended tests passed" << endl;

	cout << "Test end" << endl;

}