#include "Bag.h"
#include "BagIterator.h"
#include <exception>
#include <iostream>
using namespace std;

//Complexity: theta(1)
//Best Case == Worst Case == theta(1)
Bag::Bag() {
	this->head = nullptr;
    this->tail = nullptr;
    this->sizeOfBag = 0;
}

//Complexity:O(n)
// -Best Case: theta(1)
// - Worst Case: theta(n)
void Bag::add(TElem elem) {
	if(this->head == nullptr) {
	    //daca bag e gol adaugam elementul in bag
	    //setam head si tail la nodul nou
        auto* newNode = new Node<TElem>;//creem un nod nou
        newNode->info = elem;
        newNode->frequency = 1;
        newNode->next = nullptr;
        newNode->prev = nullptr;

        this->head = newNode;
        this->tail = newNode;
        this->sizeOfBag++;
    }else{
        //cauta elementul in bag
        //daca elementul e gasit, crestem frecventa
        Node<TElem>* currentNode = this->head;
        while (currentNode != nullptr) {  //parcurgem lista paa cand gasim elementul sau ajungem la final
            if (currentNode->info == elem) {//dacac elementul exista in bag
                currentNode->frequency++;
                this->sizeOfBag++;
                return;
            }
            currentNode = currentNode->next;//continuam cu urmatorul nod
        }

        //daca elementul nu e gasit, il adaugam la sfarsitul listei
        auto* newNode = new Node<TElem>;//creem un nod nou

        newNode->info = elem;
        newNode->frequency = 1;
        newNode->next = nullptr;
        newNode->prev = this->tail;

        this->tail->next = newNode;
        this->tail = newNode;
        this->sizeOfBag++;
    }
}

//Complexity:O(n)
// - Best Case: theta(1)
// - Worst Case: theta(n)
bool Bag::remove(TElem elem) {
	if(this->head == nullptr) {//daca lista e goala nu avem sa stergem nimic
        return false;
    }

    Node<TElem>* currentNode = this->head; //incepem cautarea de la inceputul listei
    while (currentNode != nullptr) {
        if (currentNode->info == elem) {//dac am gasit elementul cautat
            if (currentNode->frequency > 1) {
                currentNode->frequency--;
                this->sizeOfBag--;
                return true;
            }else{
                if (currentNode == this->head) {//daca elementul e in capul listei,adica are frecventa 1 trebuie eliminat complet nodul
                    this->head = currentNode->next;
                    if (this->head != nullptr) {// Daca mai exista noduri dupa el le actualizam legatura inapoi
                        this->head->prev = nullptr;
                    }
                    delete currentNode;//eliberam memoria
                    this->sizeOfBag--;
                    return true;
                }else if (currentNode == this->tail) {//daca nodul e in coada listei
                    this->tail = currentNode->prev;
                    if (this->tail != nullptr) {//daca mai exista noduri inaintea lui le actualizam legatura
                        this->tail->next = nullptr;
                    }
                    delete currentNode;
                    this->sizeOfBag--;
                    return true;
                }else{//daca nodul e in mijlocul listei
                    currentNode->prev->next = currentNode->next;
                    currentNode->next->prev = currentNode->prev;
                    delete currentNode;
                    this->sizeOfBag--;
                    return true;
                }
            }
        }
        currentNode = currentNode->next; //continuam cautarea
    }
	return false;
}

//Complexity:O(n)
// - Best Case: theta(1)
// - Worst Case: theta(n)
bool Bag::search(TElem elem) const {
	if(this->head == nullptr) {//verificam daca bag e gol
        return false;
    }

    Node<TElem>* currentNode = this->head;//incepem cautarea de la inceputul listei
    while (currentNode != nullptr) {
        if (currentNode->info == elem) {//daca elementul e gasit
            return true;
        }
        currentNode = currentNode->next;
    }

    return false;
}

//Complexity:O(n)
// - Best Case: theta(1)
// - Worst Case: theta(n)
int Bag::nrOccurrences(TElem elem) const {
    //daca bag e gol nu avem cum sa gasim elementul deci returnam 0
	if(this->head == nullptr) {
        return 0;
    }

    Node<TElem>* currentNode = this->head;
    while (currentNode != nullptr) {
        if (currentNode->info == elem) {//daca gasesc nodul ii returnez fecventa
            return currentNode->frequency;
        }
        currentNode = currentNode->next;
    }

    return 0;
}

//Complexity: theta(1)
//Best Case == Worst Case == theta(1)
int Bag::size() const {
	return this->sizeOfBag;
}

//Complexity: theta(1)
//Best Case == Worst Case == theta(1)
bool Bag::isEmpty() const {
	return this->sizeOfBag == 0;
}

//Complexity: theta(1)
//Best Case == Worst Case == theta(1)
BagIterator Bag::iterator() const {
	return BagIterator(*this);//creaza un iterator pentru bag
    // (*this)=  the current object
}

//Complexity: theta(n)
//Best Case == Worst Case == theta(n)
Bag::~Bag() {
    Node<TElem>* currentNode = this->head;
    while (currentNode != nullptr) {//o sa sterg ficare nod
        Node<TElem>* nextNode = currentNode->next;
        delete currentNode;
        currentNode = nextNode;//trcerea la nodul urmator
    }
    delete currentNode;
}





//Complexity:O(n)
//Best Case: theta(1)->adica elementul e la inceputul listei
//Worst Case: theta(n)->adica elementul e la sfarsitul listei sau nu exista
int Bag::removeOccurrences(int nr, TElem elem) {
    if (nr < 0) {
        throw "Negative number not allowed";
    }

    if (this->head == nullptr || nr == 0) {//daca bag e gol sau nr e 0 nu avem ce sa stergem
        return 0;
    }

    Node<TElem>* currentNode = this->head;

    while (currentNode != nullptr) {


        if (currentNode->info == elem) {
            //calculam cate elemente putem sterge
            int removedCount;
            if (nr < currentNode->frequency) {
                removedCount = nr;
            } else {
                removedCount = currentNode->frequency;//daca nr e mai mare decat frecventa nodului stegem tot nodul
            }


            currentNode->frequency -= removedCount;//reducem frecventa nodului
            this->sizeOfBag -= removedCount;//scadem nr total de elemente din bag


            if (currentNode->frequency == 0) {//cand nodul este la inceputul listei
                if (currentNode == head) {
                    head = currentNode->next;
                    if (head != nullptr) {
                        head->prev = nullptr;
                    }
                } else if (currentNode == tail) {//cand nodul este la sfarsitul listei
                    tail = currentNode->prev;
                    if (tail != nullptr) {
                        tail->next = nullptr;
                    }
                } else {//cand nodul este in mijlocul listei
                    currentNode->prev->next = currentNode->next;
                    currentNode->next->prev = currentNode->prev;
                }

                delete currentNode;
            }

            return removedCount;
        }
        currentNode = currentNode->next;
    }

    return 0;
}






