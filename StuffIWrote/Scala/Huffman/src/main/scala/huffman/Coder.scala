package huffman

import scala.collection.mutable

/** Ethan Petuchowski 12/30/15 */
object Coder {
    def encode(s: String): (String, CodeTreeInner) = {
        val tree = {
            val q = new mutable.PriorityQueue[CodeTreeNode]()
            q ++= s.groupBy(identity).mapValues(_.length).map { case (k, v) => CodeTreeLeaf(v, k) }
            if (q.size == 1) q += CodeTreeLeaf(1, ' ') // edge-case...
            while (q.size > 1) {
                val (smaller, bigger) = (q.dequeue(), q.dequeue())
                q += CodeTreeInner(smaller.freq + bigger.freq, bigger, smaller)
            }
            q.head.asInstanceOf[CodeTreeInner]
        }
        val encoder = tree.buildMap
        val encoded = (s foldLeft "")(_ + encoder(_))
        (encoded, tree)
    }
}

sealed trait CodeTreeNode extends Ordered[CodeTreeNode] {
    val freq: Int
    def compare(o: CodeTreeNode) = o.freq - freq
}

case class CodeTreeLeaf(freq: Int, letter: Char) extends CodeTreeNode

case class CodeTreeInner(freq: Int, left: CodeTreeNode, right: CodeTreeNode) extends CodeTreeNode {
    def buildMap: Map[Char, String] = {
        def sideMap(node: CodeTreeNode, sideChar: String): Map[Char, String] = node match {
            case leaf: CodeTreeLeaf => Map(leaf.letter -> sideChar)
            case inner: CodeTreeInner => inner.buildMap.mapValues(sideChar + _)
        }
        sideMap(left, "0") ++ sideMap(right, "1")
    }
}
