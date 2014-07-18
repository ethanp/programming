import MapReduce
import sys

mr = MapReduce.MapReduce()

def mapper(record):
    mr.emit_intermediate(record[1][:-10],0)

def reducer(key, list_of_values):
    mr.emit(key)

if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
