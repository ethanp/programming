# +----------------------------------------------+
# |                                              |
# |      Reformulate MINIMAX to be recursive     |
# | It should expand children nodes as necessary |
# |                                              |
# +----------------------------------------------+

class InternalNode(object):
    def __init__(self, children):
        self.children = children

    def minimax(self, do_max, depth):
        vals = map(lambda x: x.minimax(not do_max, depth+1), self.children)
        return max(vals) if do_max else min(vals)

class ExternalNode(object):
    def __init__(self, value):
        self.value = value

    # noinspection PyUnusedLocal
    def evaluate(self, do_max):
        return self.value

trial_graph = \
    InternalNode([
        InternalNode([
            InternalNode([
                ExternalNode(5),
                ExternalNode(3)
            ]),
            ExternalNode(3)
        ]),
        ExternalNode(5)
    ])

print trial_graph.minimax(True, 0)
