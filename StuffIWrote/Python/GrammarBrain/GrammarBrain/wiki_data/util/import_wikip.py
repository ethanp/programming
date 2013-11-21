from collections import Counter
import wiki_pos_map
import sys, os
path = '/Users/Ethan/Desktop/Data/wiki_tagged.en/'


file_count = 0
sentences, sentences_POSs, sent, sent_POS = [], [], [], []
for data_file in os.listdir(path):
    if file_count < 2:
        file_count += 1
        print data_file
        with open(path+data_file, 'rb') as text:
            lines = text.readlines()
            print len(lines)
            if len(lines) < 3:
                # blank file
                break
            for line in lines:
                if line[0] == '<':
                    # meta-data
                    continue
                a = line.split(' ')
                if len(a) > 2:
                    # collect sentence
                    sent.append(a[0])
                    sent_POS.append(a[2])
                else:
                    # end of sentence
                    # save sentence, etc.
                    sentences.append(sent[:])
                    sentences_POSs.append(sent_POS[:])
                    sent = []
                    sent_POS = []


# using this in the way I have means that there
# exist sentences with no corresponding matrix
def filter_invalid_POSs(s):
    for p in s:
        if p not in wiki_pos_map.the_map.keys():
            return False
    return True

sentences_POSs = filter(filter_invalid_POSs, sentences_POSs)

print sentences[:2]
print sentences_POSs[:2]

# go through sentences, and put it in 'file_%d' % len(sentence)
# if len(sentence) < 25

for i in range(2,25):
    pass
