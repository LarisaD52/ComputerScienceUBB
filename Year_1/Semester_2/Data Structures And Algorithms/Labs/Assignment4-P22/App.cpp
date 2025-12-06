#include "ExtendedTest.h"
#include "ShortTest.h"

#include "Map.h"


#include <iostream>
using namespace std;


int main() {
	cout<<"---Start"<<endl;
	testAll();
	testUpdateValues();
	testAllExtended();

	cout << "---That's all!" << endl;
	return 0;
}


