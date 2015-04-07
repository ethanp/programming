## Some notes

These come from the Akka docs

Actors each have independent state and behavior, so it may be helpful
to think of them as *people* that you assign tasks to.

Actor systems are hierarchies, a *parent* "supervises" its *children*.
Each actor has at most *one* parent.

* So e.g. I may want the master to be a parent and all the nodes
  to be children?
* Or I could have the master have 2 children `serverSupervisor`
  and `clientSupervisor`

The hierarchical structure helps us to clearly structure our task
in the mind and in the code.

"Watching" another actor for whether it terminates is different from
"supervising" it. When we "watch", we (under the hood) register to
receive heartbeats, using a smarter mechanism than I would ever bother
to implement on my own.

Communication between actors is "transparent", whether it is within-JVM,
across-JVM, across network, across world, etc.

Akka itself has no shared global state; we can have multiple actor systems
coexisting in one JVM.

Do not pass mutable objects or closures between actors.

The goal of an `ActorRef` is to support sending messages to the referenced
actor. An `Actor` can get an `ActorRef` to *itself* via its `self` field.
