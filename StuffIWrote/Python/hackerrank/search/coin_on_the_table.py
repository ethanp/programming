'''
7/12/14, 2:55 PM
Ethan Petuchowski
coin_on_the_table

https://www.hackerrank.com/challenges/coin-on-the-table
'''

from Queue import PriorityQueue

def get_at(coords):
    row, col = coords
    return board[row][col]

def set_at(coords, val):
    row, col = coords
    board[row][col] = val

# N = rows of board
# M = width of row
# K = search depth
N, M, K = map(int, raw_input().split())
board = [raw_input() for _ in range(N)]

star_coords = (0,0)
for i, row in enumerate(board):
    if '*' in row:
        star_coords = i, row.find('*')
        break

# zeros(N, M)
marked = [[0]*M for _ in range(N)]

# we start at the '*' and mark (on-board) adj cells that point to it
# keep iterating out of queue until search depth reached
# if any cells are unmarked we must make some sort of operation to fix it
# this is roughly Dijkstra's Algorithm
cur_depth = 0
cur_loc = star_coords
pq = PriorityQueue() # you do `pq.put((priority, element))`, then `next = pq.get()
