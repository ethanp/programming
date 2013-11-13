import grammar_trainer

if __name__ == "__main__":
    train_data, test_data = grammar_trainer.create_dataset(3, 15)
    print train_data.getSequence(0)
    print train_data.getSequence(1)
    print train_data.getSequenceLength(3)
