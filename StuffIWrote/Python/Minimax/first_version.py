# +----------------------------------+
# |                                  |
# | Just give me a minimax in Python |
# |                                  |
# +----------------------------------+

def minimax(root):
    return root.minimax()


class Node(object):
    pass


class InternalNode(Node):
    def __init__(self, children):
        self.children = children

    def __str__(self):
        return '{' + ','.join(map(str, self.children)) + '}'


class MaxNode(InternalNode):
    def evaluate(self):
        return max(map(lambda x: x.minimax(), self.children))


class MinNode(InternalNode):
    def evaluate(self):
        return min(map(lambda x: x.minimax(), self.children))


class ExternalNode(Node):
    def __init__(self, value):
        self.value = value

    def __str__(self):
        return '{' + str(self.value) + '}'

    def evaluate(self):
        print 'evaluation:', str(self.value)
        return self.value


sample_graph = \
    MaxNode([
        MinNode([
            MaxNode([
                ExternalNode(5),
                ExternalNode(3)
            ]),
            ExternalNode(3)
        ]),
        ExternalNode(5)
    ])

print sample_graph
print minimax(sample_graph), 'should have value:', 5
