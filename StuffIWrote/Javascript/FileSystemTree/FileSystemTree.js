class pathObj {
    constructor(str) {
        this.value = str
    }

    parent() {
        return this.value.substring(0, this.value.lastIndexOf("/"))
    }

    basename() {
        return this.value.substring(this.value.lastIndexOf("/") + 1, this.value.length)
    }
}

class tree {
    constructor() {
        this.root = new node("")
    }

    getNode(path) {
        return path.split("/") // get array of path components
        .filter(s => s != "") // remove the root and any trailing slash
        .reduce( // travel down tree
            (cur, next) => cur != null ? cur.getChild(next) : null,
            this.root
        )
    }

    /** returns the parent node*/
    addNode(node, parentPath) {
        if (parentPath == null || parentPath == "") return this.root.addChild(node)
        const parent = this.getNode(parentPath)  // crashes here: this.getNode is not a function (wat?)
        if (parent == null) return null
        return parent.addChild(node)
    }

    deleteNode(path) {
        const pathObj = new pathObj(path)
        const parentNode = this.getNode(pathObj.parent())
        if (parentNode == null) return null
        return parentNode.removeChild(pathObj.basename())
    }

    moveNode(component, oldDir, newDir) {
        const oldParent = this.getNode(oldDir)
        const node = oldParent.getChild(component)
        oldParent.removeChild(component)
        this.getNode(newDir).addChild(node)
        return this
    }

    treeString() {
        return this.root.treeString()
    }
}

class node {
    constructor(pathPiece) {
        this.children = []
        this.pathComponent = pathPiece
    }

    getPathComponent() {
        return this.pathComponent
    }

    addChild(child) {
        this.children.push(child)
        return this
    }

    removeChild(pathPiece) {
        const preLen = this.children.length
        this.children = this.children.filter(c => c.getPathComponent() != pathPiece)
        return preLen != this.children.length
    }

    getChild(pathPiece) {
        return this.children.find(c => c.getPathComponent() == pathPiece)
    }

    treeString() {
        let first = [this.pathComponent]
        // javascript has no "flatMap" so we do it by hand
        for (var i = 0; i < this.children.length; i++) {
            first = first.concat(this.children[i].treeString().map(l => "\t" + l))
        }
        return first
    }
}

const t = new tree()
const node1 = new node("1st")
const node2 = new node("2nd")
const node3 = new node("3rd")
t.addNode(node1, null)
t.addNode(node2, null)
t.addNode(node3, "/2nd")
t.treeString().forEach(line => console.log(line))
