package day02

import common.Puzzle
import product
import solveAndVerify
import kotlin.math.max

private data class Game(val id: Int, val rounds: List<List<Pair<String, Int>>>)

private class Day02 : Puzzle<List<Game>>(day = 2) {
    override fun parse(rawInput: List<String>): List<Game> {
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        fun parse(line: String): Game {
            val idAndRoundsRaw = line.substringAfter("Game ").split(':')
            val id = idAndRoundsRaw[0].toInt()
            val rounds = idAndRoundsRaw[1].split(';')
                .map { roundRaw ->
                    roundRaw.split(',')
                        .map { it.trim().split(" ") }
                        .map { Pair(it[1], it[0].toInt()) }
                }
            return Game(id, rounds)
        }
        return rawInput.map(::parse)
    }

    override fun part1(input: List<Game>): Long {
        val cubesCount = mapOf("red" to 12, "green" to 13, "blue" to 14)
        fun isPossible(round: List<Pair<String, Int>>): Boolean =
            round.all { it.second <= cubesCount.getValue(it.first) }
        return input.filter { it.rounds.all(::isPossible) }.sumOf { it.id.toLong() }
    }

    override fun part2(input: List<Game>): Long {
        return input.map { game ->
            game.rounds.flatten()
                .groupingBy { it.first }
                .fold(0L) { acc, x -> max(acc, x.second.toLong()) }
                .values.product()
        }.sumOf { it }
    }
}

fun main() {
    val puzzle = Day02()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 8)
    solveAndVerify({ puzzle.part2(puzzle.parse("sample.txt")) }, expected = 2286)

    puzzle.run()
}