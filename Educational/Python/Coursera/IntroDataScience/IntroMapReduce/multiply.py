import MapReduce
import sys
from collections import defaultdict

mr = MapReduce.MapReduce()
size = 5
MATRIX, ROW, COL, VALUE = range(4)

def mapper(record):
    for i in range(size):
        is_a = record[MATRIX] == 'a'
        soln_coord = (record[ROW], i) if is_a else (i, record[COL])
        loc = record[COL] if is_a else record[ROW]

        # passing the MATRIX is not strictly necessary
        mr.emit_intermediate(soln_coord, [record[MATRIX], loc, record[VALUE]])


def reducer(key, list_of_values):
    d = defaultdict(list)
    for i in list_of_values:
        d[i[1]].append(i[2])
    total = 0
    for v in d.values():
        total += 0 if len(v) < 2 else v[0]*v[1]
    l = list(key)
    l.append(total)
    mr.emit(tuple(l))

if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
