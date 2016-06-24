package ethanp.treedom

import scala.collection.mutable
import scala.util.Random

/**
  * Ethan Petuchowski
  * 6/23/16
  */
object Treedom extends App {
    private val tree = TreeStuff.generateTree5050(10)
    println(tree)
    println("\n\n")
    println("height: " + tree.height)
}

trait Node[NodeType] {
    def left: Option[Node[NodeType]]

    def value: NodeType

    def right: Option[Node[NodeType]]

    def children: List[Node[NodeType]] = List(left, right).flatten

    def height: Int = children.map(_.height + 1).+:(1).max
}

case class MutableNode[NodeType](
    var left: Option[MutableNode[NodeType]],
    value: NodeType,
    var right: Option[MutableNode[NodeType]]
) extends Node[NodeType] {
    override def toString: String = toStringMaker(level = 0)

    val indent = "    "

    private def toStringMaker(level: Int): String = {
        val l: Option[String] = left.map(_.toStringMaker(level + 1) + ",\n")
        val center: Option[String] = Some(("   " * level) + value.toString)
        val r: Option[String] = right.map(",\n" + _.toStringMaker(level + 1))
        (l ++ center ++ r).mkString
    }
}

object TreeStuff {
    def basicTree: MutableNode[Int] = {
        val innerBranch = Some(MutableNode(None, 5, None))
        val outerBranch = MutableNode(innerBranch, 4, None)
        val tree = MutableNode(None, 1, Some(outerBranch))
        tree
    }

    def generateTree5050(numNodes: Int): MutableNode[Int] = {
        //        val numbers: Iterator[Int] = (Random shuffle (0 until numNodes).toVector).iterator
        val numbers: Iterator[Int] = (0 until numNodes).iterator
        val root = MutableNode(None, numbers.next(), None)
        val frontier = new mutable.ArrayBuffer[MutableNode[Int]] {
            /* O(1) random removal when we don't care about maintaining order.
             * Looks like monkey-patching is possible, but only in the best way!
             * Try telling me this language isn't awesome. */
            def swapOut(idx: Int): MutableNode[Int] = {
                val tmp = this (idx)
                this (idx) = this.last
                this -= tmp
                tmp
            }
        }
        frontier += root
        for (i ← 1 /*already have root*/ until numNodes) {
            val node = MutableNode(None, numbers.next(), None)
            val index = Random.nextInt(frontier.size)
            val atIdx = frontier(index)
            if (atIdx.left.isEmpty && atIdx.right.isEmpty)
                if (Random.nextBoolean()) atIdx.left = Some(node) else atIdx.right = Some(node)
            else {
                if (atIdx.left.isEmpty) atIdx.left = Some(node) else atIdx.right = Some(node)
                frontier.swapOut(index)
            }
            frontier += node
        }
        root
    }
}
