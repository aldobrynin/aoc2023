package day01

import dump
import readInput

fun main() {
    val wordsToDigits = mapOf(
        "one" to 1, "two" to 2, "three" to 3,
        "four" to 4, "five" to 5, "six" to 6,
        "seven" to 7, "eight" to 8, "nine" to 9,
    )
    fun parseDigits(line: String): Collection<Int> = line.map { it.digitToIntOrNull() }.filterNotNull()

    fun parseWithWords(line: String): Collection<Int> = line.mapIndexed { index, _ ->
        line[index].digitToIntOrNull()
            ?: wordsToDigits.filter { line.startsWith(it.key, index) }.map { it.value }.firstOrNull()
    }.filterNotNull()

    fun getCalibrationValue(digits: Collection<Int>): Int = digits.first() * 10 + digits.last()

    fun part1(input: List<String>): Int {
        return input.map(::parseDigits).sumOf(::getCalibrationValue)
    }

    fun part2(input: List<String>): Int {
        return input.map(::parseWithWords).sumOf(::getCalibrationValue)
    }

    val testInput = readInput(1, "sample")
    check(part1(testInput) == 142)

    val input = readInput(1, "input")
    part1(input).dump("part1: ")
    part2(input).dump("part2: ")
}