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
    gt = BrownGrammarTrainer(**default_dict)
    #gt.timed_train()
    #gt.make_csv()

def one_big_first_go():
    '''
    test the effect of including numbers and/or punctuation
    '''
    both_dict = default_dict.copy()
    both_dict['title'] = 'First Real Experiment'
    both_dict['part'] = '1'
    both_dict['maxim'] = 7
    both_dict['train_time'] = 40

    num_dict = both_dict.copy()
    punct_dict = both_dict.copy()
    none_dict = both_dict.copy()

    pid1 = os.fork()
    if pid1 == 0:
        both_gt = BrownGrammarTrainer(**both_dict)
        both_gt.timed_train(s=4)
        exit(1)

    num_dict['include_punctuation'] = False
    num_dict['part'] = '2'

    pid2 = os.fork()
    if pid2 == 0:
        num_gt = BrownGrammarTrainer(**num_dict)
        num_gt.timed_train(s=4)
        exit(2)


    punct_dict['include_numbers'] = False
    punct_dict['part'] = '3'
    pid3 = os.fork()
    if pid3 == 0:
        punct_gt = BrownGrammarTrainer(**punct_dict)
        punct_gt.timed_train(s=4)
        exit(3)

    none_dict['include_punctuation'] = False
    none_dict['include_numbers'] = False
    none_dict['part'] = '4'

    pid4 = os.fork()
    if pid4 == 0:
        none_gt = BrownGrammarTrainer(**none_dict)
        none_gt.timed_train(s=4)
        exit(4)

    a = os.waitpid(pid1, 0)
    b = os.waitpid(pid2, 0)
    c = os.waitpid(pid3, 0)
    d = os.waitpid(pid4, 0)

if __name__ == '__main__':
    one_big_first_go()
