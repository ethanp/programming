import pandas as pd
import csv
from mcl_cli import *
import os

SAMPLE_TASK = 'Sample Task'
SAMPLE_GROUP = 'Sample'
FILE_SAMPLE_TASK_1 = TASKS_PATH+'/'+SAMPLE_GROUP+'/'+SAMPLE_TASK


def add_sample_task():
    add_task(group=SAMPLE_GROUP,
             name=SAMPLE_TASK,
             due_date='11-13-2013')


def append_time_to_sample_task():
    with open(SAMPLE_TASK, 'a+') as task_file:
        writer = csv.writer(task_file, delimiter='\n')
        writer.writerow(['37'])


def clear_sample_task():
    os.remove(FILE_SAMPLE_TASK_1)


if __name__ == "__main__":
    add_sample_task()
    #append_to_sample_task()
