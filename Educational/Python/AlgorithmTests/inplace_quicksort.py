# 5/7/14
# in-place quicksort
# source: wikipedia.org/wiki/Quicksort
# pseudocode -> code: Ethan Petuchowski

def partition(array, left, right, pivot_index):
    """
    array:      The entire array we were given to sort.
    left-right: The section of it we are partitioning.
    pivotIndex: The location holding the value with
                respect to which we should partition
                the elements.
    """
    pivot_value = array[pivot_index]

    # move the pivot element to the right end of the partition
    array[pivot_index], array[right] = array[right], array[pivot_index]

    # go through this section of the array, and put all elements less than
    # or equal to the pivot to the left of all elements greater than it
    store_index = left
    for i in range(left, right):
        if array[i] <= pivot_value:
            array[i], array[store_index] = array[store_index], array[i]
            store_index += 1

    # switch the pivot (currently parked on the right) with the left
    # most greater-than element, meaning it will bisect the two groups
    array[store_index], array[right] = array[right], array[store_index]

    # return the new index of the given pivot
    return store_index

def quicksort(array, left, right):
    """
    array:      The entire array we were given to sort.
    left-right: The section of it we are partitioning.
    """
    if left < right:
        pivot_index = (left + right) / 2

        # partition the given section of input array,
        # note new index of the given pivot element
        new_pivot_index = partition(array, left, right, pivot_index)

        # sort the unsorted left-partition
        quicksort(array, left, new_pivot_index - 1)

        # and the unsort right-partition
        quicksort(array, new_pivot_index + 1, right)


def test_quicksort():
    a = [1,2,3,4,5]
    b = [5,4,3,2,1]
    c = [12,51,451,4,123,5,45,3,3145,1]
    orig = [a,b,c]
    to_sort = [a[:],b[:],c[:]]
    for i in range(len(to_sort)):
        quicksort(to_sort[i], 0, len(to_sort[i]) - 1)
        if to_sort[i] == sorted(orig[i]):
            print i, 'Pass'
        else:
            print i, 'Fail'

if __name__ == '__main__':
    test_quicksort()
