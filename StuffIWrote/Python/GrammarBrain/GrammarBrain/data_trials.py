import grammar_trainer

if __name__ == "__main__":
    train_data, test_data = grammar_trainer.create_train_and_test_sets(MIN_LEN=5, MAX_LEN=12)
    #print train_data.getSequence(0)
    #print train_data.getSequence(1)
    print train_data.getSequenceLength(0)
    print train_data.getSequenceLength(1)
    print train_data.getSequenceLength(2)
    print train_data.getSequenceLength(3)
    print train_data.getSequenceLength(4)
    print train_data.getSequenceLength(5)
    print train_data.getSequenceLength(6)
