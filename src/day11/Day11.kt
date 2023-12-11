package day11

import columns
import common.Puzzle
import common.V
import rows
import solveAndVerify
import kotlin.math.abs

private typealias ParsedInput = List<List<Char>>

private class Day11 : Puzzle<ParsedInput>(day = 11) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }

    override fun part1(input: ParsedInput): Long = calculateDistances(input, 2)

    override fun part2(input: ParsedInput): Long = calculateDistances(input, 1_000_000)

    private fun calculateDistances(map: ParsedInput, factor: Long): Long {
        val emptyRowCounts = map.rows().runningFold(0) { acc, row -> acc + if (row.all { it == '.' }) 1 else 0 }
        val emptyColCounts = map.columns().runningFold(0) { acc, col -> acc + if (col.all { it == '.' }) 1 else 0 }

        val galaxies = map.flatMapIndexed { rowIndex, row -> row.indices.map { colIndex -> V(colIndex, rowIndex) } }
            .filter { map[it.y][it.x] == '#' }

        return galaxies.indices.flatMap { index ->
            galaxies.indices.drop(index + 1).map { galaxies[index] to galaxies[it] }
        }.sumOf {
            val (first, second) = it
            val extraX = (factor - 1) * (emptyColCounts[first.x] - emptyColCounts[second.x])
            val extraY = (factor - 1) * (emptyRowCounts[first.y] - emptyRowCounts[second.y])
            abs(first.x - second.x + extraX) + abs(first.y - second.y + extraY)
        }
    }
}

fun main() {
    val puzzle = Day11()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 374L)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 82000210L)

    puzzle.run()
}