'''
7/12/14, 2:55 PM
Ethan Petuchowski
coin_on_the_table

https://www.hackerrank.com/challenges/coin-on-the-table
'''

from Queue import PriorityQueue

class MyPriorityQueue(PriorityQueue):
    """
     this is a recipe from
     http://stackoverflow.com/questions/9289614/how-to-put-items-into-priority-queues
     the normal PriorityQueue class seemed to get() the priorities? That doesn't
     make sense, but that's what it was doing, and this one does what one would expect.
    """
    def __init__(self):
        PriorityQueue.__init__(self)
        self.counter = 0

    def put(self, item, priority):
        PriorityQueue.put(self, (priority, self.counter, item))
        self.counter += 1

    def get(self, *args, **kwargs):
        _, _, item = PriorityQueue.get(self, *args, **kwargs)
        return item



UNMARKED, MARKED = range(2)
UP, DOWN, LEFT, RIGHT = range(4)
direcs = range(4)
texts = {
    UP: 'D',
    DOWN: 'U',
    LEFT: 'R',
    RIGHT: 'L'
}

def get_at(bd, coords):
    r, c = coords
    return bd[r][c]

def set_at(bd, coords, val):
    r, c = coords
    bd[r][c] = val

def neighbors(coords):
    r, c = coords
    neigh = {
        UP: (r-1, c),
        DOWN: (r+1, c),
        LEFT: (r, c-1),
        RIGHT: (r, c+1)
    }
    def check_neigh(direc):
        x, y = neigh[direc]
        return 0 <= x < N and 0 <= y < M \
               and get_at(board, neigh[direc]) == texts[direc] \
               and get_at(dists, neigh[direc]) == UNMARKED

    for di in direcs:
        if not check_neigh(di):
            del neigh[di]

    return  neigh.values()

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
# contains (initial [at least at first...]) min-dist to each loc from '*'
dists = [[UNMARKED]*M for _ in range(N)]

# we start at the '*' and mark (on-board) adj cells that point to it
# keep iterating out of queue until search depth reached
# if any cells are unmarked we must make some sort of operation to fix it
# this is roughly Dijkstra's Algorithm
cur_depth = 0
cur_loc = star_coords
set_at(dists, cur_loc, MARKED)
pq = PriorityQueue()

def stage():
    global cur_depth
    global cur_loc
    if not isinstance(cur_loc, tuple):
        print cur_loc
    for loc in neighbors(cur_loc):
        if not isinstance(loc, tuple):
            print loc
        pq.put(loc, cur_depth)
        set_at(dists, loc, cur_depth)
    cur_depth += 1

# DO
stage()

# WHILE
while not pq.empty() and cur_depth <= K:
    cur_loc = pq.get()
    stage()

# if all cells are now marked, we're done and we didn't have to do anything
done = True
for row in dists:
    if 0 in row:
        done = False
        break

# that's great
if done:
    print 0
    exit(0)

# otw, we've gotta figure out what to actually change (MINIMALLY). heh...
# somehow, I think we've gotta use that `dists` table
