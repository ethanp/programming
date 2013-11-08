import pandas as pd
import csv
from mcl_cli import *
import os


SAMPLE_TASK = 'Sample Task'
SAMPLE_GROUP = 'Sample'
SAMPLE_GROUP_PATH = TASKS_PATH+'/'+SAMPLE_GROUP
SAMPLE_TASK_PATH = SAMPLE_GROUP_PATH+'/'+SAMPLE_TASK

def try_get_task_path():
    add_sample_task()
    get_task_path(SAMPLE_TASK)

def add_sample_task():
    add_task(name=SAMPLE_TASK,
             group=SAMPLE_GROUP,
             due_date='11-13-2013',
             note='This is a sample task!')


def append_time_to_sample_task():
    with open(SAMPLE_TASK, 'a+') as task_file:
        writer = csv.writer(task_file, delimiter='\n')
        writer.writerow(['37'])


def clear_sample_task():
    os.remove(SAMPLE_TASK_PATH)


def add_sample_group():
    add_group(SAMPLE_GROUP)
    if not os.path.exists(SAMPLE_GROUP_PATH):
        print 'add_sample_group FAILED!'


def delete_sample_group():
    delete_group(SAMPLE_GROUP)
    if os.path.exists(SAMPLE_GROUP_PATH):
        print 'delete_sample_group FAILED!'

def test_add_and_delete_group():
    add_sample_group()
    delete_sample_group()


def test_finish_task():
    add_sample_task()
    finish_task(SAMPLE_TASK)


def test_ls():
    print ls()
    print ls(SAMPLE_GROUP)


if __name__ == "__main__":
    #try_get_task_path()
    test_finish_task()