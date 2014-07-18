import MapReduce
import sys

mr = MapReduce.MapReduce()

def mapper(record):
    mr.emit_intermediate(record[1], record)

def reducer(key, list_of_values):
    # key: order_id
    # value: db row

    # now I need to perform cross-prod of entries by table
    line_items = [i for i in list_of_values if i[0] == 'line_item']
    orders = [i for i in list_of_values if i[0] == 'order']
    for l in line_items:
      for o in orders:
        mr.emit(o + l)

# Do not modify below this line
# =============================
if __name__ == '__main__':
  inputdata = open(sys.argv[1])
  mr.execute(inputdata, mapper, reducer)
