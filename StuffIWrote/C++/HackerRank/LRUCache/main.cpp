#include <iostream>
#include <vector>
#include <map>
#include <string>
#include <algorithm>
#include <set>
#include <cassert>

using namespace std;

struct Node {
    Node* next;
    Node* prev;
    int value;
    int key;

    Node(Node* p, Node* n, int k, int val)
            : prev(p), next(n), key(k), value(val) { };

    Node(int k, int val)
            : prev(NULL), next(NULL), key(k), value(val) { };
};

class Cache {

protected:
    map<int, Node*> mp; //map the key to the node in the linked list
    int cp;  //capacity
    Node* tail; // double linked list tail pointer
    Node* head; // double linked list head pointer
    virtual void set(int, int) = 0; //set function
    virtual int get(int) = 0; //get function

};


/**
 * Ethan Petuchowski wrote this class ONLY on Jan 30, 2016,
 * As part of learning C++ using HackerRank as a tutorial.
 * The rest of it is code given by the tutorial.
 */
class LRUCache : public Cache {

    void moveToFront(Node* node) {
        if (node == NULL || node == head) {
            return;
        }
        Node* curHead = head;
        node->prev->next = node->next;
        if (node != tail) {
            node->next->prev = node->prev;
        }
        else {
            tail = node->prev;
            tail->next = NULL;
        }
        node->next = curHead;
        curHead->prev = node;
        node->prev = NULL;
        head = node;
    }

public:

    LRUCache(int capacity) {
        cp = capacity;
        head = NULL;
        tail = NULL;
    };

    virtual void set(int key, int val) {
        // check if key is already in the cache
        auto search = mp.find(key);
        if (search != mp.end()) {
            // if the value is incorrect, change it
            search->second->value = val;

            // if it is, move it to the front
            moveToFront(search->second);
        }
        else {
            // if it's not, add it
            Node* newHead = new Node(NULL, head, key, val);
            mp.insert(make_pair(key, newHead));

            if (tail != NULL) {
                // if it's too big now, the last node must be removed
                if (mp.size() > cp) {
                    Node* oldTail = tail;
                    tail = tail->prev;
                    tail->next = NULL;
                    delete oldTail;
                }

                // and current head shoved aside
                head->prev = newHead;
            }
            else {
                // if the list is empty,
                // it is both head *and* tail
                tail = newHead;
            }

            head = newHead;
        }
    }

    virtual int get(int key) {
        // if key doesn't exist return -1
        auto search = mp.find(key);
        if (search == mp.end()) {
            return -1;
        }

        int ret = search->second->value;

        // otw set as most recently used
        moveToFront(search->second);
        // and return the value
        return ret;
    }
};

int main() {
    int n, capacity, i;
    cin >> n >> capacity;
    LRUCache l(capacity);
    for (i = 0; i < n; i++) {
        string command;
        cin >> command;
        if (command == "get") {
            int key;
            cin >> key;
            cout << l.get(key) << endl;
        }
        else if (command == "set") {
            int key, value;
            cin >> key >> value;
            l.set(key, value);
        }
    }
    return 0;
}
