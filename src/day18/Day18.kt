package day18

import common.Puzzle
import common.V
import solveAndVerify
import kotlin.math.absoluteValue

private typealias ParsedInput = List<String>

private class Day18 : Puzzle<ParsedInput>(day = 18) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput

    override fun part1(input: ParsedInput): Long {
        val compassToDir = mapOf("U" to V.Up, "D" to V.Down, "L" to V.Left, "R" to V.Right)
        val moves = input.map { it.split(" ") }
            .map { (dir, len) -> compassToDir[dir]!! * len.toInt() }

        return solve(moves)
    }

    override fun part2(input: ParsedInput): Long {
        val dirs = arrayOf(V.Right, V.Down, V.Left, V.Up)
        val moves = input.map { it.split(" ")[2].trim('(', ')', '#').chunked(5) }
            .map { (len, dir) -> dirs[dir.toInt()] * len.toInt(16) }
        return solve(moves)
    }

    private fun solve(moves: List<V>): Long {
        val polygon = moves.runningFold(V.Zero) { acc, move -> acc + move }
        val perimeter = polygon.zipWithNext { a, b -> a.distanceTo(b).toLong() }.sum()
        return area(polygon) + perimeter / 2 + 1
    }

    fun area(polygon: List<V>): Long =
        polygon.zipWithNext { a, b -> 1L * a.x * b.y - 1L * b.x * a.y }.sum().absoluteValue / 2
}

fun main() {
    val puzzle = Day18()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 62)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 952408144115L)

    puzzle.run()
}