#include "Matrix.h"
#include <algorithm>

// Constructor
Matrix::Matrix(int nrLines, int nrCols) {
    this->nr_lines = nrLines;
    this->nr_cols = nrCols;
    this->capacity = 10;  // Initial capacity for the dynamic array
    this->size = 0;
    this->elements = new Triple[capacity];
}
//BC: Theta(1)
//WC: Theta(1)
//Total: Theta(1)


// Destructor
Matrix::~Matrix() {
    delete[] elements;  // Deallocate the dynamic array
}
//BC: Theta(1)
//WC: Theta(1)
//Total: Theta(1)


// Copy constructor
Matrix::Matrix(const Matrix& other) {
    nr_lines = other.nr_lines;
    nr_cols = other.nr_cols;
    size = other.size;
    capacity = other.capacity;
    elements = new Triple[capacity];
    for (int i = 0; i < size; i++) {
        elements[i] = other.elements[i];
    }
}
//BC: Theta(size)    --->time complexity depends on the number of non-zero elements (size)
//WC: Theta(size)
//Total: Theta(size)



// Assignment operator
Matrix& Matrix::operator=(const Matrix& other) {
    if (this != &other) {
        delete[] elements;  // Free the existing resources

        nr_lines = other.nr_lines;
        nr_cols = other.nr_cols;
        size = other.size;
        capacity = other.capacity;
        elements = new Triple[capacity];

        for (int i = 0; i < size; i++) {
            elements[i] = other.elements[i];
        }
    }
    return *this;
}
//BC: Theta(size)     --->time complexity depends on the number of non-zero elements (size)
//WC: Theta(size)
//Total: Theta(size)




// Return the number of lines
int Matrix::nrLines() const {
    return nr_lines;
}
//BC: Theta(1)
//WC: Theta(1)
//Total: Theta(1)



// Return the number of columns
int Matrix::nrColumns() const {
    return nr_cols;
}
//BC: Theta(1)
//WC: Theta(1)
//Total: Theta(1)



//returns the element from line i and column j (indexing starts from 0)
//throws exception if (i,j) is not a valid position in the Matrix
TElem Matrix::element(int i, int j) const {
    if (i < 0 || i >= nr_lines || j < 0 || j >= nr_cols) {
        throw std::out_of_range("Invalid position");
    }
    for (int idx = 0; idx < size; idx++) {
        if (elements[idx].line == i && elements[idx].column == j) {
            return elements[idx].value;
        }
    }
    return NULL_TELEM;
}
//BC: Theta(1)  --->if the element is the first one
//WC: Theta(size)
//Total: O(size)




// Modify the element at position (i, j) and return the previous value
TElem Matrix::modify(int i, int j, TElem e) {
    if (i < 0 || i >= nr_lines || j < 0 || j >= nr_cols) {
        throw std::out_of_range("Invalid position");
    }

    for (int idx = 0; idx < size; idx++) {
        if (elements[idx].line == i && elements[idx].column == j) {
            TElem old_value = elements[idx].value;
            if (e == NULL_TELEM) {
                // Remove the element by shifting the rest
                for (int j = idx; j < size - 1; j++) {
                    elements[j] = elements[j + 1];
                }
                size--;
            } else {
                elements[idx].value = e;
            }
            return old_value;
        }
    }

    if (e != NULL_TELEM) {
        // If the element does not exist, we need to add it
        if (size == capacity) {
            resize();  // Resize the array if necessary
        }

        elements[size++] = {i, j, e};  // Add the new element
        std::sort(elements, elements + size);  // Sort the elements lexicographically
    }

    return NULL_TELEM;
}
//BC: Theta(1)
//WC: Theta(size *log size)
//Total: O(size log size)




// Resize the dynamic array
void Matrix::resize() {
    capacity *= 2;
    Triple* new_elements = new Triple[capacity];
    for (int i = 0; i < size; i++) {
        new_elements[i] = elements[i];
    }
    delete[] elements;
    elements = new_elements;
}
//BC: Theta(1)
//WC: Theta(size)
//Total: O(size)




//returns the number of non-zero elements from a given column
//throws an exception if col is not valid
int Matrix::numberOfNonZeroElems(int col) const {
    if (col < 0 || col >= nr_cols) {
        throw std::out_of_range("Invalid column");
    }

    int count = 0;
    for (int i = 0; i < size; i++) {
        if (elements[i].column == col && elements[i].value != NULL_TELEM) {
            count++;
        }
    }
    return count;
}
//BC: Theta(1)
//WC: Theta(size)
//Total: O(sizse)



void Matrix::transpose() {
    /*
    *(0,1) → 10 o sa fie (1,0) → 10
    *(2,3) → 20 -//-
     */
    // Swap the number of rows and columns
    std::swap(nr_lines, nr_cols);//schimb dimensiunea matricei,deoarece nr de linii se schimba cu cel de coloane

    // Swap line and column for each non-zero element
    for (int i = 0; i < size; i++) {
        std::swap(elements[i].line, elements[i].column);
    }

    // Sort elements lexicographically based on (line, column)
    std::sort(elements, elements + size, [](const Triple& a, const Triple& b) {
        return (a.line < b.line) || (a.line == b.line && a.column < b.column);
    });
}
//BC: Theta(1)
//WC: Theta(size)
//Total: O(size * log(size))