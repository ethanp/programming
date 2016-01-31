#include <iostream>
#include <deque>
using namespace std;
// they won't let me just use std::max_element...
int myMax(deque<int> &dq) {
    int max = 0;
    for (auto val : dq)
        if (val > max)
            max = val;
    return max;
}
void printKMax(int arr[], int n, int k){
    deque<int> dq;
    int curMax = 0;
    for (int i = 0; i < n; i++) {
        curMax = max(curMax, arr[i]);
        dq.push_front(arr[i]);
        bool redo = dq[dq.size()-1] == curMax;
        if (dq.size() > k) dq.pop_back();
        if (redo) curMax = myMax(dq);
        if (dq.size() == k) printf("%d ", curMax);
    }
    printf("\n");
}

int main() {

    int t;
    cin >> t;
    while(t --> 0) {
        int n,k;
        cin >> n >> k;
        int i;
        int arr[n];
        for(i=0;i<n;i++)
            cin >> arr[i];
        printKMax(arr, n, k);
    }
    return 0;
}
