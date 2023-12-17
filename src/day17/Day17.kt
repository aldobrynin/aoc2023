package day17

import common.Puzzle
import common.V
import solveAndVerify
import java.util.*

private typealias Grid = List<List<Int>>
private typealias ParsedInput = Grid

private data class State(val pos: V, val dir: V, val steps: Int)

private class Day17 : Puzzle<ParsedInput>(day = 17) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { line -> line.map { it - '0' } }

    override fun part1(input: ParsedInput): Long = findShortestPath(input)

    override fun part2(input: ParsedInput): Long = findShortestPath(input, minSteps = 4, maxSteps = 10)

    fun findShortestPath(grid: Grid, minSteps: Int = 1, maxSteps: Int = 3): Long {
        val initialState = State(V.Zero, V.Zero, 0)
        val end = V(grid[0].lastIndex, grid.lastIndex)
        val visited = mutableSetOf<State>()
        val queue = PriorityQueue<Pair<State, Long>>(compareBy { it.second })
        queue.add(initialState to 0)
        visited.add(initialState)

        while (queue.isNotEmpty()) {
            val (state, distance) = queue.poll()
            if (state.pos == end && state.steps >= minSteps) return distance

            val canTurn = state.dir == V.Zero || state.steps >= minSteps
            nextStates(state)
                .filter { it.pos.belongsTo(grid) && it.steps <= maxSteps }
                .filter { it.dir == state.dir || canTurn }
                .filter { visited.add(it) }
                .forEach { queue.add(it to (distance + grid.at(it.pos))) }
        }
        throw Exception("No path found")
    }

    fun nextStates(state: State) = V.dir4()
        .asSequence()
        .filterNot { it == -state.dir }
        .map { State(state.pos + it, it, (if (state.dir == it) state.steps else 0) + 1) }

    fun Grid.at(pos: V): Int = this[pos.y][pos.x]
}

fun main() {
    val puzzle = Day17()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 102)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 94)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample2.txt")) }, expected = 71)

    puzzle.run()
}