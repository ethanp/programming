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

module.exports = class tree {
    constructor() {
        this.root = new node("")
    }

    /**
     * get all the nodes in this tree as an array
     * (depth-first)
     */
    allNodes() {
        return this.root.getSubtree()
    }

    getNodeByID(id) {
        return this.root.findByID(id)
    }

    /** returns a simple string visualization of the entire tree */
    treeString() {
        return this.root.treeString()
    }

    getNodeByAbsolutePath(path) {
        return path.split("/") // get array of path components
            .filter(s => s != "") // remove the root and any trailing slash
            .reduce( // travel down tree
                (cur, next) => cur == null ? null : cur.getChild(next),
                this.root
            )
    }

    /** returns the parent node*/
    addNode(node, parentPath) {
        if (parentPath == null || parentPath == "") return this.root.addChild(node)
        const parent = this.getNodeByAbsolutePath(parentPath)
        return {
            success: parent == null ? false : parent.addChild(node),
            tree: this
        }
    }

    deleteNode(path) {
        const pathObj = new pathObj(path)
        const parentNode = this.getNodeByAbsolutePath(pathObj.parent())
        return {
            success: parentNode == null ? null : parentNode.removeChild(pathObj.basename()),
            tree: this
        }
    }

    /** move node "component" from "oldDir" to "newDir" */
    moveNode(component, oldDir, newDir) {
        const oldParent = this.getNodeByAbsolutePath(oldDir)
        const node = oldParent.getChild(component)
        oldParent.removeChild(component)
        const newParent = this.getNodeByAbsolutePath(newDir)
        newParent.addChild(node)
        return this
    }
}

class node {
    constructor(pathPiece) {
        this.children = []
        this.parent = null
        this.uid = Symbol()
        this.pathComponent = pathPiece
        this.data = new Map()
    }

    getPathComponent() {
        return this.pathComponent
    }

    isRoot() {
        return this.parent == null
    }

    findByID(id) {
        return this.uid == id ? this : this.children.find(c => c.findByID(id));
    }

    getAbsolutePath() {
        return this.isRoot()
            ? this.pathComponent
            : this.parent.getAbsolutePath() + "/" + this.pathComponent;
    }

    addChild(child) {
        this.children.push(child)
        child.setParent(this)
        return this
    }

    setParent(node) {
        this.parent = node
        return this
    }

    /** returns true if a node was removed */
    removeChild(pathPiece) {
        const oldNumChildren = this.numChildren()
        this.children = this.children.filter(c => c.getPathComponent() != pathPiece)
        return this.numChildren() < oldNumChildren
    }

    numChildren() {
        return this.children.length
    }

    /** returns undefined if the child doesn't exist */
    getChild(pathPiece) {
        if (pathPiece.split("/") > 1) {
            alert("unforeseen usecase: complex path pieces in getChild are not supported")
        }
        return this.children.find(c => c.getPathComponent() == pathPiece)
    }

    /** create a simple string visualization of this node and its descendents */
    treeString() {
        return this.children.reduce(
            (prev, cur) => prev.concat(
                cur.treeString().map(l => "\t" + l)),
            [this.pathComponent]
        )
    }

    /**
     * get an array that starts with this node, and contains all its descendents
     * (pre-order, depth-first)
     */
    getSubtree() {
        return this.children.reduce(
            (prev, cur) => prev.concat(cur.getSubtree()), [this]
        )
    }
}
/*
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
console.log(t2.allNodes())

//console.log(t2.getNodeByAbsolutePath("/").children.map(c => c.pathComponent))
//t2.treeString().forEach(line => console.log(line))

const em = new EventEmitter()

const userCreatedConnection = kefir.fromEvents(em, "userCreatedConnection");

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

//em.emit("userCreatedConnection", "the string we emitted")
*/
