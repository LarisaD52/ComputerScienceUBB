#pragma once

typedef int TElem;
#define NULL_TELEM 0

#include <stdexcept>

class Matrix {
private:
    int nr_lines;
    int nr_cols;

    struct Triple {
        int line;
        int column;
        TElem value;

        bool operator<(const Triple& other) const {
            if (line != other.line) {
                return line < other.line;
            }
            return column < other.column;
        }

        bool operator==(const Triple& other) const {
            return line == other.line && column == other.column && value == other.value;
        }
    };

    Triple* elements;  //dynamic array to hold the triples
    int capacity;      //capacity of the dynamic array
    int size;          //urrent number of elements in the matrix

    void resize();  // Helper function to resize the dynamic array

public:
    Matrix(int nrLines, int nrCols);
    ~Matrix();

    Matrix(const Matrix& other);  //copy constructor
    Matrix& operator=(const Matrix& other);  //assignment operator

    int nrLines() const;
    int nrColumns() const;
    TElem element(int i, int j) const;
    TElem modify(int i, int j, TElem e);
    int numberOfNonZeroElems(int col) const;
    void transpose();
};
