package day22

import common.Puzzle
import common.V
import solveAndVerify
import toIntArray

private data class Point3(val x: Int, val y: Int, val z: Int)

private data class Brick(val min: Point3, val max: Point3) {
    var bricksBelow = mutableSetOf<Brick>()
    var bricksAbove = mutableSetOf<Brick>()
    companion object {
        fun parse(string: String): Brick {
            val points = string.split('~').map { it.toIntArray(",") }.map { Point3(it[0], it[1], it[2]) }
            return Brick(points[0], points[1])
        }
    }

    fun addBrickBelow(brick: Brick): Boolean = bricksBelow.add(brick) && brick.bricksAbove.add(this)

    fun pointsAtXY(): List<V> = (min.y..max.y).flatMap { y -> (min.x..max.x).map { V(it, y) } }
}
private typealias ParsedInput = List<Brick>

private class Day22 : Puzzle<ParsedInput>(day = 22) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { Brick.parse(it) }

    override fun part1(input: ParsedInput): Long {
        dropBricksAndSetDependencies(input)

        fun canRemove(brick: Brick): Boolean {
            return brick.bricksAbove.count { it.bricksBelow.size > 1 } == brick.bricksAbove.size
        }

        return input.count(::canRemove).toLong()
    }

    override fun part2(input: ParsedInput): Long {
        dropBricksAndSetDependencies(input)
        val sortedBricks = input.sortedBy { it.min.z }

        fun getFallenCount(brick: Brick): Int {
            val fallen = mutableSetOf(brick)
            for (next in sortedBricks) {
                if (next.bricksBelow.isNotEmpty() && fallen.containsAll(next.bricksBelow)) {
                    fallen.add(next)
                }
            }
            return fallen.size - 1
        }

        return sortedBricks.sumOf(::getFallenCount).toLong()
    }

    private fun dropBricksAndSetDependencies(input: ParsedInput) {
        val bricksMap = input.withIndex().associate { it.index to it.value }

        val minHeights = mutableMapOf<V, Pair<Int, Int>>()
        for ((index, brick) in bricksMap.entries.sortedBy { it.value.min.z }) {
            val finalMinZ = brick.pointsAtXY().maxOf { minHeights.getOrDefault(it, 0 to 0).first } + 1
            val finalMaxZ = brick.max.z - (brick.min.z - finalMinZ)

            for (v in brick.pointsAtXY()) {
                val current = minHeights[v]
                if (current != null && finalMinZ == current.first + 1) {
                    brick.addBrickBelow(bricksMap.getValue(current.second))
                }
                minHeights[v] = finalMaxZ to index
            }
        }
    }
}

fun main() {
    val puzzle = Day22()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 5)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 7)

    puzzle.run()
}