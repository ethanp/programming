package streams

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import Bloxorz._  // TODO this is here so that I can *choose* to use it for better testing

@RunWith(classOf[JUnitRunner])
class BloxorzSuite extends FunSuite {

  trait SolutionChecker extends GameDef with Solver with StringParserTerrain {
    /**
     * This method applies a list of moves `ls` to the block at position
     * `startPos`. This can be used to verify if a certain list of moves
     * is a valid solution, i.e. leads to the goal.
     */
    def solve(ls: List[Move]): Block =
      ls.foldLeft(startBlock) { case (block, move) => move match {
        case Left => block.left
        case Right => block.right
        case Up => block.up
        case Down => block.down
      }
    }
  }

  trait Level1 extends SolutionChecker {
      /* terrain for level 1*/

    val level =
    """ooo-------
      |oSoooo----
      |ooooooooo-
      |-ooooooooo
      |-----ooToo
      |------ooo-""".stripMargin

    val optsolution = List(Right, Right, Down, Right, Right, Right, Down)
  }

  trait Level0 extends SolutionChecker {
    val level =
      """------
        |--ST--
        |--oo--
        |--oo--
        |------""".stripMargin

    val optsolution = List(Down, Right, Up)
  }

  trait Level6 extends SolutionChecker {
    val level =
      """-----oooooo
        |-----o--ooo
        |-----o--ooooo
        |Sooooo-----oooo
        |----ooo----ooTo
        |----ooo-----ooo
        |------o--oo
        |------ooooo
        |------ooooo
        |-------ooo""".stripMargin

    val optsolution = List(
      Right, Right, Right, Down, Right, Down, Down,Right, Down, Down,
      Right, Up, Left, Left, Left, Up, Up, Left, Up, Up, Up, Right,
      Right, Right, Down, Down, Left, Up, Right, Right, Down, Right,
      Down, Down, Right)
  }

  trait LevelUnsolvable extends SolutionChecker {
    val level =
      """------
        |--ST--
        |--oo--
        |--o---
        |------""".stripMargin

    val optsolution = Nil
  }

  test("terrain function level 1") {
    new Level1 {
      assert(terrain(Pos(0,0)), "0,0")
      assert(!terrain(Pos(4,11)), "4,11")
    }
  }

  test("findChar level 1") {
    new Level1 {
      assert(startPos == Pos(1,1))
    }
  }

  test("neighborsWithHistory level 1") {
    new Level1 {
      assert(neighborsWithHistory(
                Block(Pos(1,1),Pos(1,1)), List(Left,Up)).toSet
        == Set((Block(Pos(1,2),Pos(1,3)), List(Right,Left,Up)),
               (Block(Pos(2,1),Pos(3,1)), List(Down,Left,Up))))
    }
  }

  test("newNeighborsOnly level 1") {
    new Level1 {
      newNeighborsOnly(
        Set(
          (Block(Pos(1,2),Pos(1,3)), List(Right,Left,Up)),  // should get filtered out
          (Block(Pos(2,1),Pos(3,1)), List(Down,Left,Up))).toStream,
        Set(
          Block(Pos(1,2),Pos(1,3)),
          Block(Pos(1,1),Pos(1,1)))
      ) ==
        Set(
          (Block(Pos(2,1),Pos(3,1)), List(Down,Left,Up))).toStream
    }
  }

  test("optimal solution for level 1") {
    new Level1 {
      val a = solution
      val b = solve(a)
      assert(solve(solution) == Block(goal, goal))
    }
  }

  test("optimal solution length for level 1") {
    new Level1 {
      assert(solution.length == optsolution.length)
    }
  }

  test("optimal solution for level 0") {
    new Level0 {
      assert(solve(solution) == Block(goal, goal))
      assert(solution == optsolution) // this one definitely only has one solution
    }
  }

  test("solution length for level 6") {
    new Level6 {
      println("Solution: " + solution)
      assert(solution.length == optsolution.length)
      if (solution == optsolution) println("Found the example solution")
      else println("Found something other than the example solution")
    }
  }

  test("solution for unsolvable level should be Nil") {
    new LevelUnsolvable {
      assert(solution.length == optsolution.length)
    }
  }
}
