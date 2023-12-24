package day21

import common.Puzzle
import common.V
import coordinates
import solveAndVerify

private typealias Grid = List<List<Char>>
private typealias ParsedInput = Grid

private class Day21 : Puzzle<ParsedInput>(day = 21) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }

    override fun part1(input: ParsedInput): Long = solve(input, steps = 64)

    override fun part2(input: ParsedInput): Long {
        val steps = 26501365
        val tileSize = input.size
        val grids = steps / tileSize
        val remainingSteps = steps % tileSize
        val stepsToSimulate = 2 * tileSize + remainingSteps

        val sequence = countTiles(input, stepsToSimulate)
            .filterIndexed { index, _ -> index % tileSize == remainingSteps }
            .toList()

        val (a, b, c) = findQuadraticEquationCoefficients(sequence)
        return a * grids * grids + b * grids + c
    }

    fun solve(input: ParsedInput, steps: Int): Long {
        return countTiles(input, steps).last()
    }

    // f(x) = a*x^2 + b*x + c
    // f(0) = a*0 + b*0 + c = f0
    // f(1) = a*1 + b*1 + c = f1
    // f(2) = a*4 + b*2 + c = f2
    // c = f0
    // a = (f2 - 2*f1 + c)/2
    // b = f1 - c - a
    fun findQuadraticEquationCoefficients(sequence: List<Long>): Triple<Long, Long, Long> {
        val c = sequence[0]
        val a = (sequence[2] - 2 * sequence[1] + c) / 2
        val b = sequence[1] - c - a
        return Triple(a, b, c)
    }

    private fun countTiles(map: Grid, steps: Int): Sequence<Long> {
        val start = map.coordinates().first { map.at(it) == 'S' }
        var current = setOf(start)
        return sequence {
            yield(current.size.toLong())
            repeat(steps) {
                current = current.flatMap { it.neighbors4().filter { next -> map.at(next) != '#' } }.toSet()
                yield(current.size.toLong())
            }
        }
    }

    private fun Int.eulerModulo(mod: Int): Int = ((this % mod) + mod) % mod

    private fun <E> List<List<E>>.at(v: V): E = this[v.y.eulerModulo(this.size)][v.x.eulerModulo(this.first().size)]
}

fun main() {
    val puzzle = Day21()

    solveAndVerify({ puzzle.solve(puzzle.parse("sample.txt"), steps = 6) }, expected = 16)
//    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 0)

    puzzle.run()
}