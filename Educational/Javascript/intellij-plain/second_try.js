/** This is something we can get and put into Mongo */
const TreeNode = (p_id, p_parents, p_relativePath, p_children, p_content) => {

    const id = p_id
    const parents = p_parents || [{ isSomething: true }]
    const relativePath = p_relativePath
    const children = p_children || [{ isSymbolic: true, nodeId: "asdf" }]
    const content = p_content || { text: "this is my text on tv" }

    return {
        treeBelow: () => {
            const obj = this.asTreeObj()
            return children.map(c => mongo.getNodeById(c.nodeId).treeBelow())
        },
        asTreeObj: () => {
            return {
                id:           this.id,
                relativePath: this.relativePath,
                children:     this.children,
                content:      this.content
            }
        }
    }
}

const mongo = (port) => {

    // TODO this (obvi) doesn't work
    const conn = mongoConn(port)

    return {
        getNodeById: (id) => {
            const mongoObj = conn.find({ id: id })

            // TODO this doesn't look right, see FunFunFunction for how to fix it
            // or see developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/create
            return Object.create(TreeNode, {
                id:           mongoObj.id,
                parents:      mongoObj.parents,
                relativePath: mongoObj.relativePath,
                children:     mongoObj.children,
                content:      mongoObj.content
            })
        }
    }
}
