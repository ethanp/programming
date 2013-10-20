import pandas as pd
import csv
from mcl_cli import *
import os


SAMPLE_TASK = 'Sample Task'
SAMPLE_GROUP = 'Sample'
SAMPLE_GROUP_PATH = TASKS_PATH+'/'+SAMPLE_GROUP
SAMPLE_TASK_PATH = SAMPLE_GROUP_PATH+'/'+SAMPLE_TASK


def add_sample_task():
    add_task(group=SAMPLE_GROUP,
             name=SAMPLE_TASK,
             due_date='11-13-2013')


def append_time_to_sample_task():
    with open(SAMPLE_TASK, 'a+') as task_file:
        writer = csv.writer(task_file, delimiter='\n')
        writer.writerow(['37'])


def clear_sample_task():
    os.remove(SAMPLE_TASK_PATH)


def add_sample_group():
    add_group(SAMPLE_GROUP)
    if SAMPLE_GROUP not in os.listdir(TASKS_PATH):
        print 'add_sample_group FAILED!'


def delete_sample_group():
    delete_group(SAMPLE_GROUP)
    if os.path.exists(SAMPLE_GROUP_PATH):
        print 'delete_sample_group FAILED!'


def test_ls():
    print ls()


if __name__ == "__main__":
    #add_sample_task()
    #append_to_sample_task()
    #test_ls()
    add_sample_group()
    delete_sample_group()
