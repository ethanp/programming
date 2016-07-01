function init() {
    window.requestAnimationFrame(draw)
}

const TreeNode = (value) => {
    return {
        draw: () => {
            return value
        }
    }
}

const root = TreeNode(6)
console.log(root.draw())

function draw() {
    const ctx = document.getElementById('canvas').getContext('2d')
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
