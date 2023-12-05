package day02

import dump
import readInput
import kotlin.math.max

fun main() {
    val cubesCount = mapOf("red" to 12, "green" to 13, "blue" to 14)
    fun isPossible(round: List<Pair<String, Int>>): Boolean {
        return round.all { it.second <= cubesCount.getValue(it.first) }
    }

    fun part1(input: List<Game>): Int {
        return input.filter { it.rounds.all(::isPossible) }.sumOf { it.id }
    }

    fun part2(input: List<Game>): Int {
        return input.map { game ->
            game.rounds.flatten()
                .groupingBy { it.first }
                .fold(0) { acc, x -> max(acc, x.second) }
                .values.fold(1) { acc, current -> acc * current }
        }.sumOf { it }
    }

    val testInput = readInputParsed("sample")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInputParsed("input")
    part1(input).dump("part1: ")
    part2(input).dump("part2: ")
}


fun readInputParsed(name: String): List<Game> = readInput(2, name).map(::parse)

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

data class Game(val id: Int, val rounds: List<List<Pair<String, Int>>>)