#include <iostream>
#include "ExtendedTest.h"
#include "ShortTest.h"

using namespace std;


int main() {

    cout<<"--->Tests Start!"<<endl;
    testAll();
    testTranspose();
    testAllExtended();
    cout << "--->Tests End!" << endl;
    //system("pause");
}