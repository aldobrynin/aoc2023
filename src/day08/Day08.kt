package day08

import common.Puzzle
import solveAndVerify
import java.math.BigInteger

private class Game(val directions: String, val network: Map<String, Pair<String, String>>) {
    companion object {
        fun parse(rawInput: List<String>): Game {
            val directions = rawInput.first()
            val network = rawInput.drop(n = 2).map { line ->
                    line.split(' ', '=', '(', ')', ',').filter { it.isNotBlank() }
                }.associateBy({ it[0] }, { Pair(it[1], it[2]) })
            return Game(directions, network)
        }
    }

    fun getNextNode(current: String, step: Long): String {
        val (left, right) = network.getValue(current)
        val direction = directions[(step % directions.length).toInt()]
        return if (direction == 'L') left else right
    }
}
private typealias ParsedInput = Game

private class Day08 : Puzzle<ParsedInput>(day = 8) {
    override fun parse(rawInput: List<String>): ParsedInput = Game.parse(rawInput)

    override fun part1(input: ParsedInput): Long {
        return countSteps("AAA", input) { it == "ZZZ" }
    }

    override fun part2(input: ParsedInput): Long {
        return input.network.keys.filter { it.endsWith('A') }
            .map { start -> countSteps(start, input) { it.endsWith('Z') }.toBigInteger() }
            .fold(BigInteger.valueOf(1L), ::lcm).toLong()
    }

    fun countSteps(start: String, game: Game, isExit: (String) -> Boolean): Long {
        var current = start
        var step = 0L
        while (!isExit(current)) current = game.getNextNode(current, step++)
        return step
    }

    fun lcm(a: BigInteger, b: BigInteger): BigInteger = a * b / a.gcd(b)
}

fun main() {
    val puzzle = Day08()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 2)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample2.txt")) }, expected = 6)

    puzzle.run()
}