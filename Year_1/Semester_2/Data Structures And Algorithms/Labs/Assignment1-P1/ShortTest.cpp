#include <cassert>
#include <iostream>
#include <ostream>

#include "Matrix.h"

using namespace std;

void testAll() {
    Matrix m(4, 4);
    assert(m.nrLines() == 4);
    assert(m.nrColumns() == 4);
    m.modify(1, 1, 5);
    assert(m.element(1, 1) == 5);
    TElem old_value = m.modify(1, 1, 6);
    assert(m.element(1, 2) == NULL_TELEM);
    assert(old_value == 5);
    cout << "All tests passed!" << endl;
}

void testTranspose() {
    // Test transpose
    Matrix m(4, 4);
    // Modify some elements
    m.modify(0, 1, 10);
    m.modify(2, 3, 20);

    m.transpose();

    assert(m.nrLines() == 4);
    assert(m.nrColumns() == 4);
    //vefific daca elementele s-au transpus corect
    assert(m.element(1, 0) == 10);  // (0,1) → (1,0)
    assert(m.element(3, 2) == 20);  // (2,3) → (3,2)

    assert(m.element(1, 1) == NULL_TELEM);
    assert(m.element(0, 1) == NULL_TELEM);
    cout<<"Transpose test passed!"<<endl;
}