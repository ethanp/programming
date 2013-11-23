from pybrain import TanhLayer, LSTMLayer
from BrownGrammarTrainer import BrownGrammarTrainer

default_dict = {
        'title'               : 'default dict title',
        'minim'               : 3,
        'maxim'               : 4,
        'outdim'              : 2,
        'hiddendim'           : [5],
        'train_time'          : 2,
        'basic_pos'           : True,
        'hidden_type'         : LSTMLayer,
        'output_type'         : TanhLayer,
        'include_punctuation' : True,
        'include_numbers'     : True
}

def test_experiment_setup():
    gt = BrownGrammarTrainer(**default_dict)
    #gt.timed_train()
    #gt.make_csv()

if __name__ == '__main__':
    test_experiment_setup()
