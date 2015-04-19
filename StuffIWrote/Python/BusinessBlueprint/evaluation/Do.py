'''
4/19/15, 2:10 PM
Ethan Petuchowski
Do.py
'''

class Component(object):
    def __init__(self):
        self.title = ''
        self.functions = set()
        self.datas = set()

class Technology(Component):
    def __init__(self):
        super(Technology, self).__init__()
        self.coeffs = []

    def coeffsForComps(self, comp):
        self.coeffs.append(self.CompFuncCoeff(comp))

    def CompFuncBdErr(self):
        m = max(self.coeffs)
        return (1 - m) + (sum(self.coeffs) - m)

    def CompFuncCoeff(self,c):
        if self.CompFunc() > 0:
            return float(self.CompFuncReg(c)) / self.CompFunc()
        else:
            return 0

    def CompFuncReg(self,c):
        return len(self.functions.intersection(c.functions))

    def CompFunc(self):
        return len(self.functions)

    def CompDataCoeff(self,c):
        if self.CompData() > 0:
            return float(self.CompDataReg(c)) / self.CompData()
        else:
            return 0

    def CompDataReg(self,c):
        return len(self.datas.intersection(c.datas))

    def CompData(self):
        return len(self.datas)

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
                self.comp.functions.add(self.val)
            else: # (is blank)
                pass

        else: # state == DATA
            if not_blank(self.val):
                self.comp.datas.add(self.val)
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
                # print 'a = %s, b = %s, c = %s' % (a.val, b.val, c.val)

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

print 'For DB1:'
for tech in db1.components:
    for d in bb.components:
        tech.coeffsForComps(d)
        print 'CompFuncCoeff(%s,%s) = %s' % (tech.title, d.title, tech.CompFuncCoeff(d))
        print 'CompDataCoeff(%s,%s) = %s' % (tech.title, d.title, tech.CompDataCoeff(d))
    print 'CompFuncBoundaryError(%s) = %s' % (tech.title, tech.CompFuncBdErr())

print 'For DB2:'
for tech in db2.components:
    for d in bb.components:
        tech.coeffsForComps(d)
        print 'CompFuncCoeff(%s,%s) = %s' % (tech.title, d.title, tech.CompFuncCoeff(d))
        print 'CompDataCoeff(%s,%s) = %s' % (tech.title, d.title, tech.CompDataCoeff(d))
    print 'CompFuncBoundaryError(%s) = %s' % (tech.title, tech.CompFuncBdErr())
