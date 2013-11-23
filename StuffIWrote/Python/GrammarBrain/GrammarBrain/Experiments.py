import os
from pybrain import TanhLayer, LSTMLayer
from BrownGrammarTrainer import BrownGrammarTrainer

default_dict = {
        'title'               : 'default dict title',
        'part'                : 'default part',
        'minim'               : 3,
        'maxim'               : 4,
        'outdim'              : 2,
        'hiddendim'           : [50],
        'train_time'          : 2,
        'basic_pos'           : True,
        'hidden_type'         : LSTMLayer,
        'output_type'         : TanhLayer,
        'include_punctuation' : True,
        'include_numbers'     : True
}

def test_experiment_setup():
    test_dict = default_dict.copy()
    test_dict['hiddendim'] = [3]
    test_dict['maxim'] = 3
    gt = BrownGrammarTrainer(**default_dict)
    gt.timed_train()
    gt.make_csv_and_pickle()


''' test the effect of including numbers and/or punctuation '''
# TODO note that this doesn't include questions or exclamations
def both():
    both_dict = default_dict.copy()
    both_dict['title'] = 'First Real Experiment'
    both_dict['part'] = '1'
    both_dict['maxim'] = 7
    both_dict['train_time'] = 40
    both_gt = BrownGrammarTrainer(**both_dict)
    both_gt.timed_train(s=4)
    both_gt.make_csv_and_pickle()

def no_punct():
    num_dict = default_dict.copy()
    num_dict['title'] = 'First Real Experiment'
    num_dict['part'] = '2'
    num_dict['maxim'] = 7
    num_dict['train_time'] = 40
    num_dict['include_punctuation'] = False
    num_gt = BrownGrammarTrainer(**num_dict)
    num_gt.timed_train(s=4)
    num_gt.make_csv_and_pickle()

def no_num():
    punct_dict = default_dict.copy()
    punct_dict['title'] = 'First Real Experiment'
    punct_dict['part'] = '3'
    punct_dict['maxim'] = 7
    punct_dict['train_time'] = 40
    punct_dict['include_numbers'] = False
    punct_gt = BrownGrammarTrainer(**punct_dict)
    punct_gt.timed_train(s=4)
    punct_gt.make_csv_and_pickle()

def none():
    none_dict = default_dict.copy()
    none_dict['title'] = 'First Real Experiment'
    none_dict['part'] = '4'
    none_dict['maxim'] = 7
    none_dict['train_time'] = 40
    none_dict['include_punctuation'] = False
    none_dict['include_numbers'] = False

    none_gt = BrownGrammarTrainer(**none_dict)
    none_gt.timed_train(s=4)
    none_gt.make_csv_and_pickle()


if __name__ == '__main__':
    test_experiment_setup()
