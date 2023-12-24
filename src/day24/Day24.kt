package day24

import common.Puzzle
import solveAndVerify

private data class V3(val x: Long, val y: Long, val z: Long) {
    companion object {
        fun parse(string: String): V3 {
            val (x, y, z) = string.split(",").map { it.trim() }.map { it.toLong() }
            return V3(x, y, z)
        }
    }
}
private data class Hailstone(val pos: V3, val vel: V3)
private typealias ParsedInput = List<Hailstone>

private class Day24 : Puzzle<ParsedInput>(day = 24) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.split(" @ ") }
        .map { Hailstone(V3.parse(it[0]), V3.parse(it[1])) }

    override fun part1(input: ParsedInput): Long = solve1(input,200000000000000.0, 400000000000000.0)

    fun solve1(input: ParsedInput, min: Double, max: Double): Long {
        fun getLinearCoefficients(stone: Hailstone): Pair<Double, Double> {
            val a = stone.vel.y.toDouble() / stone.vel.x.toDouble()
            val c = stone.pos.y.toDouble() - a * stone.pos.x.toDouble()
            return a to c
        }

        fun getTimeAtPos(pos: Pair<Double, Double>, hailstone: Hailstone): Double {
            return (pos.first - hailstone.pos.x) / hailstone.vel.x.toDouble()
        }

        fun isInRange(pos: Pair<Double, Double>): Boolean = pos.first in min..max && pos.second in min..max

        fun getIntersectionPoint(a: Hailstone, b: Hailstone): Pair<Double, Double> {
            val (a1, c1) = getLinearCoefficients(a)
            val (a2, c2) = getLinearCoefficients(b)
            val x = (c2 - c1) / (a1 - a2)
            val y = a1 * x + c1
            return x to y
        }

        return input.pairs()
            .count { (a, b) ->
                val intersection = getIntersectionPoint(a, b)
                (isInRange(intersection) && getTimeAtPos(intersection, a) > 0 && getTimeAtPos(intersection, b) > 0)
            }.toLong()
    }

    override fun part2(input: ParsedInput): Long {
        return 0
    }
}

private fun <E> List<E>.pairs(): List<Pair<E,E>> {
    return indices.flatMap { i -> (i + 1..lastIndex).map { j -> this[i] to this[j] } }
}

fun main() {
    val puzzle = Day24()

    solveAndVerify({ puzzle.solve1(puzzle.parse("sample.txt"), 7.0, 27.0) }, expected = 2)
//    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 0)

    puzzle.run()
}