#include "Map.h"
#include "MapIterator.h"

// BC: Theta(1)
// WC: Theta(capacity)
// Total: O(capacity)
Map::Map() {
    this->capacity = INITIAL_CAPACITY;
    this->table = new Node*[capacity];//tabelul= vector de liste
    for (int i = 0; i < capacity; i++)
        table[i] = nullptr;//initializez fiecare lista ca fiind goala
    this->numElements = 0;
}

// BC: Theta(capacity)
// WC: Theta(capacity + n)
// Total: O(capacity + n)
Map::~Map() {
    for (int i = 0; i < capacity; i++) {//parcurgem fiecare bucket
        Node* current = table[i];
        while (current != nullptr) {
            Node* toDelete = current;//memoram nodul curent ca sa il stergem
            current = current->next;
            delete toDelete;
        }
    }
    delete[] table;
}


// BC: Theta(1)
// WC: Theta(1)
// Total: Theta(1)
int Map::hashFunction(TKey key) const {
    int hash = key % capacity;
    if (hash < 0)
        hash += capacity;
    return hash;
}


// BC: Theta(1)
// WC: Theta(n)
// Total: O(n)
TValue Map::add(TKey c, TValue v) {
    if (numElements > capacity ) {
        resize();
    }
    int pos = hashFunction(c); //calculam pozitia in tabel pe baza functiei hash
    Node* current = table[pos];
    while (current != nullptr) {
        if (current->data.first == c) {//daca cheia exista
            TValue old = current->data.second;//memoram valoarea veche
            current->data.second = v;//actualizam valoarea cu cea noua
            return old;
        }
        current = current->next;
    }
    Node* newNode = new Node{ std::make_pair(c, v), table[pos] };//daca cheia nu exista, cream un nod nou ce il adaugam la inceputul listei
    table[pos] = newNode;
    numElements++;
    return NULL_TVALUE;
}


// BC: Theta(1)
// WC: Theta(n)
// Total: O(n)
TValue Map::search(TKey c) const {
    int pos = hashFunction(c);
    Node* current = table[pos];
    while (current != nullptr) {
        if (current->data.first == c)//daca gasim cheia
            return current->data.second;//returnam valoarea
        current = current->next;
    }
    return NULL_TVALUE;
}


// BC: Theta(1)
// WC: Theta(n)
// Total: O(n)
TValue Map::remove(TKey c) {
    int pos = hashFunction(c);
    Node* current = table[pos];
    Node* prev = nullptr;//nodul anterior

    while (current != nullptr) {
        if (current->data.first == c) {
            TValue val = current->data.second;
            if (prev == nullptr)//inseamna ca nodul curent este primul din lista, adica nu am trecut pana acum prin niciun nod
                table[pos] = current->next;//mutam inceputul listei la urmatorul nod
            else
                prev->next = current->next;
            delete current;
            numElements--;
            return val;
        }
        prev = current;
        current = current->next;
    }
    return NULL_TVALUE;
}


// BC: Theta(1)
// WC: Theta(1)
// Total: Theta(1)
int Map::size() const {
    return numElements;
}
// Functia de redimensionare a tabelului (marirea capacitatii)
// BC: Theta(n)
// WC: Theta(n)
// Total: Theta(n)
void Map::resize() {
    //dublam dimensiunea
    int newCapacity = capacity * 2;
    Node** newTable = new Node*[newCapacity];  //cream un nou tabel cu dimensiune mai mare

    //initializam noul tabel cu nullptr
    for (int i = 0; i < newCapacity; i++) {
        newTable[i] = nullptr;
    }

    //mutam toate elementele din tabelul vechi in tabelul nou
    for (int i = 0; i < capacity; i++) {
        Node* current = table[i];
        while (current != nullptr) {  //parcurgem lista de noduri din tabelul vechi
            int newPos = current->data.first % newCapacity;  //recalculam hash-ul pentru noua capacitate
            if (newPos < 0) newPos += newCapacity;  //ajustam valoarea hash pentru a fi pozitiva
            Node* newNode = new Node{current->data, newTable[newPos]};  //cream un nod nou
            newTable[newPos] = newNode;  //adaugam nodul in tabelul nou
            current = current->next;  // mergem la urmatorul nod din lista
        }
    }

    //stergem vechiul tabel si actualizam cu noul tabel
    delete[] table;
    table = newTable;
    capacity = newCapacity;
}

// BC: Theta(1)
// WC: Theta(1)
// Total: Theta(1)
bool Map::isEmpty() const {
    return numElements == 0;
}


// BC: Theta(1)
// WC: Theta(1)
// Total: Theta(1)
MapIterator Map::iterator() const {
    return MapIterator(*this);
}


//BC: Theta(m_size)
//WC: Theta(m_size * n)
//Total: o(m_size * n)
int Map::updateValues(Map& m) {
    int updatedCount = 0;
    MapIterator it = m.iterator();  // creem un iterator pentru Map m

    while (it.valid()) {
        TElem elem = it.getCurrent();
        TKey key = elem.first;
        TValue newVal = elem.second;

        int pos = hashFunction(key);
        Node* current = table[pos];//luam lista din bucket-ul respectiv

        while (current != nullptr) {
            if (current->data.first == key) {
                if (current->data.second != newVal) {
                    current->data.second = newVal;  // actualizam valoarea
                    updatedCount++;
                }
                break;  // o cheie apare o singura data, pentru ca cheile sunt unice in MAp
            }
            current = current->next;
        }

        it.next();
    }

    return updatedCount;
}
