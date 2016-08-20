const pathObj = (str) => {
    let value = str
    return {
        parent: () => value.substring(0, value.lastIndexOf("/")),
        basename: () => value.substring(value.lastIndexOf("/") + 1, value.length)
    }
}

const tree = () => {
    const root = node("")
    return {
        getNode: (path) => {
            return path.split("/") // get array of path components
            .filter(s => s != "") // remove the root
            .reduce( // travel down tree
                (cur, next) => cur != null ? cur.getChild(next) : null,
                root
            )
        },
        /** returns the parent node*/
        addNode: (node, parentPath) => {
            if (parentPath == null || parentPath == "") return root.addChild(node)
            const parent = this.getNode(parentPath)  // crashes here: this.getNode is not a function (wat?)
            if (parent == null) return null
            return parent.addChild(node)
        },
        deleteNode: (path) => {
            const pathObj = pathObj(path)
            const parentNode = this.getNode(pathObj.parent())
            if (parentNode == null) return null
            return parentNode.removeChild(pathObj.basename())
        },
        moveNode: (component, oldDir, newDir) => {
            const node = oldDir.getChild(component)
            oldDir.removeChild(component)
            newDir.addChild(node)
        },
        treeString: () => root.treeString()
    }
}

const node = (pathPiece) => {
    let children = []
    let pathComponent = pathPiece
    Array.prototype.flatMap = (lambda) => Array.prototype.concat.apply([], this.map(lambda))
    return {
        getPathComponent: () => pathComponent,
        addChild: (child) => {
            children.push(child)
            return this
        },
        removeChild: (pathPiece) => {
            const preLen = children.length
            children = children.filter(c => c.getPathComponent() != pathPiece)
            return preLen != children.length
        },
        getChild: (pathPiece) => children.find(c => c.getPathComponent() == pathPiece),
        treeString: () => [pathComponent].concat(children.flatMap(c => c.treeString().map(line => "\t" + line)))
    }
}

const t = tree()
const node1 = node("1st")
const node2 = node("2nd")
const node3 = node("3rd")
t.addNode(node1, null)
t.addNode(node2, null)
t.addNode(node3, node2)
t.treeString().forEach(line => console.log(line))
