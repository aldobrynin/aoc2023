package day10

import common.Puzzle
import common.V
import solveAndVerify

private typealias ParsedInput = List<List<Char>>

private class Day10 : Puzzle<ParsedInput>(day = 10) {
    override fun parse(rawInput: List<String>): ParsedInput = rawInput.map { it.toList() }
    val possibleMoves = mapOf(
        '|' to listOf(V.Up, V.Down), '-' to listOf(V.Left, V.Right),
        'L' to listOf(V.Up, V.Right), '7' to listOf(V.Down, V.Left),
        'J' to listOf(V.Up, V.Left), 'F' to listOf(V.Down, V.Right),
        'S' to listOf(V.Up, V.Down, V.Left, V.Right), '.' to emptyList()
    )

    val canMove: (from: Char, to: Char, dir: V) -> Boolean =
        { from, to, dir -> possibleMoves[from]!!.contains(dir) && possibleMoves[to]!!.contains(-dir) }

    override fun part1(input: ParsedInput): Long {
        val (_, _, pipe) = findStartTileAndPipe(input)
        return (pipe.size / 2L)
    }

    override fun part2(input: ParsedInput): Long {
        val (start, tile, pipe) = findStartTileAndPipe(input)

        var count = 0L
        for (y in input.indices) {
            var doubleCrossed = input[y].size * 10
            for (x in input[y].indices) {
                val v = V(x, y)
                if (pipe.contains(v)) {
                    doubleCrossed += when (if (start == v) tile else input[y][x]) {
                        '|' -> 2
                        'L' -> 1
                        'J' -> -1
                        '7' -> 1
                        'F' -> -1
                        else -> 0
                    }
                } else count += (doubleCrossed / 2 % 2)
            }
        }
        return count
    }

    private fun findStartTileAndPipe(map: ParsedInput): Triple<V, Char, Set<V>> {
        val start = map.flatMapIndexed { y, row -> row.mapIndexed { x, c -> V(x, y) to c } }
            .first { it.second == 'S' }
            .first

        val neighbors = start.area4(map)
        val startTiles = possibleMoves.keys.filter { tile ->
            tile != 'S' && neighbors.count { to -> canMove(tile, map[to.y][to.x], to - start) } == 2
        }

        return startTiles.map { Triple(start, it, findLoop(map, start, it)) }.single { it.third.isNotEmpty() }
    }

    private fun findLoop(map: ParsedInput, start: V, startTile: Char): Set<V> {
        val (first, second) = possibleMoves[startTile]!!
        val (loopStart, loopEnd) = first + start to second + start

        val visited = mutableSetOf(start, loopStart)
        var current = loopStart
        while (current != loopEnd) {
            val next = current.area4(map)
                .filter { canMove(map[current.y][current.x], map[it.y][it.x], it - current) }
                .singleOrNull { visited.add(it) }
            if (next == null) return emptySet()
            current = next
        }
        return visited
    }
}

fun main() {
    val puzzle = Day10()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 80)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 10)

    puzzle.run()
}