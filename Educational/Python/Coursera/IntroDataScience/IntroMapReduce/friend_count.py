import MapReduce
import sys

mr = MapReduce.MapReduce()

def mapper(record):
    mr.emit_intermediate(record[0], record[1])

def reducer(key, list_of_values):
    # key: word
    # value: list of occurrence counts
    mr.emit((key, len(list_of_values)))

# Do not modify below this line
# =============================
if __name__ == '__main__':
    inputdata = open(sys.argv[1])
    mr.execute(inputdata, mapper, reducer)
