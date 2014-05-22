/**
 * Ethan Petuchowski 5/18/14.
 * Interactive Java tree test-case generator.
 */


var width = 960,
    height = 500,
    update_interval = 350,
    max_size = 2,
    radius = 20,
    inset = radius * 2.5,
    takenValues = {},
    connections = {},
    default_elements = [];

for (var i = 0; i < max_size; i++) {
    default_elements.push(i * i * i);
}

// construct tree with all default settings except for the size
// github.com/mbostock/d3/wiki/Tree-Layout
// set layout size to be slightly smaller than width, height setting above
var tree = d3.layout.tree()
    .size([width - inset, height - inset]);

var root = { intValue: 2 },
    nodes = tree(root); // Runs the tree layout, returning the array of
                        // nodes associated with the specified root node.

takenValues[root.intValue] = true;

root.parent = root;
root.px = root.x; // I think we're saving the current values so that these
root.py = root.y; // values will still be available after the root moves

// returns a function that generates the path data for
// a cubic Bézier connecting the source and target points
var diagonal = d3.svg.diagonal();
var childVal;
// @connections: { parentValue -> { childValue } }
function collectConnections(node) {
    if (!node.children) return;
    for (var i = 0; i < node.children.length; i++) {
        childVal = node.children[i].intValue + "";  // this is (ostensibly) a unique identifier
        var connectedSet = connections[node.intValue];
        if (connectedSet && !(childVal in connectedSet)) {
            connectedSet[childVal] = true;
        }
        if (!connectedSet) {
            connections[node.intValue] = { childVal : true };
        }
        collectConnections(node.children[i]);
    }
}

// We're appending a giant svg elem to the body that will contain the entire tree
// the tree is inset, so we translate to put the tree in the middle of the inset
var svg = d3.select("body").append("svg")
        .attr("width", width)
        .attr("height", height)
    .append("g")
        .attr("transform", "translate(%d,%d)".replace(/%d/g, radius * 1.4 + ""));

// Once we have a selection, we can apply various operators to the selected elements.
// For example, we might change the fill color using style,
// and the radius and the y-position using attr
// mbostock.github.io/d3/tutorial/circle.html
var node = svg.selectAll(".node"),
    link = svg.selectAll(".link");

// run `create_initial_nodes()` every 750 ms
var timer = setInterval(create_initial_nodes, update_interval);


function create_initial_nodes() {
    if (nodes.length >= max_size) {
        // clears a timer set with the setInterval() method
        // in this case, prevents the tree from growing any bigger
        return clearInterval(timer);
    }

    // create a new node with a unique id and an intValue
    // not sure that the id is useful
    var n = {id: nodes.length, intValue: default_elements.shift() + ""},  // should initially be 1 (root)

    // we're choosing a node to be the parent
    // the `| 0` casts (quickly[!]) to Integer (stackoverflow.com/questions/596467)
        p = nodes[Math.random() * nodes.length | 0];

    takenValues[n.intValue] = true;

    // add new node to its parent's list of children
    if (p.children) {
        p.children.push(n);
    }
    else p.children = [n];

    // add new node to the tree's list of nodes
    nodes.push(n);

    uiAfterAddingNode();
}

function uiAfterAddingNode() {
    // Recompute the layout and data join.
    // not sure how this works
    node = node.data(tree.nodes(root), function (d) { return d.id; });
    link = link.data(tree.links(nodes), function (d) { return d.source.id + "-" + d.target.id; });

    // Add entering node in the parent’s old position.
    var g = node.enter();
    g.append("circle")
        .attr("class", "node")
        .attr("r", radius)
        .attr("cx", function (d) { return d.parent.px; })
        .attr("cy", function (d) { return d.parent.py; })
        .on("click", newChildDialog);

    g.append("foreignObject")
        .attr('class', 'label')
        .attr('x', function (d) { return d.x - 5 })
        .attr('y', function (d) { return d.y - radius })
        .attr('width', 50)
        .attr('height', 50)
        .html(function (d) { return d.intValue });


    // Add entering link in the parent’s old position.
    // this way it'll animate nicely to its true location
    // at the same time everything else is relocating
    link.enter().insert("path", ".node")
        .attr("class", "link")
        .attr("d", function (d) {
            var o = { x: d.source.px, y: d.source.py };
            return diagonal({ source: o, target: o });
        });

    // Transition (relocate) nodes and links to their new positions.
    var t = svg.transition()
        .duration(update_interval);

    t.selectAll(".link")
        .attr("d", diagonal);

    // store current locations so that they will be available
    // after the next time all the locations get updated
    t.selectAll(".node")
        .attr("cx", function (d) { return d.px = d.x; })
        .attr("cy", function (d) { return d.py = d.y; });

    t.selectAll(".label")
        .attr("x", function (d) { return d.px = d.x - radius * .4; })
        .attr("y", function (d) { return d.py = d.y - radius * .5; })
        .html(function (d) { return d.intValue });

}

// seems this is the preferred technical spelling of the word "dialogue"
function newChildDialog(clickedParent) {
    var val = prompt("Please enter the new node's intValue, it must make sense too!");
    if (!val || val.search(/^-?[1-9][0-9]*$/) == -1 || val in takenValues) return;
    var newNode = {id: nodes.length, intValue: val};
    if (clickedParent.children) {
        if (clickedParent.children.length == 2) return;
        takenValues[val] = true;
        clickedParent.children.push(newNode);
        clickedParent.children.sort(function (a, b) { return a.intValue - b.intValue });
    }
    else clickedParent.children = [newNode];
    nodes.push(newNode);
    collectConnections(root);
    uiAfterAddingNode();
}

searchTree = false;
/**
 * The Java Generation
 * TODO 1: Case for BinarySearchTree generation.
 * TODO 2: Case for if node value is negative, then node variable name can't be e.g. node-43.
 * TODO 3: For some reason node0 is showing up twice when there are nodes hanging to the right of the root node2.
 * TODO 4: the nodes should be added in depth order, at this point they are not.
 */
document.getElementById("generate").onclick = function () {
    if (!searchTree) {
        var text = "public static BinaryTree gend() {<br>";
        text += "    BinaryTree bt = new BinaryTree(new BinaryTreeNode&lt;Integer&gt;(2));<br>";
        text += "    BinaryTreeNode&lt;Integer&gt; node2 = bt.getRoot();<br>";
        text += "    BinaryTreeNode&lt;Integer&gt; node0 = new BinaryTreeNode&lt;Integer&gt;(0);<br>"
        text += "    node2.setLeft(node0);<br>"
        // @connections: { parentValue -> { childValue } }
        for (var parent in connections) {
            for (var child in connections[parent]) {
                if (child == "childVal" || parent == "childVal") continue;
                var dir = parseInt(child) < parseInt(parent) ? "Left" : "Right";
                text += "    BinaryTreeNode&lt;Integer&gt; node" + child + " = new BinaryTreeNode&lt;Integer&gt;(" + child + ");<br>";
                text += "    node" + parent + ".set" + dir + "(node"+child+");<br>"
            }
        }
        text += "    return bt;<br>";
        text += "}";
        document.getElementById("code").innerHTML = text;
    } else {
        // TODO!!
    }
};
