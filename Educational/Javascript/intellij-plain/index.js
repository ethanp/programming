const TreeNode = (node_id, node_parents, node_pathComponent, node_children, node_content) => {
    const id = node_id
    const parents = node_parents || [{ nodeId: "asdf" }, { nodeId: "bteg" }]
    const pathComponent = node_pathComponent
    const children = node_children || [{
            isSymbolic: true,
            nodeId:     "asdf"
        }]
    const content = node_content || {}
    return {
        draw: () => {
            return node_id
        }
    }
}

const VisualNode = (xCoord, yCoord, baseNode) => {
    const x = xCoord
    const y = yCoord
    const treeNode = baseNode
    return {
        draw: () => {
            return x
        }
    }
}

var nodes = []
const clickHandler = (evt) => {
    console.log(evt.x, evt.y)
    nodes.push(VisualNode(evt.x, evt.y, TreeNode("asdf")))
    console.log(nodes.map((node) => node.draw()))
}

function draw() {
    const ctx = document.getElementById('canvas').getContext('2d')
    document.getElementById('body').onclick = clickHandler
    // Sets all pixels in the rectangle defined by
    // starting point (x, y) and size (width, height)
    // to transparent black, erasing any previously drawn content.
    ctx.clearRect(0, 0, 300, 300)
    ctx.beginPath()

    // void ctx.arc(x, y, radius, startAngle (radians), endAngle, anticlockwise);
    ctx.arc(150, 150, 105, 0, Math.PI * 2, false)
    ctx.stroke()

    // start animation loop (we're not currently using it though,
    // so no further rendering will happen)
    window.requestAnimationFrame(draw)
}

function init() {
    window.requestAnimationFrame(draw)
}
