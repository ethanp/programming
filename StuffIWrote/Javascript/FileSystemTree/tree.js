const node = require('./node')
const pathObj = require('./pathObj')

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
