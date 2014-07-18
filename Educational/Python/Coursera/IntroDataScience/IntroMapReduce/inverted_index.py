import MapReduce
import sys

mr = MapReduce.MapReduce()

def mapper(record):
    # key: document identifier
    # value: document contents
    doc_id = record[0]
    text = record[1]
    words = text.split()
    for w in words:
      mr.emit_intermediate(w, doc_id)

def reducer(key, list_of_values):
    mr.emit((key, list(set(list_of_values))))

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
