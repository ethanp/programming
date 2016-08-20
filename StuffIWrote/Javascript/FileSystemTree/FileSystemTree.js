/**
 * Created by Ethan on 8/19/16.
 *
 * API
 * ================================================
 * getNode(id)
 * addNode(node, directory a.k.a."parent")
 * deleteNode(id)
 * moveNode(id, new path)
 * get/SetAttribute/Field(id, fieldName, newValue)
 * getPath(path)
 */
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
            const parent = this.getNode(parentPath)
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
    return {
        getPathComponent: () => pathComponent,
        addChild: (child) => {
            children.push(child)
            return this
        },
        removeChild: (id) => {
            children = children.filter(c => c.id != id)
        },
        getChild: (pathPiece) => {
            return children.find(c => c.getPathComponent() == pathPiece)
        },
        treeString: () => {
            const stringArray = children.map(c => c.treeString().map(line => "\t" + line))
            stringArray.unshift(pathComponent)
            return stringArray
        }
    }
}

const t = tree()
t.addNode(node("asdf"), null)
console.log(t.treeString())
