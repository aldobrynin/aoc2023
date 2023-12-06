package day01

import common.Puzzle
import solveAndVerify

private class Day01 : Puzzle<List<String>>(day = 1) {
    private val wordsToDigits = mapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9,
    )

    private fun parseDigits(line: String): List<Int> = line.map { it.digitToIntOrNull() }.filterNotNull()

    private fun parseWithWords(line: String): List<Int> = line.mapIndexed { index, _ ->
        line[index].digitToIntOrNull() ?: wordsToDigits.filter { line.startsWith(it.key, index) }.map { it.value }
            .firstOrNull()
    }.filterNotNull()

    private fun getCalibrationValue(digits: List<Int>): Long = digits.first() * 10L + digits.last()
    override fun parse(rawInput: List<String>): List<String> = rawInput
    override fun part1(input: List<String>): Long = input.map(::parseDigits).sumOf(::getCalibrationValue)
    override fun part2(input: List<String>): Long = input.map(::parseWithWords).sumOf(::getCalibrationValue)
}

fun main() {
    val puzzle = Day01()

    solveAndVerify({ puzzle.part1(puzzle.parse("sample.txt")) }, expected = 142)

    puzzle.run()
}