import grammar_trainer

if __name__ == "__main__":
    train_data, test_data = grammar_trainer.create_dataset(MIN_LEN=5, MAX_LEN=8)
    print train_data.getSequence(0)
    print train_data.getSequence(1)
    print train_data.getSequenceLength(3)
