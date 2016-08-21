const kefir = require('kefir')
const EventEmitter = require('events')

/* TODO
* make it interact with kefir
* make it a node module e.g. in npm
*/

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
