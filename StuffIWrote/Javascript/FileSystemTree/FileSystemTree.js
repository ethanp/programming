const kefir = require('kefir')
const EventEmitter = require('events')

/* TODO
* make it interact with kefir
* make it a node module e.g. in npm
*/

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
        const parent = this.getNode(parentPath)
        if (parent == null) return null
        return {
            success: parent.addChild(node),
            tree: this
        }
    }

    deleteNode(path) {
        const pathObj = new pathObj(path)
        const parentNode = this.getNode(pathObj.parent())
        if (parentNode == null) return null
        return {
            success: parentNode.removeChild(pathObj.basename()),
            tree: this
        }
    }

    moveNode(component, oldDir, newDir) {
        const oldParent = this.getNode(oldDir)
        const node = oldParent.getChild(component)
        oldParent.removeChild(component)
        const newParent = this.getNode(newDir)
        newParent.addChild(node)
        return this
    }

    treeString() {
        return this.root.treeString()
    }
}

class node {
    constructor(pathPiece) {
        this.children = []
        this.parent = null
        this.pathComponent = pathPiece
        this.data = new Map()
    }

    getPathComponent() {
        return this.pathComponent
    }

    getAbsolutePath() {
        return this.parent != null
            ? this.parent.getAbsolutePath() + "/" + this.pathComponent
            : this.pathComponent
    }

    addChild(child) {
        this.children.push(child)
        child.setParent(this)
        return this
    }

    setParent(node) {
        this.parent = node
    }

    /** returns true if a node was removed */
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
        for (let i = 0; i < this.children.length; i++) {
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
//console.log(node3.getAbsolutePath())
//t.treeString().forEach(line => console.log(line))

const t2 = new tree();
["a", "b", "c"].forEach(p => t2.addNode(new node(p)))
//console.log(t2.getNode("/").children.map(c => c.pathComponent))
//t2.treeString().forEach(line => console.log(line))

const em = new EventEmitter()

var userCreatedConnection = kefir.fromEvents(em, "userCreatedConnection")

em.on("userCreatedConnection", evt => console.log(evt + ": from em.on"))

const mapped = userCreatedConnection.map(evt => {
    console.log("asdf")
    const n1 = evt.fromNode
    const n2 = evt.toNode
    //const result = t.addNode(n1, n2)
    return "mapped"
}).log()

mapped.onValue(val => console.log("inside onValue"))

userCreatedConnection.onValue((val) => console.log("got value"))

em.emit("userCreatedConnection", "the string we emitted")

