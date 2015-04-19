'''
4/19/15, 2:10 PM
Ethan Petuchowski
Do.py
'''

class Technology(object):
    def __init__(self):
        self.title = ''
        self.functions = []
        self.datas = []

class Component(Technology):

    def CompFuncCoeff(self,t):
        pass

    def CompFuncReg(self,t):
        pass

    def CompFunc(self):
        pass

    def CompDataCoeff(self,t):
        pass

    def CompDataReg(self,t):
        pass

    def CompData(self):
        pass

class Blueprint(object):
    def __init__(self):
        self.components = []

ITEM = 'ITEM'
FUNC = 'FUNC'
DATA = 'DATA'


def not_blank(inp):
    return inp and inp.strip()

class ParseStruct(object):
    def __init__(self, tech=False):
        self.state = ITEM
        self.comp = Technology() if tech else Component()
        self.val = ''

    def do(self):
        if self.state == ITEM:
            if self.val.strip().lower() == 'functions':
                self.state = FUNC
            elif not_blank(self.val):
                self.comp.title = self.val
            else: # (is blank)
                pass

        elif self.state == FUNC:
            if self.val.strip().lower() == 'data':
                self.state = DATA
            elif not_blank(self.val):
                self.comp.functions.append(self.val)
            else: # (is blank)
                pass

        else: # state == DATA
            if not_blank(self.val):
                self.comp.datas.append(self.val)
            else: # (is blank)
                return True

        return False

class DataSheet(object):

    def __init__(self):
        self.bb = Blueprint()
        self.db1 = Blueprint()
        self.db2 = Blueprint()

    def import_from_csv(self):

        loc = '/Users/Ethan/Dropbox/EE382C Software Architecture SE/Assignments/SE Arch Assn3/My SE Arch Assn3 Answer/python_data.csv'

        a, b, c = ParseStruct(), ParseStruct(True), ParseStruct(True)
        f = open(loc)
        for everything in f:
            for line in everything.split('\r'):
                items = line.split(',')  # csv
                a.val, b.val, c.val = items[0], items[1], items[2]
                print 'a = %s, b = %s, c = %s' % (a.val, b.val, c.val)
                if a.do():
                    self.bb.components.append(a.comp)
                    a = ParseStruct()
                if b.do():
                    self.db1.components.append(b.comp)
                    b = ParseStruct(True)
                if c.do():
                    self.db2.components.append(c.comp)
                    c = ParseStruct(True)

        return self.bb, self.db1, self.db2

bb, db1, db2 = DataSheet().import_from_csv()
