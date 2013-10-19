import datetime
import os
from mcl_cli import CSVs_PATH


# TODO unfinished (not even sure what this will do yet)
def get_csv(date=''):
    # get today's date, format it like the filename
    if date is '':
        today = datetime.date.today()
    else:

        pass  # TODO parse-date
    todays_date = str(today.month) + '-' + str(today.day) + '-' + str(today.year)
    csvs = os.listdir(CSVs_PATH)
    for this_csv in csvs:
        csv_date = this_csv[:this_csv.find('.')]  # assumes the name is properly formatted
        if csv_date is todays_date:
            # read info into DataFrame
            pass