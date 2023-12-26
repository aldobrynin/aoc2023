package day24

import common.Puzzle
import common.Rational
import common.V3
import solveAndVerify
import swap

private data class Hailstone(val pos: V3, val vel: V3){
    operator fun minus(other: Hailstone): Hailstone = Hailstone(pos - other.pos, vel - other.vel)
}
private typealias ParsedInput = List<Hailstone>

private class Day24 : Puzzle<ParsedInput>(day = 24) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.split(" @ ") }
        .map { Hailstone(V3.parse(it[0]), V3.parse(it[1])) }

    override fun part1(input: ParsedInput): Long = solve1(input, 200000000000000.0, 400000000000000.0)

    fun solve1(input: ParsedInput, min: Double, max: Double): Long {
        fun getLinearCoefficients(stone: Hailstone): Pair<Double, Double> {
            val a = stone.vel.y / stone.vel.x
            val c = stone.pos.y - a * stone.pos.x
            return a to c
        }

        fun getTimeAtPos(pos: Pair<Double, Double>, hailstone: Hailstone): Double {
            return (pos.first - hailstone.pos.x) / hailstone.vel.x
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
        val (t1, p1) = solve(input[0], input[1], input[2])
        val (t2, p2) = solve(input[0], input[1], input[3])
        val v = (p2 - p1) / (t2 - t1)
        val p = p1 - v * t1
        return (p.x + p.y + p.z).toLong()
    }

    private fun solve(zero: Hailstone, one: Hailstone, two: Hailstone): Pair<Long, V3> {
        val (p1, v1) = one - zero
        val (p2, v2) = two - zero

        val coordinates: Array<(V3) -> Double> = arrayOf({ it.x }, { it.y }, { it.z })
        val vectors = listOf(p1, v1, -v2, p2)
        val matrix = coordinates.map { c -> vectors.map { Rational.from(c(it)) }.toTypedArray() }.toTypedArray()

        val (_, _, t) = solveGaussian(matrix)
        val pos = two.pos + two.vel * t.toLong()
        return t.toLong() to pos
    }

    fun solveGaussian(matrix: Array<Array<Rational>>): Array<Rational> {
        val m = matrix.size
        for (k in matrix.indices) {
            val pivotIndex = (k..<m).maxBy { matrix[it][k].abs() }
            matrix.swap(k, pivotIndex)
            for (i in k + 1 until m) {
                val factor = matrix[i][k] / matrix[k][k]
                for (j in k + 1..m) matrix[i][j] -= factor * matrix[k][j]
                matrix[i][k] = Rational.zero
            }
        }
        val result = Array(m) { Rational.zero }
        for (i in m - 1 downTo 0) {
            result[i] = (matrix[i][m] - ((i + 1)..<m).sumOf { result[it] * matrix[i][it] }) / matrix[i][i]
        }
        return result
    }
}

private fun <T> Iterable<T>.sumOf(f: (T) -> Rational): Rational = this.fold(Rational.zero) { acc, cur -> acc + f(cur) }
private fun <E> List<E>.pairs(): List<Pair<E,E>> = indices.flatMap { i -> (i + 1..lastIndex).map { j -> this[i] to this[j] } }

fun main() {
    val puzzle = Day24()

    solveAndVerify({ puzzle.solve1(puzzle.parse("sample.txt"), 7.0, 27.0) }, expected = 2)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 47)

    puzzle.run()
}