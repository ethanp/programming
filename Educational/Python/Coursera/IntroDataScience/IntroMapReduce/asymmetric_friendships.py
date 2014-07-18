import MapReduce
import sys
from collections import Counter

FROM, TO = 1, -1

mr = MapReduce.MapReduce()

def mapper(record):
    mr.emit_intermediate((record[0], record[1]), FROM)
    mr.emit_intermediate((record[1], record[0]), TO)

def reducer(key, list_of_values):
    if sum(list_of_values) != 0:
        mr.emit(key)

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
