package day23

import common.Puzzle
import common.V
import coordinates
import solveAndVerify
import kotlin.math.max

private typealias Grid = List<List<Char>>
private typealias ParsedInput = Grid

private class Day23 : Puzzle<ParsedInput>(day = 23) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }

    override fun part1(input: ParsedInput): Long {
        val start = V(input.first().indexOf('.'), 0)
        val end = V(input.last().indexOf('.'), input.lastIndex)

        fun getMovesWithSlopes(current: V, map: Grid): List<Pair<V, Long>> {
            if (map.at(current) == '.') return getMoves(current, map)
            return listOf(current + map.at(current).toDirection() to 1L)
        }

        return findLongestPath(start, end) { v -> getMovesWithSlopes(v, input) }
    }

    override fun part2(input: ParsedInput): Long {
        val start = V(input.first().indexOf('.'), 0)
        val end = V(input.last().indexOf('.'), input.lastIndex)
        val adjMap = buildAdjacencyMap(start, end, input)

        return findLongestPath(start, end) { adjMap[it] ?: emptyList() }
    }

    fun buildAdjacencyMap(start: V, end: V, map: Grid): Map<V, List<Pair<V, Long>>> {
        val crossroads = map.coordinates().asSequence()
            .filter { map.at(it) != '#' }
            .filter { it == start || it == end || getMoves(it, map).size > 2 }
            .toSet()

        return crossroads.associateWith { findPathToOtherCrossroads(it, crossroads, map) }
    }

    private fun findPathToOtherCrossroads(crossroad: V,crossroads: Set<V>, map: Grid): List<Pair<V, Long>> {
        val res = mutableListOf<Pair<V, Long>>()
        bfs(crossroad,
            { if (it == crossroad || it !in crossroads) getMoves(it, map) else emptyList() },
            { (node, distance) -> if (node != crossroad && node in crossroads) res.add(node to distance) })
        return res
    }

    fun bfs(start: V, getMoves: (V) -> List<Pair<V, Long>>, onVisit: (Pair<V, Long>) -> Unit) {
        val queue = ArrayDeque<Pair<V, Long>>()
        queue.add(start to 0L)
        val visited = mutableSetOf(start)
        while (queue.isNotEmpty()) {
            val element = queue.removeFirst()
            onVisit(element)
            for ((next, nextDistance) in getMoves(element.first).filter { visited.add(it.first) }) {
                queue.add(next to element.second + nextDistance)
            }
        }
    }

    fun findLongestPath(from: V, to: V, getMoves: (V) -> List<Pair<V, Long>>): Long {
        fun dfs(current: V, visited: MutableSet<V>): Long {
            if (current == to) return 0

            var res = Long.MIN_VALUE
            for ((next, distance) in getMoves(current)) {
                if (!visited.add(next)) continue
                res = max(res, dfs(next, visited) + distance)
                visited.remove(next)
            }
            return res
        }
        return dfs(from, mutableSetOf())
    }

    fun getMoves(current: V, map: Grid): List<Pair<V, Long>> = current.area4(map).filter { map.at(it) != '#' }.map { it to 1 }

    fun Grid.at(point: V): Char = this[point.y][point.x]

    fun Char.toDirection(): V = when (this) {
        '^' -> V.Up
        'v' -> V.Down
        '<' -> V.Left
        '>' -> V.Right
        else -> throw Exception("Unknown arrow $this")
    }
}

fun main() {
    val puzzle = Day23()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 94)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 154)

    puzzle.run()
}