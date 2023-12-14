package day14

import common.Puzzle
import common.V
import solveAndVerify

private typealias Grid = Array<CharArray>
private typealias ParsedInput = Grid

private class Day14 : Puzzle<ParsedInput>(day = 14) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toCharArray() }.toTypedArray()

    override fun part1(input: ParsedInput): Long {
        tiltOnce(input, V.Up)
        return input.currentLoad()
    }

    override fun part2(input: ParsedInput): Long {
        val directions = listOf(V.Up, V.Left, V.Down, V.Right)

        val cycles = 1000000000
        val visited = mutableMapOf<Int, Int>()
        val history = mutableListOf<Long>()

        for (i in 0..1000) {
            history.add(input.currentLoad())
            val cycleStart = visited.putIfAbsent(input.getStateKey(), i)
            if (cycleStart != null) {
                val period = i - cycleStart
                val remaining = (cycles - i) % period
                return history[cycleStart + remaining]
            }
            directions.forEach { tiltOnce(input, it) }
        }

        throw Exception("No cycle found")
    }

    val order = mapOf(
        V.Up to Comparator.comparingInt(V::y),
        V.Left to Comparator.comparingInt(V::x),
        V.Down to Comparator.comparingInt(V::y).reversed(),
        V.Right to Comparator.comparingInt(V::x).reversed()
    )

    fun tiltOnce(grid: Grid, direction: V) {
        for (coordinate in grid.coordinates(direction)) {
            var next = coordinate
            while (canMove(grid, next, direction)) next += direction
            grid.swap(next, coordinate)
        }
    }

    fun canMove(grid: Grid, coordinate: V, direction: V): Boolean {
        val next = (coordinate + direction)
        return grid.indices.contains(next.y) && grid[0].indices.contains(next.x) && grid.at(next) == '.'
    }

    fun Grid.currentLoad(): Long = this
        .flatMapIndexed { rowIndex, row -> row.map { if (it == 'O') this.size - rowIndex else 0 } }
        .sum().toLong()

    fun Grid.at(v: V): Char = this[v.y][v.x]
    fun Grid.swap(v1: V, v2: V) {
        val tmp = this[v1.y][v1.x]
        this[v1.y][v1.x] = this[v2.y][v2.x]
        this[v2.y][v2.x] = tmp
    }

    fun Grid.getStateKey(): Int =
        this.indices.sumOf { row -> this[row].indices.sumOf { col -> this[row][col].hashCode() * row * col } }

    fun Grid.coordinates(dir: V): Iterable<V> =
        this.indices.flatMap { row -> this[row].indices.map { V(it, row) } }
            .filter { this.at(it) == 'O' }
            .sortedWith(order[dir]!!)
}

fun main() {
    val puzzle = Day14()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 136)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 64)

    puzzle.run()
}