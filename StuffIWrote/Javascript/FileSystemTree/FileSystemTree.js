const tree = () => {
    console.log("message")
    const root = node("")
    return {
        getNode: (path) => {
            const pieces = path.split("/")
            pieces.shift() // skip root node
            let cur = root
            for (const piece in pieces) {
                cur = cur.getChild(piece)
                if (cur == null) return null
            }
            return cur
        },
        /** returns the parent node*/
        addNode: (node, parentPath) => {
            if (parentPath == null || parentPath == "") return root.addChild(node)
            const parent = this.getNode(parentPath)  // crashes here: this.getNode is not a function (wat?)
            if (parent == null) return null
            return parent.addChild(node)
        },
        deleteNode: (path) => {},
        moveNode: (oldPath, newPath) => {},
        treeString: () => root.treeString()
    }
}

const node = (pathPiece) => {
    let children = []
    let pathComponent = pathPiece
    Array.prototype.flatMap = function (lambda) {
        return Array.prototype.concat.apply([], this.map(lambda));
    };
    return {
        getPathComponent: () => pathComponent,
        addChild: (child) => {
            children.push(child)
            return this
        },
        removeChild: (pathPiece) => {
            children = children.filter(c => c.getPathComponent() != pathPiece)
        },
        getChild: (pathPiece) => children.find(c => c.getPathComponent() == pathPiece),
        treeString: () => [pathComponent].concat(children.flatMap(c => c.treeString().map(line => "\t" + line)))
    }
}

const t = tree()
var node1 = node("1st")
const node2 = node("2nd")
const node3 = node("3rd")
t.addNode(node1, null)
t.addNode(node2, null)
t.addNode(node3, node2)
t.treeString().forEach(line => console.log(line))
