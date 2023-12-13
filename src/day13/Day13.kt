package day13

import common.Puzzle
import solveAndVerify
import kotlin.math.min

private typealias Grid = List<List<Char>>
private typealias ParsedInput = List<Grid>

private class Day13 : Puzzle<ParsedInput>(day = 13) {
    override fun parse(rawInput: List<String>): ParsedInput =
        rawInput.fold(mutableListOf(mutableListOf<List<Char>>())) { acc, line ->
            if (line.isBlank()) acc.add(mutableListOf())
            else acc[acc.lastIndex].add(line.toList())
            acc
        }

    override fun part1(input: ParsedInput) =
        input.sumOf { findReflectionLine(it, 0) * 100L + findReflectionLine(it.transpose(), 0) }

    override fun part2(input: ParsedInput) =
        input.sumOf { findReflectionLine(it, 1) * 100L + findReflectionLine(it.transpose(), 1) }

    fun findReflectionLine(grid: Grid, smudgesCount: Int) =
        grid.indices.drop(1).firstOrNull { isReflectionLine(grid, it, smudgesCount) } ?: 0

    fun isReflectionLine(grid: Grid, lineIndex: Int, smudgesCount: Int): Boolean =
        0.rangeUntil(min(lineIndex, grid.size - lineIndex))
            .sumOf { mirrorSize -> countDiff(grid, lineIndex - mirrorSize - 1, lineIndex + mirrorSize) } == smudgesCount

    fun countDiff(grid: Grid, ind1: Int, ind2: Int) = grid[ind1].indices.count { grid[ind1][it] != grid[ind2][it] }

    fun Grid.transpose(): Grid = this.first().indices.map { col -> this.indices.map { row -> this[row][col] } }
}

fun main() {
    val puzzle = Day13()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 405)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 400)

    puzzle.run()
}