'''
7/13/14, 4:03 PM
Ethan Petuchowski
even_tree

https://www.hackerrank.com/challenges/even-tree
'''

from collections import defaultdict

def even_tree(graph, root):
    count = 0
    size = 1

    if root not in graph.keys():
        return count, size

    for child in graph[root]:
        c_count, c_size = even_tree(graph, child)
        count += c_count
        size += c_size

    if size % 2 == 0:
        count += 1
        size = 0

    return count, size

def main():
    # build graph from input
    num_vertices, num_edges = map(int, raw_input().split())
    graph = defaultdict(set)
    for _ in range(num_edges):
        u, v = map(int, raw_input().split())
        graph[v].add(u)
    # print graph

    # find root
    all_children = set()
    for v in graph.values():
        all_children = all_children.union(v)
    root = set(graph.keys()).difference(all_children).pop()
    # print 'root:', root

    count, size = even_tree(graph, root)
    print count-1

if __name__ == '__main__':
    main()
