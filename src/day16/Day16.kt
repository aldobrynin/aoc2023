package day16

import common.Puzzle
import common.V
import solveAndVerify

private typealias Grid = List<List<Char>>
private typealias ParsedInput = Grid

private class Day16 : Puzzle<ParsedInput>(day = 16) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }

    override fun part1(input: ParsedInput): Long = count(input, V(0, 0), V.Right)

    override fun part2(input: ParsedInput): Long = listOf(
        V.Right to input.indices.map { row -> V(0, row) },
        V.Left to input.indices.map { row -> V(input[0].lastIndex, row) },
        V.Down to input[0].indices.map { col -> V(col, 0) },
        V.Up to input[0].indices.map { col -> V(col, input.lastIndex) }
    )
        .flatMap { (dir, starts) -> starts.map { it to dir } }
        .maxOf { (start, dir) -> count(input, start, dir) }

    fun count(grid: Grid, start: V, dir: V): Long {
        var current = listOf(start to dir)
        val visited = mutableSetOf(start to dir)
        while (current.isNotEmpty()) {
            current = current.flatMap { (pos, dir) ->
                nextDirections(grid[pos.y][pos.x], dir)
                    .map { nextDir -> pos + nextDir to nextDir }
                    .filter { it.first.belongsTo(grid) && visited.add(it) }
            }
        }

        return visited.distinctBy { it.first }.size.toLong()
    }

    fun nextDirections(char: Char, dir: V): List<V> {
        return when (char) {
            '.' -> listOf(dir)
            '|' -> if (dir.x == 0) listOf(dir) else listOf(V.Up, V.Down)
            '-' -> if (dir.y == 0) listOf(dir) else listOf(V.Left, V.Right)
            '/' -> listOf(reflect[dir]!!)
            '\\' -> listOf(reflect[dir]!! * -1)
            else -> throw Exception("Invalid char $char")
        }
    }

    val reflect = mapOf(V.Up to V.Right, V.Right to V.Up, V.Down to V.Left, V.Left to V.Down)
}

fun main() {
    val puzzle = Day16()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 46)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 51)

    puzzle.run()
}