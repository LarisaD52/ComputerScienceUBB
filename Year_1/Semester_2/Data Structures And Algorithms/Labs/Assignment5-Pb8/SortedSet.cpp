#include "SortedSet.h"
#include "SortedSetIterator.h"
#include <iostream>

//BC=theta(1)
//WC=theta(n)
//TC=O(log n)
SortedSet::SortedSet(Relation r) {
    this->capacity = 10;
    this->tree = new BSTNode[capacity];  //alocam vectorul de noduri
    this->root = -1;
    this->firstEmpty = 0;
    this->length = 0;
    this->relation = r; //salvam relatia

    for (int i = 0; i < capacity; i++) {
        tree[i].left = -1;
        tree[i].right = -1;
    }
}

//BC=theta(n)
//WC=theta(n)
//TC=theta(n)
void SortedSet::resize() {
    int newCapacity = capacity * 2; //dublam capacitatea
    BSTNode* newTree = new BSTNode[newCapacity];//alocam noul array
    for (int i = 0; i < capacity; i++) {//copiem nodurile existente
        newTree[i] = tree[i];
    }
    for (int i = capacity; i < newCapacity; i++) {
        newTree[i].left = -1;
        newTree[i].right = -1;
    }
    delete[] tree;
    tree = newTree;
    capacity = newCapacity;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
int SortedSet::createNode(TComp value) {
    if (firstEmpty == capacity)
        resize();
    int pos = firstEmpty++;
    tree[pos].value = value;
    tree[pos].left = -1;
    tree[pos].right = -1;
    return pos;
}


//BC=theta(1)
//WC=theta(n)
//TC=O(log n)
bool SortedSet::add(TComp elem) {
    if (root == -1) {
        root = createNode(elem);  // daca arborele este gol, o sa creez un nod radacina
        length++;
        return true;
    }

    int current = root;
    int parent = -1;
    bool isLeftChild = false;

    while (current != -1) {
        if (tree[current].value == elem)    //daca valoarea exista deja nu o adaugam
            return false;  // nu inserăm duplicate

        parent = current;
        if (relation(elem, tree[current].value)) {
            current = tree[current].left;
            isLeftChild = true;
        } else {
            current = tree[current].right;
            isLeftChild = false;
        }
    }

    int newNode = createNode(elem);

    if (isLeftChild)
        tree[parent].left = newNode;
    else
        tree[parent].right = newNode;

    length++;
    return true;
}


//BC=theta(1)
//WC=theta(n)
//TC=O(h)
int SortedSet::findMax(int node) const {
    while (tree[node].right != -1)//mergem pana la cel mai din dreapta nod
        node = tree[node].right;
    return node;
}


//BC=theta(1)
//WC=theta(n)
//TC=O(log n)
bool SortedSet::remove(TComp elem) {
    int current = root;
    int parent = -1;
    bool isLeftChild = false;

    //cautam nodul care trebuie sters
    while (current != -1 && tree[current].value != elem) {
        parent = current;
        if (relation(elem, tree[current].value)) {
            isLeftChild = true;
            current = tree[current].left;
        } else {
            isLeftChild = false;
            current = tree[current].right;
        }
    }

    if (current == -1)
        return false;  // Elementul nu a fost gasit

    //nodul nu are copii
    if (tree[current].left == -1 && tree[current].right == -1) {
        if (current == root)
            root = -1;
        else if (isLeftChild)
            tree[parent].left = -1;
        else
            tree[parent].right = -1;
    }
    //nodul are un singur copil (doar stang)
    else if (tree[current].right == -1) {
        if (current == root)
            root = tree[current].left;
        else if (isLeftChild)
            tree[parent].left = tree[current].left;
        else
            tree[parent].right = tree[current].left;
    }
    //nodul are un singur copil (doar drept)
    else if (tree[current].left == -1) {
        if (current == root)
            root = tree[current].right;
        else if (isLeftChild)
            tree[parent].left = tree[current].right;
        else
            tree[parent].right = tree[current].right;
    }
    //nodul are doi copii
    else {
        //gasim maximul din subarborele stang
        int maxParent = current;
        int maxLeft = tree[current].left;
        while (tree[maxLeft].right != -1) {
            maxParent = maxLeft;
            maxLeft = tree[maxLeft].right;
        }

        //inlocuim valoarea
        tree[current].value = tree[maxLeft].value;

        //stergem nodul duplicat
        if (tree[maxLeft].left != -1) {
            if (tree[maxParent].right == maxLeft)
                tree[maxParent].right = tree[maxLeft].left;
            else
                tree[maxParent].left = tree[maxLeft].left;
        } else {
            if (tree[maxParent].right == maxLeft)
                tree[maxParent].right = -1;
            else
                tree[maxParent].left = -1;
        }
    }

    length--;
    return true;
}


//BC=theta(1)
//WC=theta(n)
//TC=O(log n)
bool SortedSet::search(TComp elem) const {
    int current = root;
    while (current != -1) {
        if (tree[current].value == elem)
            return true;
        if (relation(elem, tree[current].value))
            current = tree[current].left;
        else
            current = tree[current].right;
    }
    return false;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
int SortedSet::size() const {
    return length;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
bool SortedSet::isEmpty() const {
    return length == 0;
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
SortedSetIterator SortedSet::iterator() const {
    return SortedSetIterator(*this);
}

//BC=theta(1)
//WC=theta(1)
//TC=theta(1)
SortedSet::~SortedSet() {
    delete[] tree;
}






//BC=Theta(1)
//WC=Theta(n * log n)
//TC=O(n * log n)
void SortedSet::intersection(const SortedSet& s) {
    SortedSetIterator it(*this);
    it.first();
    while (it.valid()) {
        TComp elem = it.getCurrent();
        it.next();
        if (!s.search(elem)) {
            this->remove(elem);
        }
    }
}
